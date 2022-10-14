package apply.domain.evaluationtarget

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun EvaluationTargetRepository.getByEvaluationIdAndUserId(evaluationId: Long, userId: Long) =
    findByEvaluationIdAndUserId(evaluationId, userId)
        ?: throw NoSuchElementException("평가 대상자가 존재하지 않습니다. evaluationId: $evaluationId, userId: $userId")

fun EvaluationTargetRepository.getById(id: Long): EvaluationTarget = findByIdOrNull(id)
    ?: throw NoSuchElementException("평가 대상자가 존재하지 않습니다. id: $id")

interface EvaluationTargetRepository : JpaRepository<EvaluationTarget, Long> {
    fun findByEvaluationIdAndUserId(evaluationId: Long, userId: Long): EvaluationTarget?
    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationTarget>
    fun deleteByUserIdIn(userIds: Collection<Long>)
    fun deleteByEvaluationIdAndUserIdIn(evaluationId: Long, userIds: Collection<Long>)
    fun findAllByEvaluationIdAndUserIdIn(evaluationId: Long, userIds: Collection<Long>): List<EvaluationTarget>
    fun findAllByEvaluationIdAndEvaluationStatus(
        evaluationId: Long,
        evaluationStatus: EvaluationStatus
    ): List<EvaluationTarget>

    fun findAllByUserIdAndEvaluationIdIn(userId: Long, evaluationIds: Collection<Long>): List<EvaluationTarget>
}
