package apply

import apply.application.AgreementResponse
import apply.domain.agreement.Agreement
import support.flattenByMargin

private val AGREEMENT_CONTENT: String =
    """
        |<p>(주)우아한형제들은 아래와 같이 지원자의 개인정보를 수집 및 이용합니다.</p>
        |<br>
        |<p><strong>보유 및 이용기간</strong> : <strong><span style="font-size:1.2rem">탈퇴 시 또는 이용목적 달성 시 파기</span></strong>(단, 관련법령 및 회사정책에 의해 보관이 필요한 경우 해당기간 동안 보관)</p>
    """.flattenByMargin()

fun createAgreement(
    version: Int = 20240416,
    content: String = AGREEMENT_CONTENT,
    id: Long = 0L,
): Agreement {
    return Agreement(version, content, id)
}

fun createAgreementResponse(
    version: Int = 20240416,
    content: String = AGREEMENT_CONTENT,
    id: Long = 0L,
): AgreementResponse {
    return AgreementResponse(id, version, content)
}
