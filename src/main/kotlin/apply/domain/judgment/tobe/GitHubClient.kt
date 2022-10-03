package apply.domain.judgment.tobe

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class GitHubClient(
    private val gitHubProperties: GitHubProperties
) : AssignmentArchive {
    override fun getLastCommit(pullRequestUrl: String, limitedDateTime: LocalDateTime): Commit {
        TODO("Not yet implemented")
    }
}
