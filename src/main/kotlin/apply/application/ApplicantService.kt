package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(private val applicantRepository: ApplicantRepository) {
    fun findAll(): List<Applicant> {
        return applicantRepository.findAll()
    }

    fun findByName(name: String): Applicant? =
        applicantRepository.findByName(name)

    @PostConstruct
    private fun populateDummy() {
        if (applicantRepository.count() != 0L) {
            return
        }
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
                "a@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5)
            ),
            Applicant(
                "홍길동3",
                "a@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1)
            )
        )
        applicantRepository.saveAll(applicants)
    }
}
