package apply.ui.api

import apply.application.UserService
import apply.application.MissionService
import apply.createMissionData
import apply.domain.evaluation.EvaluationRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

@WebMvcTest(
    controllers = [MissionController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class MissionControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var missionService: MissionService

    @MockkBean
    private lateinit var evaluationRepository: EvaluationRepository

    private val recruitmentId = 1L
    private val evaluationId = 1L

    @Test
    fun `과제를 추가한다`() {
        every { evaluationRepository.existsById(evaluationId) } returns true
        every { missionService.save(createMissionData()) } just Runs

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/missions",
            recruitmentId,
            evaluationId
        ) {
            content = objectMapper.writeValueAsString(createMissionData())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
        }
    }
}
