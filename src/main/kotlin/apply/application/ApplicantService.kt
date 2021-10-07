package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.user.User
import apply.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicantService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val userRepository: UserRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAllByRecruitmentIdAndKeyword(
        recruitmentId: Long,
        keyword: String? = null
    ): List<ApplicantAndFormResponse> {
        val formsByApplicantId = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.userId }
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

    private fun findAllByIdsAndKeyword(ids: Set<Long>, keyword: String?): List<User> {
        return if (keyword != null) {
            userRepository.findAllByKeyword(keyword).filter { ids.contains(it.id) }
        } else {
            userRepository.findAllById(ids)
        }
    }
}
