package apply.domain.administrator

import apply.ADMINISTRATOR_USERNAME
import apply.createAdministrator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.springframework.security.core.userdetails.UsernameNotFoundException
import support.test.RepositoryTest

@RepositoryTest
class AdministratorRepositoryTest(
    private val administratorRepository: AdministratorRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("관리자 조회") {
        val username = ADMINISTRATOR_USERNAME
        val administrator = administratorRepository.save(createAdministrator(username = username))

        expect("일치하는 사용자명을 가진 관리자를 조회한다") {
            val actual = administratorRepository.getByUsername(username)
            actual shouldBe administrator
        }

        expect("일치하는 사용자명을 가진 관리자가 없으면 예외가 발생한다") {
            shouldThrow<UsernameNotFoundException> {
                administratorRepository.getByUsername("invalid_username")
            }
        }

        expect("식별자로 관리자를 조회한다") {
            val actual = administratorRepository.getOrThrow(administrator.id)
            actual shouldBe administrator
        }

        expect("일치하는 식별자를 가진 관리자가 없으면 예외가 발생한다") {
            shouldThrow<NoSuchElementException> {
                administratorRepository.getOrThrow(-1L)
            }
        }

        expect("일치하는 사용자명을 가진 관리자가 있는지 확인한다") {
            administratorRepository.existsByUsername(administrator.username).shouldBeTrue()
        }
    }
})
