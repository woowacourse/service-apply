package apply.domain.authenticationcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import support.test.RepositoryTest

private const val EMAIL: String = "test@email.com"

@RepositoryTest
class AuthenticationCodeRepositoryTest(
    private val authenticationCodeRepository: AuthenticationCodeRepository
) {
    @Test
    fun `가장 최근에 생성된 인증 코드를 조회한다`() {
        val authenticationCodes = authenticationCodeRepository.saveAll(
            listOf(AuthenticationCode(EMAIL), AuthenticationCode(EMAIL), AuthenticationCode(EMAIL))
        )
        val actual = authenticationCodeRepository.findFirstByEmailOrderByCreatedDateTimeDesc(EMAIL)
        assertThat(actual).isSameAs(authenticationCodes.last())
    }

    @Test
    fun `인증된 이메일을 이메일로 조회한다`() {
        authenticationCodeRepository.saveAll(
            listOf(
                AuthenticationCode("authenticated@email.com", authenticated = true),
                AuthenticationCode("authenticated@email.com", authenticated = true),
                AuthenticationCode("unauthenticated@email.com")
            )
        )
        val actual = authenticationCodeRepository.existsByEmailAndAuthenticatedTrue("authenticated@email.com")
        assertAll(
            { assertThat(actual).isNotNull() },
            { assertThat(actual).isTrue() }
        )
    }

    @Test
    fun `인증되지 않은 이메일을 이메일로 조회한다`() {
        authenticationCodeRepository.saveAll(
            listOf(
                AuthenticationCode("authenticated@email.com", authenticated = true),
                AuthenticationCode("unauthenticated@email.com"),
                AuthenticationCode("unauthenticated@email.com")
            )
        )
        val actual = authenticationCodeRepository.existsByEmailAndAuthenticatedTrue("unauthenticated@email.com")
        assertAll(
            { assertThat(actual).isNotNull() },
            { assertThat(actual).isFalse() }
        )
    }
}
