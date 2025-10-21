package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.member.MemberRepository
import apply.domain.member.MemberStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicantService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val memberRepository: MemberRepository,
    private val cheaterRepository: CheaterRepository,
) {
    fun findAllByRecruitmentIdAndKeyword(
        recruitmentId: Long,
        keyword: String? = null,
    ): List<ApplicantAndFormResponse> {
        val formsByApplicantId = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.memberId }
        val cheaterApplicantEmails = cheaterRepository.findAll().map { it.email }
        return memberRepository.findAllByIdInAndKeyword(formsByApplicantId.keys, keyword)
            .map {
                when (it.status) {
                    MemberStatus.ACTIVE -> ApplicantAndFormResponse(
                        it,
                        cheaterApplicantEmails.contains(it.email),
                        formsByApplicantId.getValue(it.id),
                    )

                    else -> ApplicantAndFormResponse(it.id, formsByApplicantId.getValue(it.id))
                }
            }
    }
}
