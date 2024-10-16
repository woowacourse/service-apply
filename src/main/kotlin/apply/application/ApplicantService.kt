package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.member.Member
import apply.domain.member.MemberRepository
import apply.domain.member.findAllByIdIn
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicantService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val memberRepository: MemberRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAllByRecruitmentIdAndKeyword(
        recruitmentId: Long,
        keyword: String? = null
    ): List<ApplicantAndFormResponse> {
        val formsByApplicantId = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.memberId }
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

    private fun findAllByIdsAndKeyword(ids: Set<Long>, keyword: String?): List<Member> {
        return if (keyword != null) {
            memberRepository.findAllByKeyword(keyword).filter { ids.contains(it.id) }
        } else {
            memberRepository.findAllByIdIn(ids)
        }
    }
}
