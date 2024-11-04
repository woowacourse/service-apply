package apply.infra.github

import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.Commit
import apply.domain.mission.SubmissionMethod
import apply.domain.mission.SubmissionMethod.PRIVATE_REPOSITORY
import apply.domain.mission.SubmissionMethod.PUBLIC_PULL_REQUEST
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId

private val log = KotlinLogging.logger { }
private const val PAGE_SIZE: Int = 100
private val PULL_REQUEST_URL_PATTERN: Regex =
    "https://github\\.com/(?<owner>.+)/(?<repo>.+)/pull/(?<pullNumber>\\d+)".toRegex()
private val REPOSITORY_URL_PATTERN: Regex = "https://github\\.com/(?<owner>.+)/(?<repo>.+)".toRegex()

@Component
class GitHub(
    private val gitHubClient: GitHubClient,
) : AssignmentArchive {
    override fun getLastCommit(submissionMethod: SubmissionMethod, url: String, endDateTime: LocalDateTime): Commit {
        val commits = when (submissionMethod) {
            PUBLIC_PULL_REQUEST -> getCommitsFromPullRequest(url)
            PRIVATE_REPOSITORY -> getCommitsFromRepository(url, endDateTime)
        }
        log.debug { "commits: $commits" }
        return Commit(commits.last(endDateTime).hash)
    }

    private fun getCommitsFromPullRequest(url: String): List<CommitResponse> {
        val (owner, repo, pullNumber) = PULL_REQUEST_URL_PATTERN.extractParts(url)
        return generateSequence(1) { page -> page + 1 }
            .map { page -> gitHubClient.getCommitsFromPullRequest(owner, repo, pullNumber.toInt(), page, PAGE_SIZE) }
            .takeUntil { it.size < PAGE_SIZE }
            .flatten()
            .toList()
    }

    private fun getCommitsFromRepository(url: String, endDateTime: LocalDateTime): List<CommitResponse> {
        val (owner, repo) = REPOSITORY_URL_PATTERN.extractParts(url)
        return runCatching { gitHubClient.getCommitsFromRepository(owner, repo) }
            .getOrElse {
                when (it) {
                    is IllegalArgumentException -> acceptInvitationAndFetchCommits(owner, repo, endDateTime)
                    else -> throw it
                }
            }
    }

    private fun acceptInvitationAndFetchCommits(
        owner: String,
        repo: String,
        endDateTime: LocalDateTime,
    ): List<CommitResponse> {
        val invitation = getInvitations()
            .filter { it.createdAt.withZoneSameInstant(ZoneId.systemDefault()) <= endDateTime.atZone(ZoneId.systemDefault()) }
            .first { it.repository.fullName.equals("$owner/$repo", ignoreCase = true) }
        gitHubClient.acceptInvitation(invitation.id)
        return gitHubClient.getCommitsFromRepository(owner, repo)
    }

    private fun Regex.extractParts(url: String): List<String> {
        val result = find(url) ?: throw IllegalArgumentException("올바른 형식의 URL이어야 합니다.")
        return result.destructured.toList()
    }

    private fun List<CommitResponse>.last(endDateTime: LocalDateTime): CommitResponse {
        val zonedDateTime = endDateTime.atZone(ZoneId.systemDefault())
        return filter { it.date <= zonedDateTime }
            .maxByOrNull { it.date }
            ?: throw IllegalArgumentException("해당 커밋이 존재하지 않습니다. endDateTime: $endDateTime")
    }

    fun getInvitations(): List<InvitationResponse> {
        return generateSequence(1) { page -> page + 1 }
            .map { page -> gitHubClient.getInvitations(page, PAGE_SIZE) }
            .takeUntil { it.size < PAGE_SIZE }
            .flatten()
            .toList()
            .also { log.debug { "invitations: $it" } }
    }

    fun acceptInvitation(invitationId: Long) {
        gitHubClient.acceptInvitation(invitationId)
    }

    private fun <T> Sequence<T>.takeUntil(predicate: (T) -> Boolean): Sequence<T> {
        return sequence {
            for (element in this@takeUntil) {
                yield(element)
                if (predicate(element)) break
            }
        }
    }
}
