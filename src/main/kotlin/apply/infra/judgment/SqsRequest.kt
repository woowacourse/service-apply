package apply.infra.judgment

import apply.application.JudgmentRequest

data class SqsRequest(
    val judgmentId: Long,
    val judgmentType: String,
    val programmingLanguage: String,
    val testName: String,
    val submissionMethod: String,
    val url: String,
    val commitHash: String
) {
    constructor(request: JudgmentRequest) : this(
        judgmentId = request.judgmentId,
        judgmentType = request.judgmentType.name,
        programmingLanguage = request.programmingLanguage.name,
        testName = request.testName,
        submissionMethod = request.submissionMethod.name,
        url = request.url,
        commitHash = request.commit.hash
    )
}
