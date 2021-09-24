package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.findAllByEmail
import apply.domain.applicant.findByEmailAndRecruitmentId
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
) {
    fun getByEmailAndRecruitmentId(email: String, recruitmentId: Long): Applicant {
        return applicantRepository.findByEmailAndRecruitmentId(email, recruitmentId) ?: throw IllegalArgumentException("지원자가 존재하지 않습니다. email: $email")
    }

    fun getAllByEmail(email: String): List<Applicant> {
        return applicantRepository.findAllByEmail(email)
    }

    fun findAllByRecruitmentIdAndKeyword(
        recruitmentId: Long,
        keyword: String? = null
    ): List<ApplicantAndFormResponse> {
        val formsByApplicantId = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.applicantId }
        val cheaterApplicantEmails = cheaterRepository.findAll().map { it.email }
        return findAllByIdsAndKeyword(formsByApplicantId.keys, keyword)
            .map {
                ApplicantAndFormResponse(
                    it,
                    cheaterApplicantEmails.contains(it.email),
                    formsByApplicantId.getValue(it.id)
                )
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
}
