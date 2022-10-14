package apply.domain.judgment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun JudgmentRepository.getById(id: Long): Judgment = findByIdOrNull(id)
    ?: throw NoSuchElementException("자동 채점이 존재하지 않습니다. id: $id")

interface JudgmentRepository : JpaRepository<Judgment, Long> {
    fun findByAssignmentIdAndType(assignmentId: Long, type: JudgmentType): Judgment?
    fun findAllByAssignmentIdInAndType(assignmentIds: Collection<Long>, type: JudgmentType): List<Judgment>
}
