package apply.domain.authenticationcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

private const val EMAIL: String = "test@email.com"
private const val VALID_CODE: String = "VALID"
private const val INVALID_CODE: String = "INVALID"

internal class AuthenticationCodeTest {
    @Test
    fun `인증 코드를 생성한다`() {
        val authenticationCode = AuthenticationCode(EMAIL)
        assertAll(
            { assertThat(authenticationCode.code).isNotNull() },
            { assertThat(authenticationCode.authenticated).isFalse() },
            { assertThat(authenticationCode.createdDateTime).isNotNull() }
        )
    }

    @Test
    fun `코드를 인증한다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE)
        authenticationCode.authenticate(VALID_CODE)
        assertThat(authenticationCode.authenticated).isTrue()
    }

    @Test
    fun `이미 인증한 경우 다시 인증할 수 없다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, true)
        assertThrows<IllegalStateException> { authenticationCode.authenticate(VALID_CODE) }
    }

    @Test
    fun `코드가 일치하지 않으면 인증할 수 없다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE)
        assertThrows<IllegalArgumentException> { authenticationCode.authenticate(INVALID_CODE) }
    }

    @Test
    fun `인증시간이 10분이 지나면 인증할 수 없다`() {
        val authenticationCode =
            AuthenticationCode(EMAIL, VALID_CODE, createdDateTime = LocalDateTime.now().minusMinutes(11L))
        assertThrows<IllegalStateException> { authenticationCode.authenticate(VALID_CODE) }
    }
}
