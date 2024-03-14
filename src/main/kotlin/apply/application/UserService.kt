package apply.application

import apply.domain.member.Member
import apply.domain.member.MemberRepository
import apply.domain.member.findByEmail
import apply.domain.member.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
    private val memberRepository: MemberRepository,
    private val passwordGenerator: PasswordGenerator
) {
    fun getByEmail(email: String): Member {
        return memberRepository.findByEmail(email) ?: throw IllegalArgumentException("회원이 존재하지 않습니다. email: $email")
    }

    fun findAllByKeyword(keyword: String): List<UserResponse> {
        return memberRepository.findAllByKeyword(keyword).map(::UserResponse)
    }

    fun resetPassword(request: ResetPasswordRequest) {
        val member = getByEmail(request.email)
        member.resetPassword(request.name, request.birthday, passwordGenerator.generate())
        memberRepository.save(member)
    }

    fun editPassword(id: Long, request: EditPasswordRequest) {
        require(request.password == request.confirmPassword) { "새 비밀번호가 일치하지 않습니다." }
        memberRepository.getOrThrow(id).changePassword(request.oldPassword, request.password)
    }

    fun getInformation(id: Long): UserResponse {
        val member = memberRepository.getOrThrow(id)
        return UserResponse(member)
    }

    fun editInformation(id: Long, request: EditInformationRequest) {
        memberRepository.getOrThrow(id).changePhoneNumber(request.phoneNumber)
    }
}
