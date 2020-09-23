package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantInformation
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import apply.domain.cheater.CheaterRepository
import apply.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import java.lang.IllegalArgumentException
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun findAll(): List<ApplicantResponse> = applicantRepository.findAll().map {
        ApplicantResponse(it, cheaterRepository.existsByApplicantId(it.id))
    }

    fun findAllByIds(applicantIds: List<Long>): List<ApplicantResponse> =
        applicantRepository.findAllById(applicantIds).map {
            ApplicantResponse(it, cheaterRepository.existsByApplicantId(it.id))
        }

    fun findByNameOrEmail(keyword: String): List<ApplicantResponse> =
        applicantRepository.findByNameContainingOrEmailContaining(keyword, keyword).map {
            ApplicantResponse(it, cheaterRepository.existsByApplicantId(it.id))
        }

    fun generateToken(applicantInformation: ApplicantInformation): String {
        val applicant = applicantRepository.findByEmail(applicantInformation.email)
            ?.also { it.validate(applicantInformation) }
            ?: applicantRepository.save(applicantInformation.toEntity())

        return jwtTokenProvider.createToken(applicant.email)
    }

    fun changePassword(applicantId: Long, password: String) {
        val applicant = applicantRepository.findById(applicantId).get()
        applicant.password = password
        applicantRepository.save(applicant)
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
                birthday = createLocalDate(2020, 4, 17),
                password = "password"
            ),
            Applicant(
                name = "홍길동2",
                email = "b@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.FEMALE,
                birthday = createLocalDate(2020, 5, 5),
                password = "password"
            ),
            Applicant(
                name = "홍길동3",
                email = "c@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1),
                password = "password"
            )
        )
        applicantRepository.saveAll(applicants)
    }
}
