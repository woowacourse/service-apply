package apply.domain.user

import java.time.LocalDateTime

data class PasswordResetEvent(
    val memberId: Long,
    val name: String,
    val email: String,
    val password: String,
    val occurredOn: LocalDateTime = LocalDateTime.now()
)
