package apply.ui.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class AgreementResponse(val id: Long, val version: Int, val content: String)

@RequestMapping("/api/agreements")
@RestController
class AgreementRestController {
    @GetMapping("/latest")
    fun latest(): ResponseEntity<ApiResponse<AgreementResponse>> {
        val response = AgreementResponse(
            1L,
            20240418,
            """
                (주)우아한형제들은 아래와 같이 지원자의 개인정보를 수집 및 이용합니다.
                
                <strong>보유 및 이용기간</strong> : <strong><span style="font-size:1.2rem">탈퇴 시 또는 이용목적 달성 시 파기</span></strong>(단, 관련법령 및 회사정책에 의해 보관이 필요한 경우 해당기간 동안 보관)
            """.trimIndent()
        )
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
