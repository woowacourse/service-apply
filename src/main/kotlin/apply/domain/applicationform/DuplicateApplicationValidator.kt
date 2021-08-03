package apply.domain.applicationform

import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitment.getById
import org.springframework.stereotype.Component

@Component
class DuplicateApplicationValidator(
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository
) : ApplicationValidator {
    override fun validate(applicantId: Long, recruitmentId: Long) {
        val appliedRecruitmentIds = applicationFormRepository.findAllByApplicantIdAndSubmittedTrue(applicantId)
            .map { it.recruitmentId }
        if (recruitmentId.alreadyApplied(appliedRecruitmentIds)) {
            throw DuplicateApplicationException("이미 지원한 모집입니다.")
        }
        if (appliedRecruitmentIds.hasSameTerm(recruitmentId)) {
            throw DuplicateApplicationException("같은 기수의 다른 모집에 지원했습니다.")
        }
    }

    private fun Long.alreadyApplied(recruitmentIds: List<Long>): Boolean {
        return recruitmentIds.contains(this)
    }

    private fun List<Long>.hasSameTerm(recruitmentId: Long): Boolean {
        val recruitment = recruitmentRepository.getById(recruitmentId)
        val term = recruitment.term ?: return false
        val recruitmentIds = recruitmentRepository.findAllByTerm(term).map { it.id }
        return intersect(recruitmentIds).isNotEmpty()
    }
}
