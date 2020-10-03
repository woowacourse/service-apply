package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantInformation
import apply.domain.applicant.ApplicantPasswordFindInformation
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantVerifyInformation
import apply.domain.applicant.Gender
import apply.domain.applicant.exception.ApplicantValidateException
import apply.domain.cheater.CheaterRepository
import apply.security.JwtTokenProvider
import apply.utils.RandomStringGenerator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val randomStringGenerator: RandomStringGenerator
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

    fun changePassword(applicantId: Long, password: String): String {
        val applicant =
            applicantRepository.findByIdOrNull(applicantId) ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")
        applicant.password = password

        return applicant.password
    }

    fun generateTokenByLogin(applicantVerifyInformation: ApplicantVerifyInformation): String {
        return when (
            applicantRepository.existsByNameAndEmailAndBirthdayAndPassword(
                applicantVerifyInformation.name,
                applicantVerifyInformation.email,
                applicantVerifyInformation.birthday,
                applicantVerifyInformation.password
            )
        ) {
            true -> jwtTokenProvider.createToken(applicantVerifyInformation.email)
            else -> throw ApplicantValidateException()
        }
    }

    fun resetPassword(applicantPasswordFindInformation: ApplicantPasswordFindInformation): String {
        return if (
            applicantRepository.existsByNameAndEmailAndBirthday(
                applicantPasswordFindInformation.name,
                applicantPasswordFindInformation.email,
                applicantPasswordFindInformation.birthday
            )
        ) {
            val applicant = applicantRepository.findByNameAndEmailAndBirthday(
                applicantPasswordFindInformation.name,
                applicantPasswordFindInformation.email,
                applicantPasswordFindInformation.birthday
            ) ?: throw ApplicantValidateException()

            changePassword(applicant.id, randomStringGenerator.generateRandomString())
        } else {
            throw ApplicantValidateException()
        }
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
