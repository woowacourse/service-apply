package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.toSort

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
        keyword: String,
        offset: Int,
        limit: Int,
        orders: Map<String, String>
    ): List<ApplicantAndFormResponse> {
        val page = offset / limit
        return applicationFormRepository
            .findByRecruitmentIdAndKeyword(recruitmentId, keyword, PageRequest.of(page, limit, orders.toSort()))
            .content
    }

    fun findAllByRecruitmentIdAndSubmittedTrue(recruitmentId: Long): List<ApplicantAndFormResponse> {
        return applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.applicantId }
            .run { match { applicantRepository.findAllById(keys) } }
    }

    private fun Map<Long, ApplicationForm>.match(supplier: () -> List<Applicant>): List<ApplicantAndFormResponse> {
        val cheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }
        return supplier()
            .filter { containsKey(it.id) }
            .map { ApplicantAndFormResponse(it, cheaterApplicantIds.contains(it.id), getValue(it.id)) }
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

    fun count(recruitmentId: Long, keyword: String): Long {
        return applicationFormRepository.countByRecruitmentIdAndKeyword(recruitmentId, keyword)
    }
}
