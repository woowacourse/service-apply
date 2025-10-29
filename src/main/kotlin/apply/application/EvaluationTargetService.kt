package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getOrThrow
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getOrThrow
import apply.domain.member.MemberRepository
import apply.domain.member.MemberStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class EvaluationTargetService(
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val memberRepository: MemberRepository,
    private val cheaterRepository: CheaterRepository,
) {
    fun findAllByEvaluationIdAndKeyword(
        evaluationId: Long,
        keyword: String = "",
    ): List<EvaluationTargetResponse> {
        val targetsByMemberId = evaluationTargetRepository
            .findAllByEvaluationId(evaluationId)
            .associateBy { it.memberId }
        return memberRepository
            .findAllByIdInAndKeyword(targetsByMemberId.keys, keyword)
            .map {
                val evaluationTarget = targetsByMemberId.getValue(it.id)
                when (it.status) {
                    MemberStatus.ACTIVE -> EvaluationTargetResponse(evaluationTarget, it)
                    else -> EvaluationTargetResponse(evaluationTarget, it.id)
                }
            }
    }

    /**
     * @see [평가_대상자_불러오기](https://github.com/woowacourse/service-apply/issues/301)
     */
    fun load(evaluationId: Long) {
        val evaluation = evaluationRepository.getOrThrow(evaluationId)
        val latestApplicants = findLatestApplicants(evaluation)
        val loadedApplicants = findLoadedApplicants(evaluationId)
        val cheaters = findCheatersIn(latestApplicants)

        val removedApplicants = loadedApplicants - latestApplicants
        evaluationTargetRepository.deleteByEvaluationIdAndMemberIdIn(evaluationId, removedApplicants)

        val loadedCheaters = loadedApplicants intersect cheaters
        updateFail(loadedCheaters, evaluation)

        val newApplicants = latestApplicants - (loadedApplicants + cheaters)
        save(newApplicants, evaluation, EvaluationStatus.WAITING)

        val newCheaters = cheaters - loadedCheaters
        save(newCheaters, evaluation, EvaluationStatus.FAIL)
    }

    private fun findLatestApplicants(evaluation: Evaluation): Set<Long> {
        return if (evaluation.hasBeforeEvaluation()) {
            findPassedMemberIdsFromBeforeEvaluation(evaluation)
        } else {
            findMemberIdsFromRecruitment(evaluation)
        }
    }

    private fun findPassedMemberIdsFromBeforeEvaluation(evaluation: Evaluation): Set<Long> {
        return evaluationTargetRepository.findAllByEvaluationId(evaluation.beforeEvaluationId)
            .filter { it.isPassed }
            .map { it.memberId }
            .toSet()
    }

    private fun findLoadedApplicants(evaluationId: Long): Set<Long> {
        return evaluationTargetRepository.findAllByEvaluationId(evaluationId)
            .map { it.memberId }
            .toSet()
    }

    private fun findMemberIdsFromRecruitment(evaluation: Evaluation): Set<Long> {
        return applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(evaluation.recruitmentId)
            .map { it.memberId }
            .toSet()
    }

    private fun findCheatersIn(memberIds: Set<Long>): Set<Long> {
        return memberRepository.findAllByEmailIn(cheaterRepository.findAll().map { it.email })
            .filter { memberIds.contains(it.id) }
            .map { it.id }
            .toSet()
    }

    private fun save(memberIds: Set<Long>, evaluation: Evaluation, evaluationStatus: EvaluationStatus) {
        val evaluationTargets = memberRepository.findAllActiveByIdIn(memberIds)
            .map { EvaluationTarget(evaluation.id, memberId = it.id, evaluationStatus = evaluationStatus) }
        evaluationTargetRepository.saveAll(evaluationTargets)
    }

    private fun updateFail(memberIds: Set<Long>, evaluation: Evaluation) {
        evaluationTargetRepository
            .findAllByEvaluationIdAndMemberIdIn(evaluation.id, memberIds)
            .forEach { it.evaluationStatus = EvaluationStatus.FAIL }
    }

    fun getGradeEvaluation(targetId: Long): GradeEvaluationResponse {
        val evaluationTarget = evaluationTargetRepository.getOrThrow(targetId)
        val evaluation = evaluationRepository.getOrThrow(evaluationTarget.evaluationId)
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
        val evaluationTarget = evaluationTargetRepository.getOrThrow(evaluationTargetId)
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
