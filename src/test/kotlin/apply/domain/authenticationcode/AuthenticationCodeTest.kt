package apply.domain.authenticationcode

import apply.EMAIL
import apply.INVALID_CODE
import apply.VALID_CODE
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

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
        val now = LocalDateTime.now()
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, createdDateTime = now.minusMinutes(11L))
        assertThrows<IllegalStateException> { authenticationCode.authenticate(VALID_CODE) }
    }

    @Test
    fun `일치하지 않은 인증코드로 검증한다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, true)
        assertThrows<IllegalStateException> { authenticationCode.validate(INVALID_CODE) }
    }

    @Test
    fun `인증되지 않은 인증코드를 검증한다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE)
        assertThrows<IllegalStateException> { authenticationCode.validate(VALID_CODE) }
    }
}
