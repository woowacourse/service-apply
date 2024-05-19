package apply.ui.api

import apply.application.AgreementService
import apply.createAgreementResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.get

@WebMvcTest(AgreementRestController::class)
class AgreementRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var agreementService: AgreementService

    @Test
    fun `최신 버전의 동의서를 조회한다`() {
        val response = createAgreementResponse(id = 1L)
        every { agreementService.latest() } returns response

        mockMvc.get("/api/agreements/latest")
            .andExpect {
                status { isOk() }
                content { success(response) }
            }.andDo {
                print()
                handle(document("agreement-latest-get"))
            }
    }
}
