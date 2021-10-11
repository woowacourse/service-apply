package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getById
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.user.UserRepository
import apply.domain.user.findAllByEmailIn
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class EvaluationTargetService(
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val userRepository: UserRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun getById(id: Long): EvaluationTarget = evaluationTargetRepository.findByIdOrNull(id)
        ?: throw IllegalArgumentException("EvaluationTarget (id=$id) 가 존재하지 않습니다")

    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationTarget> =
        evaluationTargetRepository.findAllByEvaluationId(evaluationId)

    fun findAllByEvaluationIdAndKeyword(
        evaluationId: Long,
        keyword: String = ""
    ): List<EvaluationTargetResponse> {
        val evaluationTargets = findAllByEvaluationId(evaluationId)
        val users = userRepository.findAllByKeyword(keyword)

        return evaluationTargets
            .filter { users.any { user -> user.id == it.userId } }
            .map {
                val user = users.first { each -> each.id == it.userId }
                EvaluationTargetResponse(
                    it.id,
                    user.name,
                    user.email,
                    user.id,
                    it.evaluationAnswers.countTotalScore(),
                    it.evaluationStatus,
                    it.administratorId,
                    it.note,
                    it.evaluationAnswers
                )
            }
    }

    /**
     * @see [평가_대상자_불러오기](https://github.com/woowacourse/service-apply/issues/301)
     */
    fun load(evaluationId: Long) {
        val evaluation = evaluationRepository.getById(evaluationId)
        val latestApplicants = findLatestApplicants(evaluation)
        val loadedApplicants = findLoadedApplicants(evaluationId)
        val cheaters = findCheatersIn(latestApplicants)

        val removedApplicants = loadedApplicants - latestApplicants
        evaluationTargetRepository.deleteByEvaluationIdAndUserIdIn(evaluationId, removedApplicants)

        val loadedCheaters = loadedApplicants intersect cheaters
        updateFail(loadedCheaters, evaluation)

        val newApplicants = latestApplicants - (loadedApplicants + cheaters)
        save(newApplicants, evaluation, EvaluationStatus.WAITING)

        val newCheaters = cheaters - loadedCheaters
        save(newCheaters, evaluation, EvaluationStatus.FAIL)
    }

    private fun findLatestApplicants(evaluation: Evaluation): Set<Long> {
        return if (evaluation.hasBeforeEvaluation()) {
            findPassedUserIdsFromBeforeEvaluation(evaluation)
        } else {
            findUserIdsFromRecruitment(evaluation)
        }
    }

    private fun findPassedUserIdsFromBeforeEvaluation(evaluation: Evaluation): Set<Long> {
        return evaluationTargetRepository.findAllByEvaluationId(evaluation.beforeEvaluationId)
            .filter { it.isPassed }
            .map { it.userId }
            .toSet()
    }

    private fun findLoadedApplicants(evaluationId: Long): Set<Long> {
        return evaluationTargetRepository.findAllByEvaluationId(evaluationId)
            .map { it.userId }
            .toSet()
    }

    private fun findUserIdsFromRecruitment(evaluation: Evaluation): Set<Long> {
        return applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(evaluation.recruitmentId)
            .map { it.userId }
            .toSet()
    }

    private fun findCheatersIn(userIds: Set<Long>): Set<Long> {
        return userRepository.findAllByEmailIn(cheaterRepository.findAll().map { it.email })
            .filter { userIds.contains(it.id) }
            .map { it.id }
            .toSet()
    }

    private fun save(userIds: Set<Long>, evaluation: Evaluation, evaluationStatus: EvaluationStatus) {
        val evaluationTargets = userRepository.findAllById(userIds)
            .map { EvaluationTarget(evaluation.id, userId = it.id, evaluationStatus = evaluationStatus) }
        evaluationTargetRepository.saveAll(evaluationTargets)
    }

    private fun updateFail(userIds: Set<Long>, evaluation: Evaluation) {
        evaluationTargetRepository.findAllByEvaluationIdAndUserIdIn(evaluation.id, userIds).forEach {
            it.evaluationStatus = EvaluationStatus.FAIL
        }
    }

    fun getGradeEvaluation(targetId: Long): GradeEvaluationResponse {
        val evaluationTarget = getById(targetId)
        val evaluation = evaluationRepository.findByIdOrNull(evaluationTarget.evaluationId)
            ?: throw IllegalArgumentException("EvaluationTarget (id=$targetId)의 Evaluation (id=${evaluationTarget.evaluationId}가 존재하지 않습니다")
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluation.id)

        val evaluationItemScores = evaluationItems.map {
            EvaluationItemScoreData(
                id = it.id,
                score = evaluationTarget.evaluationAnswers.findScoreByEvaluationItemId(it.id)
            )
        }

        return GradeEvaluationResponse(
            title = evaluation.title,
            description = evaluation.description,
            evaluationTarget = EvaluationTargetData(
                note = evaluationTarget.note,
                evaluationStatus = evaluationTarget.evaluationStatus,
                evaluationItemScores = evaluationItemScores
            ),
            evaluationItems = evaluationItems.map { EvaluationItemResponse(it) }
        )
    }

    fun grade(evaluationTargetId: Long, request: EvaluationTargetData) {
        val evaluationTarget = getById(evaluationTargetId)

        val evaluationAnswers = request.evaluationItemScores
            .map { EvaluationAnswer(it.score, it.id) }
            .toMutableList()
        evaluationTarget.update(
            evaluationStatus = request.evaluationStatus,
            evaluationAnswers = EvaluationAnswers(evaluationAnswers),
            note = request.note
        )
    }

    fun gradeAll(evaluationId: Long, evaluationTargetsData: Map<Long, EvaluationTargetData>) {
        evaluationTargetRepository.findAllByEvaluationId(evaluationId)
        evaluationTargetsData
            .forEach { (evaluationTargetId, evaluationTargetData) -> grade(evaluationTargetId, evaluationTargetData) }
    }
}
