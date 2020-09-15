package apply.domain.cheater

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
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
    applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    private val cheater = applicantRepository.save(
        Applicant(
            "홍길동1",
            "a@email.com",
            "010-0000-0000",
            Gender.MALE,
            createLocalDate(2020, 4, 17)
        )
    )

    private val appliant = applicantRepository.save(
        Applicant(
            "홍길동2",
            "b@email.com",
            "010-0000-0000",
            Gender.MALE,
            createLocalDate(2020, 4, 17)
        )
    )

    @BeforeEach
    internal fun setUp() {
        cheaterRepository.save(Cheater(cheater))
    }

    @Test
    fun `지원자의 부정 행위 여부를 확인한다`() {
        assertAll(
            { Assertions.assertThat(cheaterRepository.existsByApplicant(cheater)).isTrue() },
            { Assertions.assertThat(cheaterRepository.existsByApplicant(appliant)).isFalse() }
        )
    }
}
