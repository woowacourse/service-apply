package apply.domain.judgment

import java.time.LocalDateTime

interface AssignmentArchive {
    fun getLastCommit(pullRequestUrl: String, endDateTime: LocalDateTime): Commit
}
