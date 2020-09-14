package apply.domain.applicant

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import support.createLocalDate

@DataJpaTest
internal class ApplicantRepositoryTest(
    @Autowired
    private val applicantRepository: ApplicantRepository
) {

    @BeforeEach
    internal fun setUp() {
        val applicants = listOf(
            Applicant(
                "홍길동1",
                "a@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 4, 17)
            ),
            Applicant(
                "홍길동2",
                "b@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5)
            ),
            Applicant(
                "홍길동3",
                "c@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1)
            )
        )
        applicantRepository.saveAll(applicants)
    }

    @Test
    fun findByNameContainingOrEmailContaining() {
        val nameContaining = applicantRepository.findByNameContainingOrEmailContaining("홍", "홍")
        val emailContaining = applicantRepository.findByNameContainingOrEmailContaining("a@", "a@")
        val blank = applicantRepository.findByNameContainingOrEmailContaining("", "")
        val empty = applicantRepository.findByNameContainingOrEmailContaining("4", "4")

        assertAll(
            { assertThat(nameContaining).hasSize(3) },
            { assertThat(emailContaining).hasSize(1) },
            { assertThat(blank).hasSize(3) },
            { assertThat(empty).isEmpty() }
        )
    }
}
