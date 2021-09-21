package apply.domain.authenticationcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import support.test.RepositoryTest
import java.time.LocalDateTime

private const val EMAIL: String = "test@email.com"

@RepositoryTest
class AuthenticationCodeRepositoryTest(
    private val authenticationCodeRepository: AuthenticationCodeRepository
) {
    @Test
    fun `가장 최근에 생성된 인증 코드를 조회한다`() {
        val now = LocalDateTime.now()
        val authenticationCodes = authenticationCodeRepository.saveAll(
            listOf(
                AuthenticationCode(EMAIL, createdDateTime = now),
                AuthenticationCode(EMAIL, createdDateTime = now.plusSeconds(1L)),
                AuthenticationCode(EMAIL, createdDateTime = now.plusSeconds(2L))
            )
        )
        val actual = authenticationCodeRepository.findFirstByEmailOrderByCreatedDateTimeDesc(EMAIL)
        assertThat(actual).isSameAs(authenticationCodes.last())
    }
}
