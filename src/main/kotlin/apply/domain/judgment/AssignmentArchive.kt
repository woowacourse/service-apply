package apply.domain.judgment

import apply.domain.mission.SubmissionMethod
import java.time.LocalDateTime

interface AssignmentArchive {
    fun getLastCommit(pullRequestUrl: String, endDateTime: LocalDateTime): Commit
    fun getLastCommit(submissionMethod: SubmissionMethod, url: String, endDateTime: LocalDateTime): Commit
}
