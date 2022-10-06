package apply.application

import apply.ADMINISTRATOR_PASSWORD
import apply.ADMINISTRATOR_USERNAME
import apply.createAdministrator
import apply.createAdministratorData
import apply.createAdministratorUpdateFormData
import apply.domain.administrator.AdministratorRepository
import apply.domain.administrator.getById
import apply.domain.administrator.getByUsername
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder
import support.test.spec.afterRootTest

class AdministratorServiceTest : BehaviorSpec({
    val administratorRepository = mockk<AdministratorRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()

    val administratorService = AdministratorService(administratorRepository, passwordEncoder)

    Given("특정 사용자명을 가진 관리자가 있는 경우") {
        val username = ADMINISTRATOR_USERNAME
        val administrator = createAdministrator(username = username, id = 1L)

        every { administratorRepository.getByUsername(any()) } returns administrator

        When("해당 사용자명으로 관리자를 불러오면") {
            val actual = administratorService.loadUserByUsername(username)

            Then("관리자를 확인할 수 있다") {
                actual shouldBe administrator
            }
        }
    }

    Given("같은 사용자명의 관리자가 없는 경우") {
        val username = ADMINISTRATOR_USERNAME

        every { administratorRepository.existsByUsername(any()) } returns false
        every { passwordEncoder.encode(any()) } returns ADMINISTRATOR_PASSWORD
        every { administratorRepository.save(any()) } returns createAdministrator(username = username)

        When("관리자를 생성하면") {
            val actual = administratorService.save(createAdministratorData(username = username))

            Then("관리자가 생성된다") {
                actual.username shouldBe ADMINISTRATOR_USERNAME
            }
        }

        When("비밀번호와 확인 비밀번호를 일치시키지 않고 관리자를 생성하면") {
            val request = createAdministratorData(password = "password", confirmPassword = "wrong_password")

            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    administratorService.save(request)
                }
            }
        }
    }

    Given("같은 사용자명의 관리자가 존재하는 경우") {
        every { administratorRepository.existsByUsername(any()) } returns true

        When("해당 사용자명으로 관리자를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    administratorService.save(createAdministratorData(username = ADMINISTRATOR_USERNAME))
                }
            }
        }
    }

    Given("특정 관리자가 있는 경우") {
        val administrator = createAdministrator(id = 1L)

        every { administratorRepository.getById(any()) } returns administrator

        When("해당 관리자를 조회하면") {
            val actual = administratorService.findById(administrator.id)

            Then("관리자를 확인할 수 있다") {
                actual shouldBe AdministratorResponse(administrator)
            }
        }
    }

    Given("관리자가 여러 명인 경우") {
        val administrator1 = createAdministrator(username = "admin1", id = 1L)
        val administrator2 = createAdministrator(username = "admin2", id = 2L)
        val administrator3 = createAdministrator(username = "admin3", id = 3L)

        every { administratorRepository.findAll() } returns listOf(administrator1, administrator2, administrator3)

        When("모든 관리자를 조회하면") {
            val actual = administratorService.findAll()

            Then("모든 관리자를 확인할 수 있다") {
                actual shouldHaveSize 3
            }
        }
    }

    Given("정보 수정을 요청한 관리자가 있는 경우") {
        val administrator = createAdministrator()
        val updateAdministratorFormData = createAdministratorUpdateFormData()

        every { administratorRepository.getById(any()) } returns administrator

        When("해당 관리자를 수정하면") {
            administratorService.update(createAdministratorUpdateFormData())

            Then("관리자 정보를 수정할 수 있다") {
                administrator.name shouldBe updateAdministratorFormData.name
                administrator.password shouldBe updateAdministratorFormData.password
            }
        }

        When("비밀번호와 확인 비밀번호를 일치시키지 않고 해당 관리자를 수정하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    administratorService.update(
                        createAdministratorUpdateFormData(
                            password = "ABCD1234", passwordConfirmation = "4321DCBA"
                        )
                    )
                }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
