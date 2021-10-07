package apply.domain.cheater

import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import support.createLocalDate
import support.test.RepositoryTest

@RepositoryTest
internal class CheaterRepositoryTest(
    private val cheaterRepository: CheaterRepository
) {
    private val cheater = User(
        id = 1L,
        name = "홍길동1",
        email = "a@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = Password("password")
    )

    private val user = User(
        id = 2L,
        name = "홍길동2",
        email = "b@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = Password("password")
    )

    @BeforeEach
    internal fun setUp() {
        cheaterRepository.save(Cheater(cheater.email))
    }

    @Test
    fun `지원자의 부정 행위 여부를 확인한다`() {
        assertAll(
            { assertThat(cheaterRepository.existsByEmail(cheater.email)).isTrue() },
            { assertThat(cheaterRepository.existsByEmail(user.email)).isFalse() }
        )
    }
}
