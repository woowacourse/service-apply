package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantResponse
import apply.domain.applicant.Gender
import apply.domain.cheater.CheaterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAll(): List<ApplicantResponse> = applicantRepository.findAll().map {
        ApplicantResponse(it, cheaterRepository.existsByApplicantId(it.id))
    }

    fun findAllByIds(applicantIds: List<Long>): List<ApplicantResponse> =
        applicantRepository.findAllById(applicantIds).map {
            ApplicantResponse(it, cheaterRepository.existsByApplicantId(it.id))
        }

    fun findByValue(value: String): List<ApplicantResponse> =
        applicantRepository.findByNameContainingOrEmailContaining(value, value).map {
            ApplicantResponse(it, cheaterRepository.existsByApplicantId(it.id))
        }

    @PostConstruct
    private fun populateDummy() {
        if (applicantRepository.count() != 0L) {
            return
        }
        val applicants = listOf(
            Applicant(
                name = "홍길동1",
                email = "a@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 4, 17)
            ),
            Applicant(
                name = "홍길동2",
                email = "b@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.FEMALE,
                birthday = createLocalDate(2020, 5, 5)
            ),
            Applicant(
                name = "홍길동3",
                email = "c@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1)
            )
        )
        applicantRepository.saveAll(applicants)
    }
}
