package apply.domain.administrator

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
        val administrator = createAdministrator()
        administratorRepository.save(administrator)

        expect("관리자 아이디로 관리자를 조회한다") {
            val actual = administratorRepository.getByUsername(administrator.username)
            actual shouldBe administrator
        }

        expect("관리자 아이디가 존재하지 않으면 예외가 발생한다") {
            shouldThrow<UsernameNotFoundException> {
                administratorRepository.getByUsername("invalid-username")
            }
        }

        expect("관리자 식별자로 관리자를 조회한다") {
            val actual = administratorRepository.getById(administrator.id)
            actual shouldBe administrator
        }

        expect("관리자 식별자로 조회되는 관리자가 존재하지 않으면 예외가 발생한다") {
            shouldThrow<NoSuchElementException> {
                administratorRepository.getById(-1L)
            }
        }

        expect("관리자 아이디가 존재하는지 확인한다") {
            administratorRepository.existsByUsername(administrator.username).shouldBeTrue()
        }
    }
})
