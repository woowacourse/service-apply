package apply.domain.authenticationcode

import apply.EMAIL
import apply.createAuthenticationCode
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import support.test.RepositoryTest
import java.time.LocalDateTime

@RepositoryTest
class AuthenticationCodeRepositoryTest(
    private val authenticationCodeRepository: AuthenticationCodeRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("인증 코드 조회") {
        val now = LocalDateTime.now()
        val authenticationCodes = authenticationCodeRepository.saveAll(
            listOf(
                createAuthenticationCode(EMAIL, createdDateTime = now),
                createAuthenticationCode(EMAIL, createdDateTime = now.plusSeconds(1L)),
                createAuthenticationCode(EMAIL, createdDateTime = now.plusSeconds(2L))
            )
        )

        expect("가장 최근에 생성된 인증 코드를 조회한다") {
            val actual = authenticationCodeRepository.findFirstByEmailOrderByCreatedDateTimeDesc(EMAIL)
            actual shouldBe authenticationCodes.last()
        }
    }
})
