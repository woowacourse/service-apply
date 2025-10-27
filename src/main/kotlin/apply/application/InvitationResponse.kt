package apply.application

import java.time.LocalDateTime

data class InvitationResponse(
    val id: Long,
    val githubUsername: String,
    val repositoryName: String,
    val repositoryFullName: String,
    val invitationDateTime: LocalDateTime,
    val expired: Boolean,
)
