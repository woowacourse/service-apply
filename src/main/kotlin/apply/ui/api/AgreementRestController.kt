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
                우아한형제들은 개인정보보호법, 정보통신망 이용촉진 및 정보보호 등에 관한 법률 등 관련 법령 상의 개인정보보호 규정을 준수하며, 개인정보의 보호에 최선을 다하고 있습니다.
                  
                1. 개인정보의 수집·이용 목적
                - 우아한테크코스 또는 우아한테크캠프 Pro 지원자 모집 및 대상자 선정

                2. 개인정보 수집 항목
                - [필수] 이름, 이메일, 연락처, 성별, 생년월일

                3. 개인정보 보유 및 이용기간
                - 우아한테크코스 또는 우아한테크캠프 Pro 지원 기수 교육 대상자 선정 이후 파기

                위 개인정보 수집·이용에 동의하지 않으실 수 있으며, 동의하지 않으실 경우 우아한테크코스 또는 우아한테크캠프 Pro 지원이 제한됩니다.
            """.trimIndent()
        )
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
