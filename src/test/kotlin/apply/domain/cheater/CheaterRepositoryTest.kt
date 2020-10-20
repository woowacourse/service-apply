package apply.domain.cheater

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import support.createLocalDate
import support.test.BaseDataJpaTest

internal class CheaterRepositoryTest(
    private val cheaterRepository: CheaterRepository
) : BaseDataJpaTest() {
    private val cheater = Applicant(
        id = 1L,
        name = "홍길동1",
        email = "a@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = Password("password")
    )

    private val applicant = Applicant(
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
        cheaterRepository.save(Cheater(cheater.id))
    }

    @Test
    fun `지원자의 부정 행위 여부를 확인한다`() {
        assertAll(
            { assertThat(cheaterRepository.existsByApplicantId(cheater.id)).isTrue() },
            { assertThat(cheaterRepository.existsByApplicantId(applicant.id)).isFalse() }
        )
    }
}
