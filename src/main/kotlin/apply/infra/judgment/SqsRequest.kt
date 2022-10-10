package apply.infra.judgment

import apply.application.JudgmentRequest

data class SqsRequest(
    val judgmentId: Long,
    val judgmentType: String,
    val programmingLanguage: String,
    val testName: String,
    val pullRequestUrl: String,
    val commitHash: String
) {
    constructor(request: JudgmentRequest) : this(
        request.judgmentId,
        request.judgmentType.name,
        request.programmingLanguage.name,
        request.testName,
        request.pullRequestUrl,
        request.commit.hash
    )
}
