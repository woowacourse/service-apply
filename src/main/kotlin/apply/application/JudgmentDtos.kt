package apply.application

import apply.domain.judgment.JudgmentType
import apply.domain.judgment.ProgrammingLanguage

data class JudgmentRequest(
    val judgmentId: Long,
    val testName: String,
    val language: ProgrammingLanguage,
    val testType: JudgmentType,
    val pullRequestUrl: String,
    val commitHash: String
)
