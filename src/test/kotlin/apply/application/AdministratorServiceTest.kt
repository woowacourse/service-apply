package apply.application

import apply.ADMINISTRATOR_USERNAME
import apply.createAdministrator
import apply.createAdministratorData
import apply.domain.administrator.AdministratorRepository
import apply.domain.administrator.getById
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder
import support.test.IntegrationTest
import support.test.spec.afterRootTest

@IntegrationTest
class AdministratorServiceTest(
    private val passwordEncoder: PasswordEncoder
) : BehaviorSpec({
    val administratorRepository = mockk<AdministratorRepository>()

    val administratorService = AdministratorService(administratorRepository, passwordEncoder)

    Given("특정 관리자명 또는 특정 관리자 아이디를 가진 관리자가 존재하지 않는 경우") {
        val administrator = createAdministrator()

        every { administratorRepository.existsByUsername(any()) } returns false
        every { administratorRepository.save(any()) } returns createAdministrator()

        When("관리자를 추가하면") {
            val actual = administratorService.save(createAdministratorData())

            Then("관리자가 추가된다") {
                actual shouldBe AdministratorResponse(administrator)
            }
        }
    }

    Given("특정 관리자 아이디를 가진 관리자가 존재하는 경우") {
        val administrator = createAdministrator()

        every { administratorRepository.existsByUsername(any()) } returns true
        every { administratorRepository.save(any()) } returns createAdministrator()
        every { administratorRepository.findByUsername(any()) } returns administrator

        When("관리자를 추가하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    administratorService.save(createAdministratorData())
                }
            }
        }

        When("관리자를 불러오면") {
            val actual = administratorService.loadUserByUsername(ADMINISTRATOR_USERNAME)

            Then("관리자를 불러온다") {
                actual.username shouldBe ADMINISTRATOR_USERNAME
            }
        }
    }

    Given("패스워드와 패스워드 확인이 일치하지 않는 경우") {
        every { administratorRepository.existsByUsername(any()) } returns true
        every { administratorRepository.save(any()) } returns createAdministrator()

        When("관리자를 추가하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    administratorService.save(
                        createAdministratorData(
                            password = "ABCD1234", confirmPassword = "4321DCBA"
                        )
                    )
                }
            }
        }
    }

    Given("관리자가 존재하는 경우") {
        val administrator = createAdministrator()

        every { administratorRepository.getById(any()) } returns administrator

        When("특정 관리자를 조회하면") {
            val actual = administratorService.findById(administrator.id)

            Then("해당 관리자가 조회된다") {
                actual shouldBe AdministratorResponse(administrator)
            }
        }
    }

    Given("관리자가 여러 명인 경우") {
        val administrator1 = createAdministrator(name = "관리자1", username = "admin1", id = 1L)
        val administrator2 = createAdministrator(name = "관리자2", username = "admin2", id = 2L)
        val administrator3 = createAdministrator(name = "관리자3", username = "admin3", id = 3L)
        val administrators = listOf(
            administrator1, administrator2, administrator3
        )

        every { administratorRepository.findAll() } returns administrators

        When("관리자 전체를 조회하면") {
            val actual = administratorService.findAll()

            Then("모든 관리자가 조회된다") {
                actual shouldContainExactlyInAnyOrder listOf(
                    AdministratorResponse(administrator1),
                    AdministratorResponse(administrator2),
                    AdministratorResponse(administrator3)
                )
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
