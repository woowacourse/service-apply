package apply.ui.api

import apply.application.ApplicantService
import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.config.RestDocsConfiguration
import apply.createApplicant
import apply.domain.cheater.Cheater
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.createLocalDateTime
import support.test.TestEnvironment

@WebMvcTest(
    controllers = [CheaterRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
@Import(RestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestEnvironment
internal class CheaterRestControllerTest(
    private val objectMapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var applicantService: ApplicantService // todo: 이 객체를 MockBean으로 주입해야하는 이유 찾기

    @MockkBean
    private lateinit var cheaterService: CheaterService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    internal fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentationContextProvider: RestDocumentationContextProvider,
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .build()
    }

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
