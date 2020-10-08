package apply.ui.api

import apply.application.ApplicantService
import apply.application.AppliedRecruitmentResponse
import apply.application.RecruitmentItemService
import apply.application.RecruitmentService
import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import apply.domain.recruitment.RecruitmentStatus
import apply.security.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.createLocalDate
import support.createLocalDateTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(
    controllers = [RecruitmentRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
internal class RecruitmentRestControllerTest(
    private val objectMapper: ObjectMapper
) {
    @MockkBean
    private lateinit var recruitmentService: RecruitmentService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var applicantService: ApplicantService

    @MockkBean
    private lateinit var recruitmentItemService: RecruitmentItemService
    private lateinit var mockMvc: MockMvc

    private val applicant = Applicant(
        name = "홍길동1",
        email = "applicant_email@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = "password",
        id = 1L
    )

    private val appliedRecruitments = listOf(
        AppliedRecruitmentResponse(
            id = 1L,
            title = "웹 백엔드 1기",
            startDateTime = createLocalDateTime(2018, 10, 25, 10),
            endDateTime = createLocalDateTime(2018, 11, 5, 10),
            canRecruit = true,
            isHidden = false,
            status = RecruitmentStatus.ENDED,
            submitted = true
        ),
        AppliedRecruitmentResponse(
            id = 2L,
            title = "웹 백엔드 2기",
            startDateTime = createLocalDateTime(2019, 10, 25, 10),
            endDateTime = createLocalDateTime(2019, 11, 5, 10),
            canRecruit = true,
            isHidden = false,
            status = RecruitmentStatus.ENDED,
            submitted = true
        ),
        AppliedRecruitmentResponse(
            id = 3L,
            title = "웹 백엔드 3기",
            startDateTime = createLocalDateTime(2020, 10, 25, 10),
            endDateTime = createLocalDateTime(2020, 11, 5, 10),
            canRecruit = true,
            isHidden = false,
            status = RecruitmentStatus.RECRUITABLE,
            submitted = false
        )
    )

    @BeforeEach
    internal fun setUp(webApplicationContext: WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    @Test
    fun `지원한 지원서 요청에 정상적으로 응답한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns applicant.email
        every { applicantService.getByEmail(applicant.email) } returns applicant
        every { recruitmentService.findAllByApplicantId(applicant.id) } returns appliedRecruitments

        mockMvc.get("/api/recruitments/applied") {
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(appliedRecruitments)) }
        }
    }
}
