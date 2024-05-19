package apply.domain.agreement

import apply.createAgreement
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class AgreementTest : StringSpec({
    "동의서 버전은 yyyyMMdd 형식으로 관리한다" {
        shouldNotThrowAny { createAgreement(version = 20240416) }
        shouldThrow<IllegalArgumentException> { createAgreement(version = 1) }
    }
})
