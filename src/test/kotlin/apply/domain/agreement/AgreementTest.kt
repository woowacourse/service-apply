package apply.domain.agreement

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class AgreementTest : StringSpec({
    "동의서 버전은 yyyyMMdd 형식으로 관리한다" {
        shouldNotThrowAny { createAgreement(version = 20240416) }
        shouldThrow<IllegalArgumentException> { createAgreement(version = 1) }
    }
})

fun createAgreement(
    version: Int = 20240416,
    content: String = "개인정보 수집 및 이용 동의서",
    id: Long = 0L,
): Agreement {
    return Agreement(version, content, id)
}
