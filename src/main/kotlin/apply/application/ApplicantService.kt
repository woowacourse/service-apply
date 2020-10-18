package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Password
import apply.domain.applicant.exception.ApplicantValidateException
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicantService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordGenerator: PasswordGenerator
) {
    fun findAllByRecruitmentIdAndSubmittedTrue(recruitmentId: Long): List<ApplicantResponse> {
        val applicationForms = applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.applicantId }
        val cheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }

        return applicantRepository.findAllById(applicationForms.keys).map {
            ApplicantResponse(it, cheaterApplicantIds.contains(it.id), applicationForms.getValue(it.id))
        }
    }

    fun findByRecruitmentIdAndKeyword(recruitmentId: Long, keyword: String): List<ApplicantResponse> =
        findAllByRecruitmentIdAndSubmittedTrue(recruitmentId)
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

    fun generateTokenByLogin(applicantVerifyInformation: ApplicantVerifyInformation): String {
        return when (
            applicantRepository.existsByEmailAndPassword(
                applicantVerifyInformation.email,
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
            passwordGenerator.generate().also { password = Password(it) }
        } ?: throw ApplicantValidateException()
    }

    fun editPassword(applicant: Applicant, request: EditPasswordRequest) {
        applicant.takeIf { it.isSamePassword(request.password) }
            ?.run {
                applicant.password = request.newPassword
                applicantRepository.save(applicant)
            } ?: throw ApplicantValidateException()
    }
}
