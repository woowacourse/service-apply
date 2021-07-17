package apply.ui.api

import apply.application.ApplicantService
import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.createApplicant
import apply.domain.cheater.Cheater
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.createLocalDateTime

@WebMvcTest(
    controllers = [CheaterRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class CheaterRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var applicantService: ApplicantService

    @MockkBean
    private lateinit var cheaterService: CheaterService

    @Test
    fun `모든 부정행위자를 찾는다`() {
        val expected = listOf(
            CheaterResponse(
                Cheater(
                    1L,
                    createLocalDateTime(2021, 10, 9, 10, 0, 0, 0)
                ),
                createApplicant(name = "로키")
            ),
            CheaterResponse(
                Cheater(
                    2L,
                    createLocalDateTime(2021, 10, 10, 10, 0, 0, 0)
                ),
                createApplicant(name = "아마찌")
            )
        )

        every { cheaterService.findAll() }.answers { expected }

        mockMvc.get("/api/cheaters")
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(expected))) }
            }
    }

    @Test
    internal fun `부정행위자를 추가한다`() {
        // given
        val cheatedApplicant = createApplicant(id = 1L, name = "로키")

        every { cheaterService.save(cheatedApplicant.id) } returns Unit

        mockMvc.post("/api/cheaters") {
            param("applicantId", cheatedApplicant.id.toString())
        }
            .andExpect {
                status { isOk }
            }
    }

    @Test
    internal fun `부정행위자를 삭제한다`() {
        val cheatedApplicant = createApplicant(id = 1L, name = "로키")

        every { cheaterService.deleteById(cheatedApplicant.id) } returns Unit

        mockMvc.delete("/api/cheaters/{applicantId}", cheatedApplicant.id)
            .andExpect {
                status { isOk }
            }
    }
}
