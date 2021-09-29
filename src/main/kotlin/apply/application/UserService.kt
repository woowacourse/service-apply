package apply.application

import apply.domain.user.User
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val userRepository: UserRepository,
    private val cheaterRepository: CheaterRepository,
    private val passwordGenerator: PasswordGenerator
) {
    fun getByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw IllegalArgumentException("지원자가 존재하지 않습니다. email: $email")
    }

    fun findAllByRecruitmentIdAndKeyword(
        recruitmentId: Long,
        keyword: String? = null
    ): List<UserAndFormResponse> {
        val formsByUserId = applicationFormRepository
            .findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.userId }
        val cheaterUserEmails = cheaterRepository.findAll().map { it.email }
        return findAllByIdsAndKeyword(formsByUserId.keys, keyword)
            .map {
                UserAndFormResponse(
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

    fun findAllByKeyword(keyword: String): List<UserResponse> {
        return userRepository.findAllByKeyword(keyword).map(::UserResponse)
    }

    fun resetPassword(request: ResetPasswordRequest): String {
        return passwordGenerator.generate().also {
            getByEmail(request.email).resetPassword(request.name, request.birthday, it)
        }
    }

    fun editPassword(id: Long, request: EditPasswordRequest) {
        userRepository.getOne(id).apply {
            changePassword(request.password, request.newPassword)
        }
    }
}
