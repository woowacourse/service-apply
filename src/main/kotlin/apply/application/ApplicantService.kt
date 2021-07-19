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

    fun findAllByRecruitmentIdAndKeyword(
        recruitmentId: Long,
        keyword: String?
    ): List<ApplicantAndFormResponse> {
        val formsByApplicantId = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.applicantId }
        val cheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }
        return findAllByIdsAndKeyword(formsByApplicantId.keys, keyword)
            .map {
                ApplicantAndFormResponse(it, cheaterApplicantIds.contains(it.id), formsByApplicantId.getValue(it.id))
            }
    }

    private fun findAllByIdsAndKeyword(ids: Set<Long>, keyword: String?): List<Applicant> {
        return if (keyword != null) {
            applicantRepository.findAllByKeyword(keyword).filter { ids.contains(it.id) }
        } else {
            applicantRepository.findAllById(ids)
        }
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
