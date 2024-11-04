package apply.infra.github

import apply.domain.mission.SubmissionMethod.PRIVATE_REPOSITORY
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

private val today: LocalDateTime = LocalDateTime.now()
private val yesterday: LocalDateTime = today.minusDays(1L)

class GitHubTest : BehaviorSpec({
    val gitHubClient = mockk<GitHubClient>()

    val github = GitHub(gitHubClient)

    Given("비공개 저장소에 대한 접근 권한이 없고 리포지토리 초대가 있는 경우") {
        val owner = "woowahan-pjs"
        val repo = "nextstep_test"

        every { gitHubClient.getCommitsFromRepository(any(), any()) }
            .throws(IllegalArgumentException())
            .andThen(listOf(CommitResponse("hash", yesterday.atZone(ZoneId.systemDefault()))))
        every { gitHubClient.getInvitations(any(), any()) } returns listOf(
            createInvitationResponse(owner, repo, yesterday.atZone(ZoneId.systemDefault()))
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

    // 초대 목록에 일치하는 저장소 이름이 없는 경우
    // 수락했지만 접근이 불가능한 경우
    // 수락 전 접근이 가능한 경우
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
            fork = true
        ),
        createdAt,
        false
    )
}
