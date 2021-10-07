package apply.domain.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import support.createLocalDate
import support.test.RepositoryTest

@RepositoryTest
internal class UserRepositoryTest(private val userRepository: UserRepository) {
    @BeforeEach
    internal fun setUp() {
        val users = listOf(
            User(
                "홍길동1",
                "a@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 4, 17),
                Password("password")
            ),
            User(
                "홍길동2",
                "b@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5),
                Password("password")
            ),
            User(
                "홍길동3",
                "c@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1),
                Password("password")
            )
        )
        userRepository.saveAll(users)
    }

    @ParameterizedTest
    @CsvSource("홍,3", "a@,1", "'',3", "4,0")
    fun `이름 또는 이메일에 검색 키워드가 포함되는 회원들을 모두 조회한다`(keyword: String, expectedSize: Int) {
        val result = userRepository.findAllByKeyword(keyword)
        assertThat(result).hasSize(expectedSize)
    }

    @Test
    fun `이메일이 일치하는 회원을 조회한다`() {
        assertThat(userRepository.findByEmail("b@email.com")!!.name).isEqualTo("홍길동2")
    }

    @Test
    fun `이메일이 일치하는 회원이 존재하지 않을 때, null을 반환한다`() {
        assertThat(userRepository.findByEmail("notexist@email.com")).isNull()
    }

    @Test
    fun `이메일이 일치하는 회원들을 전부 조회한다`() {
        val emails = listOf("b@email.com", "c@email.com")
        assertThat(userRepository.findAllByEmailIn(emails)).hasSize(2)
    }

    @Test
    fun `이메일이 일치하는 회원이 있는지 확인한다`() {
        assertAll(
            { assertThat(userRepository.existsByEmail("a@email.com")).isTrue() },
            { assertThat(userRepository.existsByEmail("non-exists@email.com")).isFalse() }
        )
    }
}
