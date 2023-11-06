package apply.infra.judgment

import apply.application.JudgmentRequest
import apply.domain.judgment.Commit
import apply.domain.judgment.JudgmentType.EXAMPLE
import apply.domain.judgmentitem.ProgrammingLanguage.JAVA
import apply.domain.mission.SubmissionMethod.PUBLIC_PULL_REQUEST
import io.kotest.core.spec.style.StringSpec
import support.test.IntegrationTest

@IntegrationTest
class SqsClientTest(
    private val sqsClient: SqsClient
) : StringSpec({
    "성공적인 전송".config(enabled = false) {
        sqsClient.requestJudge(
            JudgmentRequest(
                judgmentId = 1L,
                judgmentType = EXAMPLE,
                programmingLanguage = JAVA,
                testName = "onboarding",
                submissionMethod = PUBLIC_PULL_REQUEST,
                url = "https://github.com/woowacourse/java-onboarding-precourse-test/pull/3",
                commit = Commit("862a30c9bde7340c71c66ef8382973f4e23d796d")
            )
        )
    }
})
