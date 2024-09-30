package apply.infra.github

import apply.PUBLIC_PULL_REQUEST_URL
import apply.createCommit
import apply.domain.mission.SubmissionMethod.PRIVATE_REPOSITORY
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.web.client.RestTemplateBuilder
import support.createLocalDateTime
import support.test.IntegrationTest
import java.time.LocalDateTime.now

@IntegrationTest
class GitHubClientTest(
    private val gitHubClient: GitHubClient,
    private val properties: GitHubProperties,
    private val builder: RestTemplateBuilder,
) : StringSpec({
    val now = now()

    "설정된 날짜와 시간을 기준으로 마지막 커밋을 조회한다" {
        val actual = gitHubClient.getLastCommit(PUBLIC_PULL_REQUEST_URL, createLocalDateTime(2021, 10, 11))
        actual shouldBe createCommit("8c2d61313838d9220848bd38a5a5adc34efc5169")
    }

    "풀 리퀘스트의 마지막 커밋을 조회한다" {
        val actual = gitHubClient.getLastCommit(PUBLIC_PULL_REQUEST_URL, now)
        actual shouldBe createCommit("eeb43de3f53f4bec08e7d63f07badb66c12dfa31")
    }

    "커밋이 100개 이상인 풀 리퀘스트에서 마지막 커밋을 조회한다" {
        val actual = gitHubClient.getLastCommit(
            "https://github.com/woowacourse/nextstep_test/pull/697",
            now
        )
        actual shouldBe createCommit("8c4a97b43cf4db7f3d3f6ec53de0751eb20bdae0")
    }

    "저장소의 마지막 커밋을 조회한다" {
        val actual = gitHubClient.getLastCommit(
            PRIVATE_REPOSITORY, "https://github.com/woowacourse/java-chicken-2019", now
        )
        actual shouldBe createCommit("e7d2311185e7a5f8dbee4e14231d27ece16ae343")
    }

    "커밋이 100개 이상인 저장소에서 마지막 커밋을 조회한다" {
        val actual = gitHubClient.getLastCommit(
            PRIVATE_REPOSITORY, "https://github.com/woowahan-pjs/nextstep_test", now
        )
        actual shouldBe createCommit("8c4a97b43cf4db7f3d3f6ec53de0751eb20bdae0")
    }

    "토큰이 유효하지 않으면 예외가 발생한다" {
        val client = GitHubClient(properties.copy(accessKey = "invalid_token"), builder)
        shouldThrow<IllegalStateException> {
            client.getLastCommit(PRIVATE_REPOSITORY, "https://github.com/woowacourse/java-chicken-2019", now)
        }
    }

    "리소스가 없으면 예외가 발생한다" {
        shouldThrow<IllegalArgumentException> {
            gitHubClient.getLastCommit("https://github.com/woowacourse/service-apply/pull/1", now)
        }
    }

    "해당 커밋이 없으면 예외가 발생한다" {
        shouldThrow<IllegalArgumentException> {
            gitHubClient.getLastCommit(PUBLIC_PULL_REQUEST_URL, createLocalDateTime(2018))
        }
    }

    "비공개 저장소의 마지막 커밋을 조회한다".config(enabled = false) {
        val actual = gitHubClient.getLastCommit(PRIVATE_REPOSITORY, "https://github.com/applicant01/all-fail", now)
        actual shouldBe createCommit("936a0afb8da904ed9dfdea405042860395600047")
    }
})
