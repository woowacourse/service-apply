package apply.infra.github

import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.Commit
import apply.domain.mission.SubmissionMethod
import apply.domain.mission.SubmissionMethod.PRIVATE_REPOSITORY
import apply.domain.mission.SubmissionMethod.PUBLIC_PULL_REQUEST
import mu.KotlinLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.Forbidden
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.time.LocalDateTime
import java.time.ZoneId

private val log = KotlinLogging.logger { }
private const val API_VERSION_HEADER: String = "X-GitHub-Api-Version"
private const val API_VERSION: String = "2022-11-28"
private const val PAGE_SIZE: Int = 100
private val PULL_REQUEST_URL_PATTERN: Regex =
    "https://github\\.com/(?<owner>.+)/(?<repo>.+)/pull/(?<pullNumber>\\d+)".toRegex()
private val REPOSITORY_URL_PATTERN: Regex = "https://github\\.com/(?<owner>.+)/(?<repo>.+)".toRegex()

@Component
class GitHubClient(
    private val gitHubProperties: GitHubProperties,
    restTemplateBuilder: RestTemplateBuilder,
) : AssignmentArchive {
    private val restTemplate: RestTemplate = restTemplateBuilder
        .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
        .defaultHeader(AUTHORIZATION, bearerToken(gitHubProperties.accessKey))
        .defaultHeader(API_VERSION_HEADER, API_VERSION)
        .build()

    private fun bearerToken(token: String): String = "Bearer $token".takeIf { token.isNotEmpty() } ?: ""

    override fun getLastCommit(submissionMethod: SubmissionMethod, url: String, endDateTime: LocalDateTime): Commit {
        val commits = when (submissionMethod) {
            PUBLIC_PULL_REQUEST -> getCommitsFromPullRequest(url)
            PRIVATE_REPOSITORY -> getCommitsFromRepository(url)
        }
        return Commit(commits.last(endDateTime).hash)
    }

    /**
     * 조회 시 커밋 날짜를 기준으로 오름차순으로 제공한다.
     * 커밋이 250개가 넘는 경우 별도 대응이 필요하다.
     * @see [API](https://docs.github.com/en/rest/pulls/pulls#list-commits-on-a-pull-request)
     */
    private fun getCommitsFromPullRequest(url: String): List<CommitResponse> {
        val (owner, repo, pullNumber) = PULL_REQUEST_URL_PATTERN.extractParts(url)
        return generateSequence(1) { page -> page + 1 }
            .map { page -> getCommits("${gitHubProperties.uri}/repos/$owner/$repo/pulls/$pullNumber/commits?per_page=$PAGE_SIZE&page=$page") }
            .takeWhile { it.isNotEmpty() }
            .flatten()
            .toList()
    }

    /**
     * 조회 시 커밋 날짜를 기준으로 내림차순으로 제공한다.
     * @see [API](https://docs.github.com/en/rest/commits/commits#list-commits)
     */
    private fun getCommitsFromRepository(url: String): List<CommitResponse> {
        val (owner, repo) = REPOSITORY_URL_PATTERN.extractParts(url)
        return getCommits("${gitHubProperties.uri}/repos/$owner/$repo/commits")
    }

    private fun Regex.extractParts(url: String): List<String> {
        val result = find(url) ?: throw IllegalArgumentException("올바른 형식의 URL이어야 합니다.")
        return result.destructured.toList()
    }

    private fun getCommits(url: String): List<CommitResponse> {
        val request = RequestEntity.get(url).build()
        return runCatching { restTemplate.exchange<List<CommitResponse>>(request) }
            .onFailure { handleException(it, url) }
            .map { it.body }
            .getOrThrow()
            ?: emptyList()
    }

    private fun handleException(exception: Throwable, url: String) {
        val response = (exception as? RestClientResponseException)?.responseBodyAsString ?: throw exception
        log.error { "error response: $response, url: $url" }
        when (exception) {
            is Unauthorized -> throw IllegalStateException("유효한 토큰이 아닙니다.")
            is Forbidden -> throw IllegalStateException("요청 한도에 도달했습니다.")
            is NotFound -> throw IllegalArgumentException("리소스가 존재하지 않거나 접근할 수 없습니다.")
            else -> throw RuntimeException("예기치 않은 예외가 발생했습니다.", exception)
        }
    }

    private fun List<CommitResponse>.last(endDateTime: LocalDateTime): CommitResponse {
        val zonedDateTime = endDateTime.atZone(ZoneId.systemDefault())
        return filter { it.date <= zonedDateTime }
            .maxByOrNull { it.date }
            ?: throw IllegalArgumentException("해당 커밋이 존재하지 않습니다. endDateTime: $endDateTime")
    }
}
