package apply.infra.github

import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.Commit
import apply.domain.mission.SubmissionMethod
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.Forbidden
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import support.toUri
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class GitHubClient(
    private val gitHubProperties: GitHubProperties,
    restTemplateBuilder: RestTemplateBuilder
) : AssignmentArchive {
    private val restTemplate: RestTemplate = restTemplateBuilder.build()

    /**
     * @see [API](https://docs.github.com/en/rest/pulls/pulls#list-commits-on-a-pull-request)
     */
    override fun getLastCommit(
        submissionMethod: SubmissionMethod,
        url: String,
        endDateTime: LocalDateTime
    ): Commit {
        return when (submissionMethod) {
            SubmissionMethod.PUBLIC_PULL_REQUEST -> getLastCommitFromPullRequest(url, endDateTime)
            SubmissionMethod.PRIVATE_REPOSITORY -> getLastCommitFromRepository(url, endDateTime)
        }
    }

    private fun getLastCommitFromPullRequest(url: String, endDateTime: LocalDateTime): Commit {
        val (owner, repo, pullNumber) = extract(url)
        val requestEntity = RequestEntity
            .get("${gitHubProperties.uri}/repos/$owner/$repo/pulls/$pullNumber/commits?per_page=$PAGE_SIZE".toUri())
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, bearerToken(gitHubProperties.accessKey))
            .build()
        return runCatching { restTemplate.exchange<List<CommitResponse>>(requestEntity) }
            .onFailure {
                when (it) {
                    is Unauthorized -> throw RuntimeException("유효한 토큰이 아닙니다.")
                    is Forbidden -> throw RuntimeException("요청 한도에 도달했습니다.")
                    is NotFound -> throw IllegalArgumentException("PR이 존재하지 않습니다. pullRequestUrl: $url")
                    else -> throw RuntimeException("예기치 않은 예외가 발생했습니다.", it)
                }
            }
            .map { it.last(endDateTime) }.getOrThrow()
    }

    private fun extract(pullRequestUrl: String): List<String> {
        val result = PULL_REQUEST_URL_PATTERN.find(pullRequestUrl)
            ?: throw IllegalArgumentException("올바른 형식의 Pull Request URL이어야 합니다.")
        return result.destructured.toList()
    }

    /**
     * @see [API](https://docs.github.com/en/rest/commits/commits#list-commits)
     */
    fun getLastCommitFromRepository(url: String, endDateTime: LocalDateTime): Commit {
        val result = REPOSITORY_URL_PATTERN.find(url) ?: throw IllegalArgumentException("올바른 형식의 URL이어야 합니다.")
        val (owner, repo) = result.destructured.toList()
        val requestEntity = RequestEntity
            .get("${gitHubProperties.uri}/repos/$owner/$repo/commits")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, bearerToken(gitHubProperties.accessKey))
            .build()
        return runCatching { restTemplate.exchange<List<CommitResponse>>(requestEntity) }
            .onFailure {
                when (it) {
                    is Unauthorized -> throw RuntimeException("유효한 토큰이 아닙니다.")
                    is Forbidden -> throw RuntimeException("요청 한도에 도달했습니다.")
                    is NotFound -> throw IllegalArgumentException("저장소가 존재하지 않습니다. url: $url")
                    else -> throw RuntimeException("예기치 않은 예외가 발생했습니다.", it)
                }
            }
            .map { it.last(endDateTime) }.getOrThrow()
    }

    private fun bearerToken(token: String): String = if (token.isEmpty()) "" else "Bearer $token"

    private fun ResponseEntity<List<CommitResponse>>.last(endDateTime: LocalDateTime): Commit {
        val zonedDateTime = endDateTime.atZone(ZoneId.systemDefault())
        return body
            ?.filter { it.date <= zonedDateTime }
            ?.maxByOrNull { it.date }
            ?.let { Commit(it.hash) }
            ?: throw IllegalArgumentException("해당 커밋이 존재하지 않습니다. endDateTime: $endDateTime")
    }

    companion object {
        private const val PAGE_SIZE: Int = 100
        private val PULL_REQUEST_URL_PATTERN: Regex =
            "https://github\\.com/(?<owner>.+)/(?<repo>.+)/pull/(?<pullNumber>\\d+)".toRegex()
        private val REPOSITORY_URL_PATTERN: Regex = "https://github\\.com/(?<owner>.+)/(?<repo>.+)".toRegex()
    }
}
