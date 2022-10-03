package apply.domain.judgment.tobe

import java.time.LocalDateTime

fun createJudgment(
    assignmentId: Long = 1L,
    type: JudgmentType = JudgmentType.EXAMPLE,
    records: List<JudgmentRecord> = emptyList(),
    id: Long = 0L
): Judgment {
    return Judgment(assignmentId, type, records, id)
}

fun createJudgmentRecord(
    commit: Commit = createCommit(),
    result: JudgmentResult = JudgmentResult(),
    startedDateTime: LocalDateTime = LocalDateTime.now(),
    completedDateTime: LocalDateTime? = null
): JudgmentRecord {
    return JudgmentRecord(commit, result, startedDateTime, completedDateTime)
}

fun createCommit(
    hash: String = "589a9e2e2819399962312e412298b1b3923adaa9"
): Commit {
    return Commit(hash)
}
