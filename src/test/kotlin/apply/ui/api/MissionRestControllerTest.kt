package apply.ui.api

import apply.application.MissionService
import apply.application.UserService
import apply.createMissionData
import apply.createMissionResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(
    controllers = [MissionRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class MissionRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var missionService: MissionService

    private val recruitmentId = 1L

    @Test
    fun `과제를 추가한다`() {
        every { missionService.save(createMissionData()) } just Runs

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/missions",
            recruitmentId
        ) {
            content = objectMapper.writeValueAsString(createMissionData())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `특정 모집의 모든 과제를 조회한다`() {
        val missionResponses = listOf(
            createMissionResponse(id = 1L, evaluationId = 1L, title = "과제1"),
            createMissionResponse(id = 2L, evaluationId = 2L, title = "과제2")
        )
        every { missionService.findAllByRecruitmentId(recruitmentId) } returns missionResponses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/missions", recruitmentId
        ).andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(missionResponses))) }
        }
    }

    @Test
    fun `과제를 삭제한다`() {
        val missionId = 1L
        every { missionService.deleteById(missionId) } just Runs

        mockMvc.delete(
            "/api/recruitments/{recruitmentId}/missions/{missionId}",
            recruitmentId,
            missionId
        ).andExpect {
            status { isOk }
        }
    }
}
