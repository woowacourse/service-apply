package apply.infra.github

import apply.domain.mission.SubmissionMethod.PRIVATE_REPOSITORY
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import support.test.spec.afterRootTest
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

private val today: LocalDateTime = LocalDateTime.now()
private val yesterday: ZonedDateTime = today.minusDays(1L).atZone(ZoneId.systemDefault())

class GitHubTest : BehaviorSpec({
    val gitHubClient = mockk<GitHubClient>(relaxed = true)

    val github = GitHub(gitHubClient)

    Given("비공개 저장소에 대한 접근 권한이 없고 저장소 초대가 있는 경우") {
        val owner = "woowahan-pjs"
        val repo = "nextstep_test"

        every { gitHubClient.getCommitsFromRepository(any(), any()) }
            .throws(IllegalArgumentException())
            .andThen(listOf(CommitResponse("hash", yesterday)))
        every { gitHubClient.getInvitations(any(), any()) } returns listOf(
            createInvitationResponse(owner, repo, yesterday),
        )
        every { gitHubClient.acceptInvitation(any()) } just Runs

        When("마지막 커밋을 조회하면") {
            github.getLastCommit(PRIVATE_REPOSITORY, "https://github.com/$owner/$repo", today)

            Then("해당 초대를 수락하고 커밋을 다시 조회한다") {
                verify(exactly = 1) { gitHubClient.acceptInvitation(any()) }
                verify(exactly = 2) { gitHubClient.getCommitsFromRepository(any(), any()) }
            }
        }
    }

    Given("초대 목록에 일치하는 저장소 이름이 없는 경우") {
        val owner = "woowahan-pjs"
        val repo = "nextstep_test"

        every { gitHubClient.getCommitsFromRepository(any(), any()) } throws IllegalArgumentException()
        every { gitHubClient.getInvitations(any(), any()) } returns listOf(
            createInvitationResponse("someone-else", "different-repo", yesterday),
        )

        When("마지막 커밋을 조회하면") {
            Then("조건을 충족하는 초대가 없어 예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    github.getLastCommit(PRIVATE_REPOSITORY, "https://github.com/$owner/$repo", today)
                }
            }
        }
    }

    Given("초대를 수락하였지만 여전히 접근할 수 없는 경우") {
        val owner = "woowahan-pjs"
        val repo = "nextstep_test"

        every { gitHubClient.getCommitsFromRepository(any(), any()) } throws IllegalArgumentException()
        every { gitHubClient.getInvitations(any(), any()) } returns listOf(
            createInvitationResponse(owner, repo, yesterday),
        )
        every { gitHubClient.acceptInvitation(any()) } just Runs

        When("마지막 커밋을 조회하면") {
            Then("수락하지만 결국 예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    github.getLastCommit(PRIVATE_REPOSITORY, "https://github.com/$owner/$repo", today)
                }
                verify(exactly = 1) { gitHubClient.acceptInvitation(any()) }
                verify(exactly = 2) { gitHubClient.getCommitsFromRepository(any(), any()) }
            }
        }
    }

    Given("초대를 수락하기 전에도 저장소에 접근할 수 있는 경우") {
        val owner = "woowahan-pjs"
        val repo = "nextstep_test"

        every { gitHubClient.getCommitsFromRepository(any(), any()) } returns listOf(
            CommitResponse("hash", yesterday),
        )

        When("마지막 커밋을 조회하면") {
            github.getLastCommit(PRIVATE_REPOSITORY, "https://github.com/$owner/$repo", today)

            Then("초대 목록을 조회하거나 수락하지 않는다") {
                verify(exactly = 0) { gitHubClient.getInvitations(any(), any()) }
                verify(exactly = 0) { gitHubClient.acceptInvitation(any()) }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})

private fun createInvitationResponse(
    owner: String,
    repo: String,
    createdAt: ZonedDateTime,
): InvitationResponse {
    return InvitationResponse(
        1L,
        RepositoryResponse(
            id = 1L,
            name = repo,
            fullName = "$owner/$repo",
            owner = OwnerResponse(owner),
            private = true,
            fork = true,
        ),
        createdAt,
        false,
    )
}
