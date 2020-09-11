package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.dto.EvaluationResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Transactional
@Service
class EvaluationService(
    private val evaluationRepository: EvaluationRepository,
    private val recruitmentService: RecruitmentService
) {
    private fun findAll(): List<Evaluation> {
        return evaluationRepository.findAll()
    }

    private fun findById(id: Long): Evaluation? {
        if (id == 0L) return null

        return evaluationRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("해당 id의 평가를 찾을 수 없습니다.")
    }

    fun findAllWithRecruitment(): List<EvaluationResponse> {
        return findAll().map {
            EvaluationResponse(
                it.id,
                it.title,
                it.description,
                recruitmentService.getById(it.recruitment).title,
                it.recruitment,
                findById(it.beforeEvaluation)?.title ?: "이전 평가 없음",
                it.beforeEvaluation
            )
        }.toList()
    }

    fun deleteById(id: Long) {
        evaluationRepository.deleteById(id)

        resetBeforeEvaluationContain(id)
    }

    private fun resetBeforeEvaluationContain(id: Long) {
        evaluationRepository.findAll()
            .firstOrNull { it.hasSameBeforeEvaluationWith(id) }?.resetBeforeEvaluation()
    }

    @PostConstruct
    private fun populateDummy() {
        if (evaluationRepository.count() != 0L) {
            return
        }
        val evaluations = listOf(
            Evaluation(
                "프리코스 대상자 선발",
                "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                1L
            ),
            Evaluation(
                " 1주차 - 숫자야구게임",
                "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                1L
            ),
            Evaluation(
                "2주차 - 자동차경주게임 ",
                "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                1L
            )
        )
        evaluationRepository.saveAll(evaluations)
        val secondEvaluation = findById(2L)
        secondEvaluation!!.beforeEvaluation = 1L

        val thirdEvaluation = findById(3L)
        thirdEvaluation!!.beforeEvaluation = 2L

        evaluationRepository.saveAll(listOf(secondEvaluation, thirdEvaluation))
    }
}
