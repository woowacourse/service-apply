package apply.application

import apply.domain.authenticationcode.AuthenticationCode
import apply.domain.authenticationcode.AuthenticationCodeRepository
import apply.domain.authenticationcode.getLastByEmail
import apply.domain.user.UserAuthenticationException
import apply.domain.user.UserRepository
import apply.domain.user.existsByEmail
import apply.domain.user.findByEmail
import apply.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserAuthenticationService(
    private val userRepository: UserRepository,
    private val authenticationCodeRepository: AuthenticationCodeRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun generateToken(request: RegisterUserRequest): String {
        // todo: 회원 가입 시, email 이 authenticated 인지 확인하는 로직 추가
        val user = userRepository.findByEmail(request.email)
            ?.also { it.authenticate(request.toEntity()) }
            ?: userRepository.save(request.toEntity())
        return jwtTokenProvider.createToken(user.email)
    }

    fun generateTokenByLogin(request: AuthenticateUserRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?.also { it.authenticate(request.password) }
            ?: throw UserAuthenticationException()
        return jwtTokenProvider.createToken(user.email)
    }

    fun generateAuthenticationCode(email: String): String {
        check(!userRepository.existsByEmail(email)) { "이미 등록된 이메일입니다." }
        val authenticationCode = authenticationCodeRepository.save(AuthenticationCode(email))
        return authenticationCode.code
    }

    fun authenticateEmail(email: String, code: String) {
        val authenticationCode = authenticationCodeRepository.getLastByEmail(email)
        authenticationCode.authenticate(code)
    }
}
