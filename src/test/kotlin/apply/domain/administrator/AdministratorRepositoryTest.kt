package apply.domain.administrator

import apply.createAdministrator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.UsernameNotFoundException
import support.test.RepositoryTest

@RepositoryTest
class AdministratorRepositoryTest(
    private val administratorRepository: AdministratorRepository
) {

    val administrator = createAdministrator()

    @BeforeEach
    fun setUp() {
        administratorRepository.save(administrator)
    }

    @Test
    fun `관리자 아이디로 관리자를 찾는다`() {
        val actual = administratorRepository.getByUsername(administrator.username)
        assertThat(actual).isEqualTo(administrator)
    }

    @Test
    fun `관리자 아이디가 존재하지 않으면 예외를 던진다`() {
        assertThrows<UsernameNotFoundException> {
            administratorRepository.getByUsername("invalid-username")
        }
    }

    @Test
    fun `관리자 식별자로 관리자를 찾는다`() {
        val actual = administratorRepository.getById(administrator.id)
        assertThat(actual).isEqualTo(administrator)
    }

    @Test
    fun `식별자로 조회되는 관리자가 존재하지 않으면 예외를 던진다`() {
        assertThrows<NoSuchElementException> {
            administratorRepository.getById(-1L)
        }
    }

    @Test
    fun `관리자 이름이 존재하는지 확인한다`() {
        assertThat(
            administratorRepository.existsByName(administrator.name)
        ).isTrue
    }

    @Test
    fun `관리자 아이디가 존재하는지 확인한다`() {
        assertThat(
            administratorRepository.existsByUsername(administrator.username)
        ).isTrue
    }
}
