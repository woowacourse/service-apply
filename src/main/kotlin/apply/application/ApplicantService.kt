package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.user.User
import apply.domain.user.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

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
        val formsByUserId = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.userId }
        val cheaterUserEmails = cheaterRepository.findAll().map { it.email }
        return findAllByIdsAndKeyword(formsByUserId.keys, keyword)
            .map {
                ApplicantAndFormResponse(
                    it,
                    cheaterUserEmails.contains(it.email),
                    formsByUserId.getValue(it.id)
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