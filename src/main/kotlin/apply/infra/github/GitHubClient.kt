package apply.infra.github

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

private val log = KotlinLogging.logger { }
private const val API_VERSION_HEADER: String = "X-GitHub-Api-Version"
private const val API_VERSION: String = "2022-11-28"

@Component
class GitHubClient(
    private val gitHubProperties: GitHubProperties,
    restTemplateBuilder: RestTemplateBuilder,
) {
    private val restTemplate: RestTemplate = restTemplateBuilder
        .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
        .defaultHeader(AUTHORIZATION, bearerToken(gitHubProperties.accessKey))
        .defaultHeader(API_VERSION_HEADER, API_VERSION)
        .build()

    private fun bearerToken(token: String): String = "Bearer $token".takeIf { token.isNotEmpty() } ?: ""

    /**
     * 조회 시 커밋 날짜를 기준으로 오름차순으로 제공한다.
     * 커밋이 250개가 넘는 경우 별도 대응이 필요하다.
     * @see [API](https://docs.github.com/en/rest/pulls/pulls#list-commits-on-a-pull-request)
     */
    fun getCommitsFromPullRequest(
        owner: String,
        repo: String,
        pullNumber: Int,
        page: Int,
        size: Int,
    ): List<CommitResponse> {
        return getCommits("${gitHubProperties.uri}/repos/$owner/$repo/pulls/$pullNumber/commits?per_page=$size&page=$page")
    }

    /**
     * 조회 시 커밋 날짜를 기준으로 내림차순으로 제공한다.
     * @see [API](https://docs.github.com/en/rest/commits/commits#list-commits)
     */
    fun getCommitsFromRepository(owner: String, repo: String): List<CommitResponse> {
        return getCommits("${gitHubProperties.uri}/repos/$owner/$repo/commits")
    }

    private fun getCommits(url: String): List<CommitResponse> {
        val request = RequestEntity.get(url).build()
        return runCatching { restTemplate.exchange<List<CommitResponse>>(request) }
            .onFailure { handleException(it, url) }
            .map { it.body }
            .getOrThrow()
            ?: emptyList()
    }

    /**
     * @see [API](https://docs.github.com/en/rest/collaborators/invitations#list-repository-invitations-for-the-authenticated-user)
     */
    fun getInvitations(page: Int, size: Int): List<InvitationResponse> {
        val url = "${gitHubProperties.uri}/user/repository_invitations?per_page=$size&page=$page"
        val request = RequestEntity.get(url).build()
        return runCatching { restTemplate.exchange<List<InvitationResponse>>(request) }
            .onFailure { handleException(it, url) }
            .map { it.body }
            .getOrThrow()
            ?: emptyList()
    }

    /**
     * @see [API](https://docs.github.com/en/rest/collaborators/invitations#accept-a-repository-invitation)
     */
    fun acceptInvitation(invitationId: Long) {
        val url = "${gitHubProperties.uri}/user/repository_invitations/$invitationId"
        val request = RequestEntity.patch(url).build()
        runCatching { restTemplate.exchange<String>(request) }
            .onFailure { handleException(it, url) }
            .getOrThrow()
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
}
