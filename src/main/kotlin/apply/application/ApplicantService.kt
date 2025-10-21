package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.member.Member
import apply.domain.member.MemberRepository
import apply.domain.member.MemberStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

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
        return findAllByIdsAndKeyword(formsByApplicantId.keys, keyword)
            .map {
                when (it.status) {
                    MemberStatus.ACTIVE -> ApplicantAndFormResponse(
                        it,
                        cheaterApplicantEmails.contains(it.email),
                        formsByApplicantId.getValue(it.id),
                    )

                    else -> ApplicantAndFormResponse(
                        it.id,
                        email = "deleted+${it.id}@invalid.local",
                        name = "(탈퇴 회원)",
                        birthday = LocalDate.MIN,
                        phoneNumber = "010-0000-0000",
                        githubUsername = "",
                        isCheater = false,
                        applicationForm = formsByApplicantId.getValue(it.id),
                    )
                }
            }
    }

    private fun findAllByIdsAndKeyword(ids: Set<Long>, keyword: String?): List<Member> {
        return if (keyword.isNullOrEmpty()) {
            memberRepository.findAllByIdIn(ids)
        } else {
            memberRepository.findAllByKeyword(keyword).filter { ids.contains(it.id) }
        }
    }
}
