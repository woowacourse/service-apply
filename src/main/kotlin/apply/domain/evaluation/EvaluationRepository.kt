package apply.domain.evaluation

import org.springframework.data.jpa.repository.JpaRepository

interface EvaluationRepository : JpaRepository<Evaluation, Long> {
    fun findAllByRecruitmentId(recruitmentId: Long): List<Evaluation>
}
