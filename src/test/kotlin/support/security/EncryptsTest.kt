package support.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EncryptsTest {
    @Test
    fun `sha256Encrypt - 동일한 입력에 동일한 결과를 출력한다`() {
        val input = "password"

        assertThat(sha256Encrypt(input) == sha256Encrypt(input)).isTrue()
    }

    @Test
    fun `sha256Encrypt - 충돌하지 않는 경우를 테스트한다`() {
        val first = "1q2w3e4r!"
        val second = "2w3e4r5t!"

        assertThat(sha256Encrypt(first) != sha256Encrypt(second)).isTrue()
    }
}
