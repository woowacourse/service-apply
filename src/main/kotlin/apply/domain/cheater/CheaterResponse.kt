package apply.domain.cheater

import java.time.LocalDateTime

data class CheaterResponse(
    val id: Long,
    val name: String,
    val email: String,
    val createdDateTime: LocalDateTime
)
