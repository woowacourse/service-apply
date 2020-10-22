package apply.domain.evaluationtarget

import apply.application.EvaluationTargetResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EvaluationTargetRepository : JpaRepository<EvaluationTarget, Long> {
    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationTarget>

    fun existsByEvaluationId(evaluationId: Long): Boolean

    fun deleteByApplicantIdIn(applicantIds: Collection<Long>)

    fun deleteByEvaluationIdAndApplicantIdIn(evaluationId: Long, applicantIds: Collection<Long>)

    @Query(
        """
        select new apply.application.EvaluationTargetResponse(t, a)
        from Applicant a inner join EvaluationTarget t on a.id = t.applicantId
        where t.evaluationId = :evaluationId 
            and (a.information.name like %:keyword% or a.information.email like %:keyword%)
        """
    )
    fun findByEvaluationIdAndKeyword(
        evaluationId: Long,
        keyword: String,
        pageable: Pageable
    ): Page<EvaluationTargetResponse>

    @Query(
        """
        select count(a)
        from Applicant a inner join EvaluationTarget t on a.id = t.applicantId
        where t.evaluationId = :evaluationId 
            and (a.information.name like %:keyword% or a.information.email like %:keyword%)
        """
    )
    fun countByEvaluationIdAndKeyword(evaluationId: Long, keyword: String): Long
}
