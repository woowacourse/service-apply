package apply.infra.github

import apply.PULL_REQUEST_URL
import apply.createCommit
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import support.createLocalDateTime
import support.test.IntegrationTest
import java.time.LocalDateTime.now

@IntegrationTest
class GitHubClientTest(
    private val gitHubClient: GitHubClient
) : StringSpec({
    val now = now()

    "마지막 커밋을 조회한다" {
        val actual = gitHubClient.getLastCommit(PULL_REQUEST_URL, now)
        actual shouldBe createCommit("eeb43de3f53f4bec08e7d63f07badb66c12dfa31")
    }

    "설정된 날짜와 시간을 기준으로 마지막 커밋을 조회한다" {
        val actual = gitHubClient.getLastCommit(PULL_REQUEST_URL, createLocalDateTime(2021, 10, 11))
        actual shouldBe createCommit("8c2d61313838d9220848bd38a5a5adc34efc5169")
    }

    "해당 커밋이 없으면 예외가 발생한다" {
        shouldThrow<RuntimeException> {
            gitHubClient.getLastCommit(PULL_REQUEST_URL, createLocalDateTime(2018))
        }
    }
})
