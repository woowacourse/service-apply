package apply.domain.evaluationtarget

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun EvaluationTargetRepository.getByEvaluationIdAndMemberId(evaluationId: Long, memberId: Long) =
    findByEvaluationIdAndMemberId(evaluationId, memberId)
        ?: throw NoSuchElementException("평가 대상자가 존재하지 않습니다. evaluationId: $evaluationId, memberId: $memberId")

fun EvaluationTargetRepository.getOrThrow(id: Long): EvaluationTarget = findByIdOrNull(id)
    ?: throw NoSuchElementException("평가 대상자가 존재하지 않습니다. id: $id")

interface EvaluationTargetRepository : JpaRepository<EvaluationTarget, Long> {
    fun findByEvaluationIdAndMemberId(evaluationId: Long, memberId: Long): EvaluationTarget?
    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationTarget>
    fun deleteByMemberIdIn(memberIds: Collection<Long>)
    fun deleteByEvaluationIdAndMemberIdIn(evaluationId: Long, memberIds: Collection<Long>)
    fun findAllByEvaluationIdAndMemberIdIn(evaluationId: Long, memberIds: Collection<Long>): List<EvaluationTarget>
    fun findAllByEvaluationIdAndEvaluationStatus(
        evaluationId: Long,
        evaluationStatus: EvaluationStatus
    ): List<EvaluationTarget>

    fun findAllByMemberIdAndEvaluationIdIn(memberId: Long, evaluationIds: Collection<Long>): List<EvaluationTarget>
}
