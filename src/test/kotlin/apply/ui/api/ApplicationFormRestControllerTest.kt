package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.CreateApplicationFormRequest
import apply.application.MyApplicationFormResponse
import apply.createApplicationForm
import apply.createApplicationForms
import apply.createUser
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(
    controllers = [ApplicationFormRestController::class]
)
internal class ApplicationFormRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var applicationFormService: ApplicationFormService

    @MockkBean
    private lateinit var applicantService: ApplicantService

    private val applicationFormResponse = ApplicationFormResponse(createApplicationForm())
    private val myApplicationFormResponses = createApplicationForms().map(::MyApplicationFormResponse)
    private val userKeyword = "아마찌"
    private val applicantAndFormResponses = listOf(
        ApplicantAndFormResponse(createUser(name = "로키"), false, createApplicationForms()[0]),
        ApplicantAndFormResponse(createUser(name = userKeyword), false, createApplicationForms()[1])
    )
    private val applicantAndFormFindByUserKeywordResponses = listOf(applicantAndFormResponses[1])

    @Test
    fun `올바른 지원서 요청에 정상적으로 응답한다`() {
        every { applicationFormService.getApplicationForm(any(), any()) } returns applicationFormResponse

        mockMvc.get("/api/application-forms") {
            param("recruitmentId", "1")
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicationFormResponse))) }
        }
    }

    @Test
    fun `내 지원서 요청에 정상적으로 응답한다`() {
        every { applicationFormService.getMyApplicationForms(any()) } returns myApplicationFormResponses

        mockMvc.get("/api/application-forms/me") {
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(myApplicationFormResponses))) }
        }
    }

    @Test
    fun `지원서를 생성한다`() {
        every { applicationFormService.create(any(), any()) } returns applicationFormResponse

        mockMvc.post("/api/application-forms") {
            content = objectMapper.writeValueAsString(CreateApplicationFormRequest(1L))
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isCreated }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicationFormResponse))) }
        }
    }

    @Test
    fun `특정 모집 id와 지원자에 대한 키워드(이름 or 이메일)로 지원서 정보들을 조회한다`() {
        val recruitmentId = applicantAndFormResponses[0].applicationForm.recruitmentId

        every {
            applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, userKeyword)
        } returns applicantAndFormFindByUserKeywordResponses

        mockMvc.get("/api/recruitments/{recruitmentId}/application-forms", recruitmentId) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
            param("keyword", userKeyword)
        }
            .andExpect {
                status { isOk }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            ApiResponse.success(
                                applicantAndFormFindByUserKeywordResponses
                            )
                        )
                    )
                }
            }
    }

    @Test
    fun `특정 모집 id에 지원완료한 지원서 정보들을 조회한다`() {
        val recruitmentId = applicantAndFormResponses[0].applicationForm.recruitmentId

        every {
            applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId)
        } returns applicantAndFormResponses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/application-forms", recruitmentId
        ) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicantAndFormResponses))) }
        }
    }
}
