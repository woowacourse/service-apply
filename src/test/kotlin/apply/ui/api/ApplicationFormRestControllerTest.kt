package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.CreateApplicationFormRequest
import apply.application.MyApplicationFormResponse
import apply.application.UpdateApplicationFormRequest
import apply.createAnswerRequest
import apply.createApplicationForm
import apply.createApplicationForms
import apply.createUser
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(ApplicationFormRestController::class)
class ApplicationFormRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var applicationFormService: ApplicationFormService

    @MockkBean
    private lateinit var applicantService: ApplicantService

    @Test
    fun `지원서를 생성한다`() {
        val response = ApplicationFormResponse(createApplicationForm())
        every { applicationFormService.create(any(), any()) } returns response

        mockMvc.post("/api/application-forms") {
            jsonContent(CreateApplicationFormRequest(1L))
            bearer("valid_token")
        }.andExpect {
            status { isCreated() }
            content { success(response) }
        }.andDo {
            handle(document("application-form-post"))
        }
    }

    @Test
    fun `지원서를 수정한다`() {
        every { applicationFormService.update(any(), any()) } just Runs

        mockMvc.patch("/api/application-forms") {
            jsonContent(UpdateApplicationFormRequest(recruitmentId = 1L, answers = listOf(createAnswerRequest())))
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
        }.andDo {
            handle(document("application-form-patch"))
        }
    }

    @Test
    fun `내 지원서 요청에 정상적으로 응답한다`() {
        val responses = createApplicationForms().map(::MyApplicationFormResponse)
        every { applicationFormService.getMyApplicationForms(any()) } returns responses

        mockMvc.get("/api/application-forms/me") {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }.andDo {
            handle(document("application-form-me-get"))
        }
    }

    @Test
    fun `올바른 지원서 요청에 정상적으로 응답한다`() {
        val response = ApplicationFormResponse(createApplicationForm())
        every { applicationFormService.getApplicationForm(any(), any()) } returns response

        mockMvc.get("/api/application-forms") {
            bearer("valid_token")
            param("recruitmentId", "1")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("application-form-get"))
        }
    }

    @Test
    fun `최종 지원서를 조회하는 경우 400으로 응답한다`() {
        every {
            applicationFormService.getApplicationForm(any(), any())
        } throws IllegalStateException("이미 제출한 지원서는 열람할 수 없습니다.")

        mockMvc.get("/api/application-forms") {
            bearer("valid_token")
            param("recruitmentId", "1")
        }.andExpect {
            status { isBadRequest() }
        }.andDo {
            handle(document("application-form-get-bad-request"))
        }
    }

    @Test
    fun `특정 모집에 대한 특정 회원의 지원서가 없는 경우 404로 응답한다`() {
        every {
            applicationFormService.getApplicationForm(any(), any())
        } throws NoSuchElementException("해당하는 지원서가 없습니다.")

        mockMvc.get("/api/application-forms") {
            bearer("valid_token")
            param("recruitmentId", "1")
        }.andExpect {
            status { isNotFound() }
        }.andDo {
            handle(document("application-form-get-not-found"))
        }
    }

    @Test
    fun `특정 모집 id와 지원자에 대한 키워드(이름 or 이메일)로 지원서 정보들을 조회한다`() {
        val keyword = "아마찌"
        val responses = listOf(ApplicantAndFormResponse(createUser(name = keyword), false, createApplicationForm()))
        every { applicantService.findAllByRecruitmentIdAndKeyword(any(), any()) } returns responses

        mockMvc.get("/api/recruitments/{recruitmentId}/application-forms", 1L) {
            bearer("valid_token")
            param("keyword", keyword)
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }

    @Test
    fun `특정 모집 id에 지원완료한 지원서 정보들을 조회한다`() {
        val responses = listOf(
            ApplicantAndFormResponse(createUser(name = "로키"), false, createApplicationForm()),
            ApplicantAndFormResponse(createUser(name = "아마찌"), false, createApplicationForm())
        )
        every { applicantService.findAllByRecruitmentIdAndKeyword(any()) } returns responses

        mockMvc.get("/api/recruitments/{recruitmentId}/application-forms", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }
}
