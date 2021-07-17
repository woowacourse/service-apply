package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicantService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository,
    private val passwordGenerator: PasswordGenerator
) {
    fun getByEmail(email: String): Applicant {
        return applicantRepository.findByEmail(email) ?: throw IllegalArgumentException("지원자가 존재하지 않습니다. email: $email")
    }

    fun findAllByRecruitmentIdAndSubmittedTrueAndKeyword(
        recruitmentId: Long,
        keyword: String?
    ): List<ApplicantAndFormResponse> {
        val applicationFormMap = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.applicantId }
        val applicants = if (keyword != null) {
            applicantRepository.findAllByKeyword(keyword)
        } else {
            applicantRepository.findAllById(applicationFormMap.keys)
        }
        val cheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }
        return applicants.filter { applicationFormMap.containsKey(it.id) }
            .map { ApplicantAndFormResponse(it, cheaterApplicantIds.contains(it.id), applicationFormMap.getValue(it.id)) }
    }

    fun findAllByKeyword(keyword: String): List<ApplicantResponse> {
        return applicantRepository.findAllByKeyword(keyword).map(::ApplicantResponse)
    }

    fun resetPassword(request: ResetPasswordRequest): String {
        return passwordGenerator.generate().also {
            getByEmail(request.email).resetPassword(request.name, request.birthday, it)
        }
    }

    fun editPassword(id: Long, request: EditPasswordRequest) {
        applicantRepository.getOne(id).apply {
            changePassword(request.password, request.newPassword)
        }
    }
}
