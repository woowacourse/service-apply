package support.security

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EncryptsTest : StringSpec({
    "sha256Encrypt - 동일한 입력에 동일한 결과를 출력한다" {
        val input = "password"
        sha256Encrypt(input) shouldBe sha256Encrypt(input)
    }

    "sha256Encrypt - 충돌하지 않는 경우를 테스트한다" {
        val first = "1q2w3e4r!"
        val second = "2w3e4r5t!"
        sha256Encrypt(first) shouldNotBe sha256Encrypt(second)
    }
})
