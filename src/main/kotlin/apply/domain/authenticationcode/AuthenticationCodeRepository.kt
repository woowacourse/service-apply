package apply.domain.authenticationcode

import org.springframework.data.jpa.repository.JpaRepository

fun AuthenticationCodeRepository.getLastByEmail(email: String): AuthenticationCode {
    return findFirstByEmailOrderByCreatedDateTimeDesc(email)
        ?: throw IllegalArgumentException("인증 코드가 존재하지 않습니다. email: $email")
}

interface AuthenticationCodeRepository : JpaRepository<AuthenticationCode, Long> {
    fun findFirstByEmailOrderByCreatedDateTimeDesc(email: String): AuthenticationCode?
}
