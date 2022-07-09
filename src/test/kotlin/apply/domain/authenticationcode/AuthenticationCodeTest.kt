package apply.domain.authenticationcode

import apply.EMAIL
import apply.INVALID_CODE
import apply.VALID_CODE
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import java.time.LocalDateTime

internal class AuthenticationCodeTest : AnnotationSpec() {
    @Test
    fun `인증 코드를 생성한다`() {
        val authenticationCode = AuthenticationCode(EMAIL)
        assertSoftly {
            authenticationCode.code.shouldNotBeNull()
            authenticationCode.authenticated.shouldBeFalse()
            authenticationCode.createdDateTime.shouldNotBeNull()
        }
    }

    @Test
    fun `코드를 인증한다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE)
        authenticationCode.authenticate(VALID_CODE)
        authenticationCode.authenticated.shouldBeTrue()
    }

    @Test
    fun `이미 인증한 경우 다시 인증할 수 없다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, true)
        shouldThrowExactly<IllegalStateException> { authenticationCode.authenticate(VALID_CODE) }
    }

    @Test
    fun `코드가 일치하지 않으면 인증할 수 없다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE)
        shouldThrowExactly<IllegalArgumentException> { authenticationCode.authenticate(INVALID_CODE) }
    }

    @Test
    fun `인증시간이 10분이 지나면 인증할 수 없다`() {
        val now = LocalDateTime.now()
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, createdDateTime = now.minusMinutes(11L))
        shouldThrowExactly<IllegalStateException> { authenticationCode.authenticate(VALID_CODE) }
    }

    @Test
    fun `일치하지 않은 코드로 검증한다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, true)
        shouldThrowExactly<IllegalStateException> { authenticationCode.validate(INVALID_CODE) }
    }

    @Test
    fun `인증 여부를 검증한다`() {
        val authenticationCode = AuthenticationCode(EMAIL, VALID_CODE, false)
        shouldThrowExactly<IllegalStateException> { authenticationCode.validate(VALID_CODE) }
    }
}
