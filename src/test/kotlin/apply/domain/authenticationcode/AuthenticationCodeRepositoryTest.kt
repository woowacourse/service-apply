package apply.domain.authenticationcode

import apply.EMAIL
import apply.createAuthenticationCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import support.test.RepositoryTest
import java.time.LocalDateTime

@RepositoryTest
class AuthenticationCodeRepositoryTest(
    private val authenticationCodeRepository: AuthenticationCodeRepository
) {
    @Test
    fun `가장 최근에 생성된 인증 코드를 조회한다`() {
        val now = LocalDateTime.now()
        val authenticationCodes = authenticationCodeRepository.saveAll(
            listOf(
                createAuthenticationCode(EMAIL),
                createAuthenticationCode(EMAIL, createdDateTime = now.plusSeconds(1L)),
                createAuthenticationCode(EMAIL, createdDateTime = now.plusSeconds(2L))
            )
        )
        val actual = authenticationCodeRepository.findFirstByEmailOrderByCreatedDateTimeDesc(EMAIL)
        assertThat(actual).isSameAs(authenticationCodes.last())
    }
}
