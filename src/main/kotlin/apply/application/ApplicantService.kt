package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(val applicantRepository: ApplicantRepository) {
    fun findAll(): List<Applicant> {
        return applicantRepository.findAll()
    }

    fun getByName(name: String): Applicant =
        applicantRepository.findByName(name).orElse(null)

    @PostConstruct
    private fun populateDummy() {
        if (applicantRepository.count() != 0L) {
            return
        }
        val applicants = listOf(
            Applicant(
                "홍길동1",
                "@email.com",
                "010-0000-0000",
                Gender.MALE,
                "0000.00.00"
            ),
            Applicant(
                "홍길동2",
                "@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                "0000.00.00"
            ),
            Applicant(
                "홍길동3",
                "@email.com",
                "010-0000-0000",
                Gender.MALE,
                "0000.00.00"
            )
        )
        applicantRepository.saveAll(applicants)
    }
}
