package apply.application

import apply.EVALUATION_DESCRIPTION
import apply.EVALUATION_TARGET_NOTE
import apply.EVALUATION_TITLE1
import apply.createApplicationForm
import apply.createCheater
import apply.createEvaluation
import apply.createEvaluationAnswer
import apply.createEvaluationItem
import apply.createEvaluationTarget
import apply.createUser
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationitem.EvaluationItem
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import apply.domain.evaluationtarget.EvaluationStatus.WAITING
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getOrThrow
import apply.domain.user.User
import apply.domain.user.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import org.springframework.transaction.annotation.Transactional
import support.test.IntegrationTest
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@Transactional
@IntegrationTest
class EvaluationTargetIntegrationTest(
    private val evaluationTargetService: EvaluationTargetService,
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val userRepository: UserRepository,
    private val cheaterRepository: CheaterRepository
) : BehaviorSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    fun saveUser(email: String): User {
        return userRepository.save(createUser(email = email))
    }

    fun saveCheater(email: String): Cheater {
        return cheaterRepository.save(createCheater(email))
    }

    fun saveApplicationForm(
        userId: Long,
        recruitmentId: Long,
        submitted: Boolean,
        submittedDateTime: LocalDateTime
    ): ApplicationForm {
        return applicationFormRepository.save(
            createApplicationForm(userId, recruitmentId, submitted = submitted, submittedDateTime = submittedDateTime)
        )
    }

    fun saveEvaluation(
        recruitmentId: Long = 1L,
        beforeEvaluationId: Long = 0L,
        title: String = EVALUATION_TITLE1,
        description: String = EVALUATION_DESCRIPTION
    ): Evaluation {
        return evaluationRepository.save(createEvaluation(title, description, recruitmentId, beforeEvaluationId))
    }

    fun saveEvaluationItem(evaluationId: Long): EvaluationItem {
        return evaluationItemRepository.save(createEvaluationItem(evaluationId = evaluationId))
    }

    fun saveEvaluationTarget(
        evaluationId: Long,
        userId: Long,
        evaluationStatus: EvaluationStatus,
        note: String = EVALUATION_TARGET_NOTE,
        evaluationAnswers: List<EvaluationAnswer> = emptyList()
    ): EvaluationTarget {
        return evaluationTargetRepository.save(
            createEvaluationTarget(evaluationId, userId, evaluationStatus, note, EvaluationAnswers(evaluationAnswers))
        )
    }

    fun saveApplicant(recruitmentId: Long, email: String): User {
        val user = saveUser(email)
        saveApplicationForm(user.id, recruitmentId, true, now())
        return user
    }

    fun saveCheaterUser(email: String): User {
        val user = saveUser(email)
        saveCheater(user.email)
        return user
    }

    fun saveCheaterApplicant(recruitmentId: Long, email: String): User {
        val user = saveApplicant(recruitmentId, email)
        saveCheater(user.email)
        return user
    }

    Given("특정 모집에 지원한 지원자가 있고 이전 평가가 없는 특정 평가가 있는 경우") {
        val recruitmentId = 1L
        val user1 = saveApplicant(recruitmentId, "a@email.com")
        val user2 = saveApplicant(recruitmentId, "b@email.com")
        val user3 = saveApplicant(recruitmentId, "c@email.com")
        val evaluation = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = 0L)

        When("해당 평가의 평가 대상자를 불러오면") {
            evaluationTargetService.load(evaluation.id)

            Then("모든 지원자를 해당 평가의 평가 대상자로 지정한다") {
                val actual = evaluationTargetRepository.findAllByEvaluationId(evaluation.id)
                actual.summary shouldBe mapOf(
                    user1.id to WAITING,
                    user2.id to WAITING,
                    user3.id to WAITING
                )
            }
        }
    }

    Given("특정 모집에 지원한 부정행위자가 있고 이전 평가가 없는 특정 평가가 있는 경우") {
        val recruitmentId = 1L
        val user = saveCheaterApplicant(recruitmentId, "a@email.com")
        val evaluation = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = 0L)

        When("해당 평가의 평가 대상자를 불러오면") {
            evaluationTargetService.load(evaluation.id)

            Then("해당 지원자를 탈락한 평가 대상자로 지정한다") {
                val actual = evaluationTargetRepository.findAllByEvaluationId(evaluation.id)
                actual.summary shouldBe mapOf(user.id to FAIL)
            }
        }
    }

    Given("특정 모집에 신규 지원자가 있고 이전 평가가 없는 특정 평가 및 평가 대상자가 있는 경우") {
        val recruitmentId = 1L
        val user1 = saveApplicant(recruitmentId, "a@email.com")
        val evaluation = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = 0L)
        saveEvaluationTarget(evaluation.id, user1.id, PASS)

        val user2 = saveApplicant(recruitmentId, "b@email.com")

        When("해당 평가의 평가 대상자를 불러오면") {
            evaluationTargetService.load(evaluation.id)

            Then("해당 평가의 평가 대상자에 신규 지원자를 평가 대상자로 추가한다") {
                val actual = evaluationTargetRepository.findAllByEvaluationId(evaluation.id)
                actual.summary shouldBe mapOf(
                    user1.id to PASS,
                    user2.id to WAITING
                )
            }
        }
    }

    Given("평가 대상자가 있는 평가가 이전 평가인 특정 평가가 있는 경우") {
        val recruitmentId = 1L
        val user1 = saveUser("a@email.com")
        val user2 = saveUser("b@email.com")
        val user3 = saveUser("c@email.com")

        val evaluation1 = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = 0L)
        saveEvaluationTarget(evaluation1.id, user1.id, WAITING)
        saveEvaluationTarget(evaluation1.id, user2.id, PASS)
        saveEvaluationTarget(evaluation1.id, user3.id, PASS)

        val evaluation2 = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = evaluation1.id)

        When("해당 평가의 평가 대상자를 불러오면") {
            evaluationTargetService.load(evaluation2.id)

            Then("이전 평가에 합격한 평가 대상자를 해당 평가의 평가 대상자로 지정한다") {
                val actual = evaluationTargetRepository.findAllByEvaluationId(evaluation2.id)
                actual.summary shouldBe mapOf(
                    user2.id to WAITING,
                    user3.id to WAITING
                )
            }
        }
    }

    Given("평가 상태가 변경된 평가 대상자가 있는 평가가 이전 평가인 특정 평가 및 평가 대상자가 있는 경우") {
        val recruitmentId = 1L
        val user1 = saveUser("a@saveUser.com")
        val user2 = saveUser("b@saveUser.com")
        val user3 = saveUser("c@saveUser.com")
        val user4 = saveUser("d@saveUser.com")

        val evaluation1 = saveEvaluation(recruitmentId, 0L)
        saveEvaluationTarget(evaluation1.id, user1.id, PASS)
        saveEvaluationTarget(evaluation1.id, user2.id, PASS)
        saveEvaluationTarget(evaluation1.id, user3.id, FAIL)
        saveEvaluationTarget(evaluation1.id, user4.id, PASS)

        val evaluation2 = saveEvaluation(recruitmentId, evaluation1.id)
        saveEvaluationTarget(evaluation2.id, user1.id, FAIL)
        saveEvaluationTarget(evaluation2.id, user2.id, PASS)
        saveEvaluationTarget(evaluation2.id, user3.id, PASS)

        When("해당 평가의 평가 대상자를 불러오면") {
            evaluationTargetService.load(evaluation2.id)

            Then("이전 평가에서 합격한 평가 대상자를 해당 평가의 평가 대상자로 지정한다") {
                val actual = evaluationTargetRepository.findAllByEvaluationId(evaluation2.id)
                actual.summary shouldBe mapOf(
                    user1.id to FAIL,
                    user2.id to PASS,
                    user4.id to WAITING
                )
            }
        }
    }

    Given("특정 평가 대상자가 부정 행위자일 경우") {
        val recruitmentId = 1L
        val user = saveCheaterUser(email = "a@email.com")
        val evaluation1 = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = 0L)
        val evaluation2 = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = evaluation1.id)
        val evaluation3 = saveEvaluation(recruitmentId = recruitmentId, beforeEvaluationId = evaluation1.id)
        saveEvaluationTarget(evaluation1.id, user.id, PASS)
        saveEvaluationTarget(evaluation2.id, user.id, WAITING)
        saveEvaluationTarget(evaluation3.id, user.id, PASS)

        When("특정 평가의 평가 대상자를 불러오면") {
            evaluationTargetService.load(evaluation2.id)

            Then("해당 평가에만 탈락한 평가 대상자로 지정한다") {
                val actual = evaluationTargetRepository.findAll()
                actual.first(evaluation1.id, user.id).evaluationStatus shouldBe PASS
                actual.first(evaluation2.id, user.id).evaluationStatus shouldBe FAIL
                actual.first(evaluation3.id, user.id).evaluationStatus shouldBe PASS
            }
        }
    }

    Given("특정 평가 및 평가 항목과 특정 평가 대상자가 있는 경우") {
        val evaluation = saveEvaluation(title = EVALUATION_TITLE1, description = EVALUATION_DESCRIPTION)
        val evaluationItem = saveEvaluationItem(evaluation.id)
        val evaluationTarget = saveEvaluationTarget(
            evaluation.id, 1L, PASS, EVALUATION_TARGET_NOTE, listOf(createEvaluationAnswer(5, evaluationItem.id))
        )

        When("해당 평가 대상자의 평가 결과를 조회하면") {
            val actual = evaluationTargetService.getGradeEvaluation(evaluationTarget.id)

            Then("평가 정보 및 평가 결과를 확인할 수 있다") {
                actual shouldBe GradeEvaluationResponse(
                    EVALUATION_TITLE1,
                    EVALUATION_DESCRIPTION,
                    EvaluationTargetData(
                        listOf(EvaluationItemScoreData(5, evaluationItem.id)),
                        EVALUATION_TARGET_NOTE,
                        PASS
                    ),
                    listOf(EvaluationItemResponse(evaluationItem))
                )
            }
        }
    }

    Given("특정 평가 대상자가 있는 경우") {
        val evaluationTarget = saveEvaluationTarget(evaluationId = 1L, userId = 1L, WAITING)

        When("해당 평가 대상자를 채점하면") {
            evaluationTargetService.grade(
                evaluationTarget.id,
                EvaluationTargetData(listOf(EvaluationItemScoreData(5, 1L)), "특이 사항(수정)", PASS)
            )

            Then("평가 결과가 변경된다") {
                val actual = evaluationTargetRepository.getOrThrow(evaluationTarget.id)
                actual.evaluationStatus shouldBe PASS
                actual.evaluationAnswers.first(1L).score shouldBe 5
                actual.note shouldBe "특이 사항(수정)"
            }
        }
    }
})

private val List<EvaluationTarget>.summary: Map<Long, EvaluationStatus>
    get() = associate { it.userId to it.evaluationStatus }

private fun List<EvaluationTarget>.first(evaluationId: Long, userId: Long): EvaluationTarget {
    return first { it.evaluationId == evaluationId && it.userId == userId }
}

private fun EvaluationAnswers.first(evaluationItemId: Long): EvaluationAnswer {
    return answers.first { it.evaluationItemId == evaluationItemId }
}
