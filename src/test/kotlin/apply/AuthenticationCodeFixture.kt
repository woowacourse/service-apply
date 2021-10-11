package apply

import apply.domain.authenticationcode.AuthenticationCode
import java.time.LocalDateTime

const val VALID_CODE: String = "VALID"
const val INVALID_CODE: String = "INVALID"
private const val AUTHENTICATION_EMAIL: String = "test@email.com"
private val CREATE_DATE_TIME: LocalDateTime = LocalDateTime.now()

fun createAuthenticationCode(
    email: String = AUTHENTICATION_EMAIL,
    code: String = VALID_CODE,
    authenticated: Boolean = false,
    createdDateTime: LocalDateTime = CREATE_DATE_TIME
): AuthenticationCode {
    return AuthenticationCode(email, code, authenticated, createdDateTime)
}
