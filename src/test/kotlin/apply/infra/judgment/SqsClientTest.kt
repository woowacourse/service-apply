package apply.infra.judgment

import apply.application.JudgmentRequest
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.ProgrammingLanguage
import io.kotest.core.spec.style.StringSpec
import support.test.IntegrationTest

@IntegrationTest
class SqsClientTest(
    private val sqsClient: SqsClient
) : StringSpec({
    "성공적인 전송".config(enabled = false) {
        sqsClient.requestJudge(
            JudgmentRequest(
                1L,
                ProgrammingLanguage.JAVA,
                "baseball",
                JudgmentType.EXAMPLE,
                "",
                ""
            )
        )
    }
})

