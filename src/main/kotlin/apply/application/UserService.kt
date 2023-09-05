package apply.application

import apply.domain.user.User
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import apply.domain.user.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordGenerator: PasswordGenerator
) {
    fun getByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw IllegalArgumentException("회원이 존재하지 않습니다. email: $email")
    }

    fun findAllByKeyword(keyword: String): List<UserResponse> {
        return userRepository.findAllByKeyword(keyword).map(::UserResponse)
    }

    fun resetPassword(request: ResetPasswordRequest) {
        val user = getByEmail(request.email)
        user.resetPassword(request.name, request.birthday, passwordGenerator.generate())
        userRepository.save(user)
    }

    fun editPassword(id: Long, request: EditPasswordRequest) {
        require(request.password == request.confirmPassword) { "새 비밀번호가 일치하지 않습니다." }
        userRepository.getOrThrow(id).changePassword(request.oldPassword, request.password)
    }

    fun getInformation(id: Long): UserResponse {
        val user = userRepository.getOrThrow(id)
        return UserResponse(user)
    }

    fun editInformation(id: Long, request: EditInformationRequest) {
        userRepository.getOrThrow(id).changePhoneNumber(request.phoneNumber)
    }
}
