package apply.ui.api

import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.MyApplicationFormResponse
import apply.createApplicationForm
import apply.createApplicationForms
import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.security.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import support.createLocalDate
import support.test.TestEnvironment

// @ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@WebMvcTest(
    controllers = [ApplicationFormRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
@TestEnvironment
@AutoConfigureRestDocs
@AutoConfigureMockMvc
internal class ApplicationFormRestControllerTest(
    private val objectMapper: ObjectMapper
) {
    @MockkBean
    private lateinit var applicationFormService: ApplicationFormService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var applicantService: ApplicantService
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val applicant = Applicant(
        name = "홍길동1",
        email = "applicant_email@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = Password("password"),
        id = 1L
    )

    private val applicationFormResponse = ApplicationFormResponse(
        createApplicationForm()
    )

    private val myApplicationFormResponses = createApplicationForms().map(::MyApplicationFormResponse)

    // @BeforeEach
    // internal fun setUp(
    //     webApplicationContext: WebApplicationContext,
    //     restDocumentationContextProvider: RestDocumentationContextProvider,
    //     restDocsMockMvcConfigurationCustomizer : RestDocsMockMvcConfigurationCustomizer,
    //     DefaultMockMvcBuilder
    // ) {
    //     mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
    //         .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
    //         .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
    //         .apply { documentationConfiguration(restDocumentationContextProvider) }
    //         .build()
    // }

    @Test
    fun `올바른 지원서 요청에 정상적으로 응답한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns applicant.email
        every { applicantService.getByEmail(applicant.email) } returns applicant
        every { applicationFormService.getApplicationForm(applicant.id, 2L) } returns applicationFormResponse

        mockMvc.get("/api/application-forms") {
            param("recruitmentId", "2")
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicationFormResponse))) }
        }.andDo {
            print()
            handle(
                document(
                    "create-applicantionform",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    // // pathParameters(
                    // //     parameterWithName("keyword").description("지원자 정보(이름, 이메일)"),
                    // //     parameterWithName("recruitmentId").description("모집 ID")
                    // // ),
                    // PayloadDocumentation.responseFields(
                    //     PayloadDocumentation.fieldWithPath("body.[]").description("지원자 목록")
                    // )
                    //     .andWithPrefix("body.[].", ApplicantRestControllerTest.FIELD_DESCRIPTORS)
                )
            )
        }
    }

    @Test
    fun `내 지원서 요청에 정상적으로 응답한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns applicant.email
        every { applicantService.getByEmail(applicant.email) } returns applicant
        every { applicationFormService.getMyApplicationForms(applicant.id) } returns myApplicationFormResponses

        mockMvc.get("/api/application-forms/me") {
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(myApplicationFormResponses))) }
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

    companion object {
        val FIELD_DESCRIPTORS = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("전화번호"),
            fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
            fieldWithPath("birthday").type(JsonFieldType.STRING).description("생년월일"),
            fieldWithPath("isCheater").type(JsonFieldType.BOOLEAN).description("부정행위여부"),
            fieldWithPath("applicationForm").type(JsonFieldType.ARRAY).description("부정행위여부")
        )
    }
}
