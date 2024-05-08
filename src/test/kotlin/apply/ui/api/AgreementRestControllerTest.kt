package apply.ui.api

import apply.application.AgreementResponse
import apply.application.AgreementService
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
        val response = AgreementResponse(
            1L,
            20240418,
            """
                <p>
                  (주)우아한형제들은 아래와 같이 지원자의 개인정보를 수집 및 이용합니다.
                  
                  <strong>보유 및 이용기간</strong> : <strong><span style="font-size:1.2rem">탈퇴 시 또는 이용목적 달성 시 파기</span></strong>(단, 관련법령 및 회사정책에 의해 보관이 필요한 경우 해당기간 동안 보관)
                </p>
            """.trimIndent()
        )
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
