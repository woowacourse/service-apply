package apply.domain.cheater

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor
import support.createLocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
internal class CheaterRepositoryTest(
    private val cheaterRepository: CheaterRepository
) {
    private val cheater = Applicant(
        id = 1L,
        name = "홍길동1",
        email = "a@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17)
    )

    private val applicant = Applicant(
        id = 2L,
        name = "홍길동2",
        email = "b@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17)
    )

    @BeforeEach
    internal fun setUp() {
        cheaterRepository.save(Cheater(cheater.id))
    }

    @Test
    fun `지원자의 부정 행위 여부를 확인한다`() {
        assertAll(
            { Assertions.assertThat(cheaterRepository.existsByApplicantId(cheater.id)).isTrue() },
            { Assertions.assertThat(cheaterRepository.existsByApplicantId(applicant.id)).isFalse() }
        )
    }
}
