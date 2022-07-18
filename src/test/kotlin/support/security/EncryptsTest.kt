package support.security

import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat

class EncryptsTest : StringSpec({
    "sha256Encrypt - 동일한 입력에 동일한 결과를 출력한다" {
        val input = "password"

        assertThat(sha256Encrypt(input)).isEqualTo(sha256Encrypt(input))
    }

    "sha256Encrypt - 충돌하지 않는 경우를 테스트한다" {
        val first = "1q2w3e4r!"
        val second = "2w3e4r5t!"

        assertThat(sha256Encrypt(first)).isNotEqualTo(sha256Encrypt(second))
    }
})
