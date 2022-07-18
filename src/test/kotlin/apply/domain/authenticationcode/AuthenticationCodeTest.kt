package apply.domain.authenticationcode

import apply.EMAIL
import apply.INVALID_CODE
import apply.VALID_CODE
import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class AuthenticationCodeTest : StringSpec({
    "인증 코드를 생성한다" {
        val authenticationCode = AuthenticationCode(EMAIL)
        assertAll(
            { assertThat(authenticationCode.code).isNotNull() },
            { assertThat(authenticationCode.authenticated).isFalse() },
            { assertThat(authenticationCode.createdDateTime).isNotNull() }
        )
    }

    "코드를 인증한다" {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE)
        authenticationCode.authenticate(VALID_CODE)
        assertThat(authenticationCode.authenticated).isTrue()
    }

    "이미 인증한 경우 다시 인증할 수 없다" {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, true)
        assertThrows<IllegalStateException> { authenticationCode.authenticate(VALID_CODE) }
    }

    "코드가 일치하지 않으면 인증할 수 없다" {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE)
        assertThrows<IllegalArgumentException> { authenticationCode.authenticate(INVALID_CODE) }
    }

    "인증시간이 10분이 지나면 인증할 수 없다" {
        val now = LocalDateTime.now()
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, createdDateTime = now.minusMinutes(11L))
        assertThrows<IllegalStateException> { authenticationCode.authenticate(VALID_CODE) }
    }

    "일치하지 않은 코드로 검증한다" {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, true)
        assertThrows<IllegalStateException> { authenticationCode.validate(INVALID_CODE) }
    }

    "인증 여부를 검증한다" {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, false)
        assertThrows<IllegalStateException> { authenticationCode.validate(VALID_CODE) }
    }
})
