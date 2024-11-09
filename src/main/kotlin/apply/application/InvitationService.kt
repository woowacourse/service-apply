package apply.application

import apply.infra.github.GitHub
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class InvitationService(
    private val gitHub: GitHub,
) {
    fun findAll(): List<InvitationResponse> {
        return gitHub
            .getInvitations()
            .map {
                InvitationResponse(
                    it.id,
                    it.repository.owner.login,
                    it.repository.name,
                    it.repository.fullName,
                    it.createdAt.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime(),
                    it.expired
                )
            }
    }

    fun acceptAll(keyword: String, deadlineDateTime: LocalDateTime) {
        require(keyword.isNotBlank()) { "키워드는 빈 값일 수 없습니다." }
        findAll()
            .filter { it.invitationDateTime <= deadlineDateTime }
            .filter { it.repositoryFullName.contains(keyword, ignoreCase = true) }
            .forEach { accept(it.id) }
    }

    fun accept(invitationId: Long) {
        gitHub.acceptInvitation(invitationId)
    }

    fun decline(invitationId: Long) {
        gitHub.declineInvitation(invitationId)
    }
}
