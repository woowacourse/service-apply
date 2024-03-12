package apply.domain.evaluationtarget

import apply.createEvaluationTarget
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.shouldBe
import support.test.RepositoryTest
import support.test.spec.afterRootTest

@RepositoryTest
class EvaluationTargetRepositoryTest(
    private val evaluationTargetRepository: EvaluationTargetRepository
) : ExpectSpec({
    extensions(SpringExtension)

    context("평가 대상자 조회") {
        val evaluationId = 1L
        evaluationTargetRepository.saveAll(
            listOf(
                createEvaluationTarget(evaluationId = evaluationId, memberId = 1L),
                createEvaluationTarget(evaluationId = evaluationId, memberId = 2L)
            )
        )

        expect("특정 평가의 평가 대상자를 조회한다") {
            val actual = evaluationTargetRepository.findAllByEvaluationId(evaluationId)
            actual shouldHaveSize 2
        }
    }

    context("평가 상태가 다른 평가 대상자 조회") {
        val evaluationId = 1L
        evaluationTargetRepository.saveAll(
            listOf(
                createEvaluationTarget(evaluationId = evaluationId, memberId = 1L, evaluationStatus = PASS),
                createEvaluationTarget(evaluationId = evaluationId, memberId = 2L, evaluationStatus = PASS),
                createEvaluationTarget(evaluationId = evaluationId, memberId = 3L, evaluationStatus = FAIL)
            )
        )

        expect("특정 평가의 평가 상태가 일치하는 평가 대상자를 조회한다") {
            listOf(PASS to 2, FAIL to 1).forAll { (evaluationStatus, size) ->
                val actual = evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(
                    evaluationId, evaluationStatus
                )
                actual shouldHaveSize size
            }
        }
    }

    context("평가 대상자 삭제") {
        evaluationTargetRepository.saveAll(
            listOf(
                createEvaluationTarget(evaluationId = 1L, memberId = 1L),
                createEvaluationTarget(evaluationId = 1L, memberId = 2L),
                createEvaluationTarget(evaluationId = 2L, memberId = 1L)
            )
        )

        expect("평가 대상자에서 특정 사용자를 삭제한다") {
            evaluationTargetRepository.deleteByMemberIdIn(setOf(1L, 2L))
            evaluationTargetRepository.count().shouldBeZero()
        }

        expect("특정 평가의 평가 대상자에서 특정 사용자를 삭제한다") {
            evaluationTargetRepository.deleteByEvaluationIdAndMemberIdIn(1L, setOf(1L, 2L))
            evaluationTargetRepository.count() shouldBe 1
        }
    }

    afterRootTest {
        evaluationTargetRepository.deleteAll()
    }
})
