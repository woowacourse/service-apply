package apply.domain.judgment.tobe

import java.time.LocalDateTime

interface AssignmentArchive {
    fun getLastCommit(pullRequestUrl: String, limitedDateTime: LocalDateTime): Commit
}
