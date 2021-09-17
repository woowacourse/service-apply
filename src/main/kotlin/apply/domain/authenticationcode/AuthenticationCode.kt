package apply.domain.authenticationcode

import support.domain.BaseEntity
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity

private const val EXPIRY_MINUTE_TIME = 10L

@Entity
class AuthenticationCode(
    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, columnDefinition = "char(8)")
    val code: String = UUID.randomUUID().toString().take(8),

    @Column(nullable = false)
    var authenticated: Boolean = false,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now()
) : BaseEntity() {
    fun authenticate(code: String) {
        check(!authenticated) { "이미 인증되었습니다." }
        require(this.code == code) { "인증 코드가 일치하지 않습니다." }
        check(createdDateTime.plusMinutes(EXPIRY_MINUTE_TIME) > LocalDateTime.now()) { "인증 코드가 만료되었습니다." }
        authenticated = true
    }
}
