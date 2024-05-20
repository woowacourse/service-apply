package apply.application

import apply.domain.authenticationcode.AuthenticationCode
import apply.domain.authenticationcode.AuthenticationCodeRepository
import apply.domain.authenticationcode.getLastByEmail
import apply.domain.member.Member
import apply.domain.member.MemberRepository
import apply.domain.member.UnidentifiedMemberException
import apply.domain.member.existsByEmail
import apply.domain.member.findByEmail
import apply.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MemberAuthenticationService(
    private val memberRepository: MemberRepository,
    private val authenticationCodeRepository: AuthenticationCodeRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun generateTokenByRegister(request: RegisterMemberRequest): String {
        require(request.password == request.confirmPassword) { "비밀번호가 일치하지 않습니다." }
        check(!memberRepository.existsByEmail(request.email)) { "이미 가입된 이메일입니다." }
        authenticationCodeRepository.getLastByEmail(request.email).validate(request.authenticationCode)
        val member = memberRepository.save(
            Member(
                email = request.email,
                password = request.password,
                name = request.name,
                birthday = request.birthday,
                phoneNumber = request.phoneNumber,
                githubUsername = request.githubUsername,
            )
        )
        return jwtTokenProvider.createToken(member.email)
    }

    fun generateTokenByLogin(request: AuthenticateMemberRequest): String {
        val member = memberRepository.findByEmail(request.email)
            ?: throw UnidentifiedMemberException("사용자 정보가 일치하지 않습니다.")
        member.authenticate(request.password)
        return jwtTokenProvider.createToken(member.email)
    }

    fun generateAuthenticationCode(email: String): String {
        check(!memberRepository.existsByEmail(email)) { "이미 등록된 이메일입니다." }
        val authenticationCode = authenticationCodeRepository.save(AuthenticationCode(email))
        return authenticationCode.code
    }

    fun authenticateEmail(email: String, code: String) {
        val authenticationCode = authenticationCodeRepository.getLastByEmail(email)
        authenticationCode.authenticate(code)
    }
}
