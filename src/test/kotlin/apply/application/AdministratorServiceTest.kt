package apply.application

import apply.ADMINISTRATOR_USERNAME
import apply.createAdministrator
import apply.createAdministratorData
import apply.domain.administrator.AdministratorRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest

@UnitTest
class AdministratorServiceTest {

    /*
    TODO : migrate to kotest
     */
    @MockK
    private lateinit var administratorRepository: AdministratorRepository

    private lateinit var administratorService: AdministratorService

    @BeforeEach
    fun setUp() {
        administratorService = AdministratorService(administratorRepository)

        every { administratorRepository.existsByName(any()) } returns false
        every { administratorRepository.existsByUsername(any()) } returns false
        every { administratorRepository.save(any()) } returns createAdministrator()
    }

    @Test
    fun `관리자를 추가한다`() {
        assertDoesNotThrow { administratorService.save(createAdministratorData()) }
    }

    @Test
    fun `중복된 아이디로 관리자를 생성할 수 없다`() {
        every { administratorRepository.existsByUsername(any()) } returns true
        assertThrows<IllegalStateException> { administratorService.save(createAdministratorData()) }
    }

    @Test
    fun `중복된 이름으로 관리자를 생성할 수 없다`() {
        every { administratorRepository.existsByName(any()) } returns true
        assertThrows<IllegalStateException> { administratorService.save(createAdministratorData()) }
    }

    @Test
    fun `패스워드와 패스워드 확인이 일치하지 않으면 관리자를 생성할 수 없다`() {
        assertThrows<IllegalStateException> {
            administratorService.save(
                createAdministratorData(
                    password = "ABCD1234", passwordConfirmation = "4321DCBA"
                )
            )
        }
    }

    @Test
    fun `관리자 아이디로 관리자를 불러온다`() {
        every { administratorRepository.findByUsername(any()) } returns createAdministrator()

        val administrator = administratorService.loadUserByUsername(ADMINISTRATOR_USERNAME)

        assertThat(administrator).isNotNull
    }

    @Test
    fun `관리자 목록을 조회한다`() {
        val administrators = listOf(
            createAdministrator(name = "adminA", username = "masterA", id = 1L),
            createAdministrator(name = "adminB", username = "masterB", id = 2L),
            createAdministrator(name = "adminC", username = "masterC", id = 3L)
        )

        every { administratorRepository.findAll() } returns administrators

        assertThat(administratorService.findAll()).hasSize(3)
    }
}
