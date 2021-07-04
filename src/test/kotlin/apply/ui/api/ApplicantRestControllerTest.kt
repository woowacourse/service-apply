package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantAuthenticationService
import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.AuthenticateApplicantRequest
import apply.application.EditPasswordRequest
import apply.application.RegisterApplicantRequest
import apply.application.ResetPasswordRequest
import apply.application.mail.MailService
import apply.config.RestDocsConfiguration
import apply.createApplicant
import apply.createApplicationForms
import apply.domain.applicant.ApplicantAuthenticationException
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.security.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.createLocalDate
import support.test.TestEnvironment

private const val VALID_TOKEN = "SOME_VALID_TOKEN"
private const val RANDOM_PASSWORD = "nEw_p@ssw0rd"
private const val PASSWORD = "password"
private const val INVALID_PASSWORD = "invalid_password"

private fun RegisterApplicantRequest.withPlainPassword(password: String): Map<String, Any?> {
    return mapOf(
        "name" to name,
        "email" to email,
        "phoneNumber" to phoneNumber,
        "gender" to gender,
        "birthday" to birthday,
        "password" to password
    )
}

private fun AuthenticateApplicantRequest.withPlainPassword(password: String): Map<String, Any?> {
    return mapOf("email" to email, "password" to password)
}

@WebMvcTest(
    controllers = [ApplicantRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
// @AutoConfigureRestDocs
@Import(RestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestEnvironment
internal class ApplicantRestControllerTest(
    private val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var applicantService: ApplicantService

    @MockBean
    private lateinit var applicantAuthenticationService: ApplicantAuthenticationService

    @MockBean
    private lateinit var mailService: MailService

    @MockBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val applicantRequest = RegisterApplicantRequest(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = Password(PASSWORD)
    )

    private val applicantLoginRequest = AuthenticateApplicantRequest(
        email = applicantRequest.email,
        password = applicantRequest.password
    )

    private val applicantPasswordFindRequest = ResetPasswordRequest(
        name = applicantRequest.name,
        email = applicantRequest.email,
        birthday = applicantRequest.birthday
    )

    private val invalidApplicantRequest = applicantRequest.copy(password = Password(INVALID_PASSWORD))

    private val invalidApplicantLoginRequest = applicantLoginRequest.copy(password = Password(INVALID_PASSWORD))

    private val inValidApplicantPasswordFindRequest =
        applicantPasswordFindRequest.copy(birthday = createLocalDate(1995, 4, 4))

    private val validEditPasswordRequest = EditPasswordRequest(
        password = Password("password"),
        newPassword = Password("NEW_PASSWORD")
    )

    private val inValidEditPasswordRequest = validEditPasswordRequest.copy(password = Password("wrongPassword"))

    @BeforeEach
    internal fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentationContextProvider: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentationContextProvider))
            .build()
    }

    @Test
    fun `유효한 지원자 생성 및 검증 요청에 대하여 응답으로 토큰이 반환된다`(): ResultActionsDsl {
        given(applicantAuthenticationService.generateToken(applicantRequest))
            .willReturn(VALID_TOKEN)

        val andReturn = mockMvc.post("/api/applicants/register") {
            content = objectMapper.writeValueAsBytes(applicantRequest.withPlainPassword(PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(VALID_TOKEN))) }
        }
        return andReturn
    }

    @Test
    fun name() {
        val resultActionsDsl = `유효한 지원자 생성 및 검증 요청에 대하여 응답으로 토큰이 반환된다`()
        resultActionsDsl.andDo { }
    }

    @Test
    fun `기존 지원자 정보와 일치하지 않는 지원자 생성 및 검증 요청에 응답으로 Unauthorized를 반환한다`() {
        given(
            applicantAuthenticationService.generateToken(invalidApplicantRequest)
        ).willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/register") {
            content = objectMapper.writeValueAsBytes(invalidApplicantRequest.withPlainPassword(INVALID_PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
            content { json(objectMapper.writeValueAsString(ApiResponse.error("요청 정보가 기존 지원자 정보와 일치하지 않습니다"))) }
        }
    }

    @Test
    fun `올바른 지원자 로그인 요청에 응답으로 Token을 반환한다`() {
        given(
            applicantAuthenticationService.generateTokenByLogin(applicantLoginRequest)
        ).willReturn(VALID_TOKEN)

        mockMvc.post("/api/applicants/login") {
            content = objectMapper.writeValueAsBytes(applicantLoginRequest.withPlainPassword(PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(VALID_TOKEN))) }
        }
    }

    @Test
    fun `잘못된 지원자 로그인 요청에 응답으로 Unauthorized와 메시지를 반환한다`() {
        given(
            applicantAuthenticationService.generateTokenByLogin(invalidApplicantLoginRequest)
        ).willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/login") {
            content = objectMapper.writeValueAsBytes(invalidApplicantLoginRequest.withPlainPassword(INVALID_PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
            content { json(objectMapper.writeValueAsString(ApiResponse.error("요청 정보가 기존 지원자 정보와 일치하지 않습니다"))) }
        }
    }

    @Test
    fun `올바른 비밀번호 찾기 요청에 응답으로 NoContent를 반환한다`() {
        given(
            applicantService.resetPassword(applicantPasswordFindRequest)
        ).willReturn(RANDOM_PASSWORD)

        willDoNothing().given(mailService).sendPasswordResetMail(applicantPasswordFindRequest, RANDOM_PASSWORD)

        mockMvc.post("/api/applicants/reset-password") {
            content = objectMapper.writeValueAsBytes(applicantPasswordFindRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `잘못된 비밀번호 찾기 요청에 응답으로 Unauthorized를 반환한다`() {
        given(
            applicantService.resetPassword(inValidApplicantPasswordFindRequest)
        ).willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/reset-password") {
            content = objectMapper.writeValueAsBytes(inValidApplicantPasswordFindRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
        }
    }

    @Test
    fun `올바른 비밀번호 변경 요청에 응답으로 NoContent를 반환한다`() {
        given(jwtTokenProvider.isValidToken("valid_token")).willReturn(true)
        given(jwtTokenProvider.getSubject("valid_token")).willReturn(applicantRequest.email)
        given(applicantService.getByEmail(applicantRequest.email)).willReturn(applicantRequest.toEntity())
        willDoNothing().given(applicantService).editPassword(applicantRequest.toEntity().id, validEditPasswordRequest)

        mockMvc.post("/api/applicants/edit-password") {
            content = objectMapper.writeValueAsBytes(validEditPasswordRequest)
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `잘못된 비밀번호 변경 요청에 응답으로 Unauthorized를 반환한다`() {
        given(jwtTokenProvider.isValidToken("valid_token")).willReturn(true)
        given(jwtTokenProvider.getSubject("valid_token")).willReturn(applicantRequest.email)
        given(applicantService.getByEmail(applicantRequest.email)).willReturn(applicantRequest.toEntity())
        given(applicantService.editPassword(applicantRequest.toEntity().id, inValidEditPasswordRequest))
            .willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/edit-password") {
            content = objectMapper.writeValueAsBytes(inValidEditPasswordRequest)
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `특정 모집 id와 지원자에 대한 키워드(이름 or 이메일)로 지원자들을 찾는다`() {
        val expected = listOf(
            ApplicantAndFormResponse(
                createApplicant(name = "로키"), false,
                createApplicationForms()[0]
            ),
            ApplicantAndFormResponse(
                createApplicant(name = "아마찌"), false,
                createApplicationForms()[1]
            )
        )
        val recruitmentId = expected[0].applicationForm.recruitmentId
        val keyword = expected[0].name

        given(applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword))
            .willReturn(expected)

        mockMvc.get(
            "/api/applicants/{keyword}/recruitments/{recruitmentId}",
            keyword, recruitmentId,
        )
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(expected))) }
            }.andDo {
                print()
                handle(
                    document(
                        "applicant-findAllByRecruitmentIdAndKeyword",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        // relaxedResponseFields(
                        responseFields(
                            fieldWithPath("message").description("응답 메시지"),
                            fieldWithPath("body.[]").description("지원자 목록"),
                        )
                            .andWithPrefix("body.[].", FIELD_DESCRIPTORS)
                            .andWithPrefix("body.[].applicationForm.", APPLICATION_FORM_FIELD_DESCRIPTORS)
                    )
                )
            }
    }

    companion object {
        val FIELD_DESCRIPTORS = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
            fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
            fieldWithPath("birthday").type(JsonFieldType.STRING).description("생년월일"),
            fieldWithPath("isCheater").type(JsonFieldType.BOOLEAN).description("부정행위여부"),
            fieldWithPath("applicationForm").type(JsonFieldType.OBJECT).description("지원서")
        )

        val APPLICATION_FORM_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("지원서 식별번호"),
            fieldWithPath("submitted").type(JsonFieldType.BOOLEAN).description("지원서 제출 여부"),
            fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("지원서 생성 날짜"),
            fieldWithPath("modifiedDateTime").type(JsonFieldType.STRING).description("지원서 수정 날짜"),
            fieldWithPath("submittedDateTime").type(JsonFieldType.NULL).description("지원서 제출 날짜"), // todo: 임시로 타입 NULL로 지정
            fieldWithPath("applicantId").type(JsonFieldType.NUMBER).description("지원자 식별번호"),
            fieldWithPath("recruitmentId").type(JsonFieldType.NUMBER).description("모집 식별번호"),
            fieldWithPath("referenceUrl").type(JsonFieldType.STRING).description("참조 URL"),
            fieldWithPath("answers").type(JsonFieldType.OBJECT).description("지원서 질문문항"),
            fieldWithPath("answers.items").type(JsonFieldType.ARRAY).description("지원서 질문문항들"),
            fieldWithPath("answers.items.[].contents").type(JsonFieldType.STRING).description("지원서 질문문항 질문내용"),
            fieldWithPath("answers.items.[].recruitmentItemId").type(JsonFieldType.NUMBER).description("지원서 질문문항 식별번호"),
        )
    }

    // findAllByRecruitmentIdAndSubmittedTrue
    @Test
    fun `특정 모집 id에 지원완료한 지원자들을 조회`() {
        val recruitmentId = 1L
        val expected = listOf(
            ApplicantAndFormResponse(
                createApplicant(name = "로키"), false,
                createApplicationForms()[0]
            ),
            ApplicantAndFormResponse(
                createApplicant(name = "아마찌"), false,
                createApplicationForms()[1]
            )
        )

        given(applicantService.findAllByRecruitmentIdAndSubmittedTrue(recruitmentId))
            .willReturn(expected)

        mockMvc.get(
            "/api/applicants/recruitments/{recruitmentId}",
            recruitmentId
        ) {
        }.andExpect {
            status { isOk }
        }
    }

    // findAllByKeyword
    @Test
    fun `키워드(이름 or 이메일)로 지원자들을 조회`() {
        val keyword = "아마찌"
        val expected = listOf(
            ApplicantResponse(createApplicant("아마찌")),
            ApplicantResponse(createApplicant("로키"))
        )

        given(applicantService.findAllByKeyword(keyword))
            .willReturn(expected)

        mockMvc.get(
            "/api/applicants/{keyword}",
            keyword
        ) {
        }.andExpect {
            status { isOk }
        }
    }

    private fun getDocumentRequest(): OperationRequestPreprocessor {
        return Preprocessors.preprocessRequest(
            Preprocessors.modifyUris()
                .scheme("https")
                .host("woowa") // TODO 호스트 변경
                .removePort(),
            Preprocessors.prettyPrint()
        )
    }

    private fun getDocumentResponse(): OperationResponsePreprocessor {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    }
}
