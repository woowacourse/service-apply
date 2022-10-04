package apply.domain.judgment.tobe

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
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
    override fun getLastCommit(pullRequestUrl: String, endDateTime: LocalDateTime): Commit {
        val (owner, repo, pullNumber) = extract(pullRequestUrl)
        val requestEntity = RequestEntity
            .get("${gitHubProperties.uri}/repos/$owner/$repo/pulls/$pullNumber/commits?per_page=$PAGE_SIZE".toUri())
            .accept(MediaType.APPLICATION_JSON)
            // .header(HttpHeaders.AUTHORIZATION, "Bearer ${gitHubProperties.accessKey}")
            .build()
        val zonedDateTime = endDateTime.atZone(ZoneId.systemDefault())
        return restTemplate.exchange<List<CommitResponse>>(requestEntity).body
            ?.filter { it.date <= zonedDateTime }
            ?.maxByOrNull { it.date }
            ?.let { Commit(it.hash) }
            ?: throw IllegalArgumentException()
    }

    private fun extract(pullRequestUrl: String): List<String> {
        val result = PULL_REQUEST_URL_PATTERN.find(pullRequestUrl) ?: throw IllegalArgumentException()
        return result.destructured.toList()
    }

    companion object {
        private const val PAGE_SIZE: Int = 100
        private val PULL_REQUEST_URL_PATTERN: Regex =
            "https://github\\.com/(?<owner>.+)/(?<repo>.+)/pull/(?<pullNumber>\\d+)".toRegex()
    }
}
