package apply.domain.member

import java.time.LocalDateTime

data class PasswordResetEvent(
    val memberId: Long,
    val name: String,
    val email: String,
    val password: String,
    val occurredOn: LocalDateTime = LocalDateTime.now()
)
