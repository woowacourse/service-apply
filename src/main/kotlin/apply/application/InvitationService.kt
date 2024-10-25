package apply.application

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class InvitationService {
    fun findAll(): List<InvitationResponse> {
        return listOf(
            InvitationResponse(
                id = 1L,
                githubUsername = "woowahan-pjs",
                repositoryName = "nextstep_test",
                repositoryFullName = "woowahan-pjs/nextstep_test",
                invitationDateTime = LocalDateTime.now(),
                expired = false
            ),
        )
    }

    fun acceptAll(keyword: String, deadlineDateTime: LocalDateTime) {
        throw UnsupportedOperationException()
    }

    fun accept(invitationId: Long) {
        throw UnsupportedOperationException()
    }

    fun decline(invitationId: Long) {
        throw UnsupportedOperationException()
    }
}
