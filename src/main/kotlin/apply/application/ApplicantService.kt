package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantInformation
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantVerifyInformation
import apply.domain.applicant.Gender
import apply.domain.applicant.ResetPasswordRequest
import apply.domain.applicant.exception.ApplicantValidateException
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.security.JwtTokenProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordGenerator: PasswordGenerator
) {
    fun findAllByRecruitmentId(recruitmentId: Long): List<ApplicantResponse> {
        val applicationForms = applicationFormRepository.findByRecruitmentId(recruitmentId)
            .associateBy { it.applicantId }
        val cheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }

        return applicantRepository.findAllById(applicationForms.keys).map {
            ApplicantResponse(it, cheaterApplicantIds.contains(it.id), applicationForms.getValue(it.id))
        }
    }

    fun findByRecruitmentIdAndKeyword(recruitmentId: Long, keyword: String): List<ApplicantResponse> =
        findAllByRecruitmentId(recruitmentId)
            .filter { it.name.contains(keyword) || it.email.contains(keyword) }

    fun findByNameOrEmail(keyword: String): List<ApplicantBasicResponse> =
        applicantRepository.findByNameContainingOrEmailContaining(keyword, keyword).map {
            ApplicantBasicResponse(it)
        }

    fun getByEmail(email: String): Applicant =
        applicantRepository.findByEmail(email) ?: throw IllegalArgumentException("email=$email 인 유저가 존재하지 않습니다")

    fun generateToken(applicantInformation: ApplicantInformation): String {
        val applicant = applicantRepository.findByEmail(applicantInformation.email)
            ?.also { it.validate(applicantInformation) }
            ?: applicantRepository.save(applicantInformation.toEntity())

        return jwtTokenProvider.createToken(applicant.email)
    }

    fun changePassword(applicantId: Long, password: String) {
        val applicant =
            applicantRepository.findByIdOrNull(applicantId) ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")
        applicant.password = password
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

    fun resetPassword(request: ResetPasswordRequest): String {
        return applicantRepository.findByNameAndEmailAndBirthday(
            request.name,
            request.email,
            request.birthday
        )?.run {
            password = passwordGenerator.generate()
            password
        } ?: throw ApplicantValidateException()
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
