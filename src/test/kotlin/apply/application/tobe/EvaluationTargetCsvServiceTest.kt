package apply.application.tobe

import apply.application.EvaluationTargetCsvService
import apply.application.EvaluationTargetService
import apply.createEvaluationItem
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.mission.MissionRepository
import apply.utils.CsvGenerator
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

class EvaluationTargetCsvServiceTest : BehaviorSpec({
    val evaluationTargetService = mockk<EvaluationTargetService>()
    val evaluationItemRepository = mockk<EvaluationItemRepository>()
    val missionRepository = mockk<MissionRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val csvGenerator = mockk<CsvGenerator>()

    val evaluationTargetCsvService = EvaluationTargetCsvService(
        evaluationTargetService,
        evaluationItemRepository,
        missionRepository,
        assignmentRepository,
        csvGenerator
    )

    Given("특정 평가와 해당 평가에 대한 평가 항목이 여러 개 있는 경우") {
        val evaluationId = 1L
        val evaluationItems = listOf(
            createEvaluationItem("평가 항목1", evaluationId = evaluationId, maximumScore = 1, position = 1, id = 1L),
            createEvaluationItem("평가 항목2", evaluationId = evaluationId, maximumScore = 3, position = 2, id = 2L),
            createEvaluationItem("평가 항목3", evaluationId = evaluationId, maximumScore = 5, position = 3, id = 3L)
        )

        every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems
        every { evaluationTargetService.gradeAll(any(), any()) } just Runs

        When("평가 항목명과 최대 점수가 모두 일치하는 머리글을 가진 평가지로 평가를 수정하면") {
            val file = """
                id,이름,이메일,평가 상태,평가 항목1(1),평가 항목2(3),평가 항목3(5),기타 특이사항
                1,홍길동,a@email.com,PASS,1,1,1,
                2,홍길동2,b@email.com,PASS,1,1,1,
                3,홍길동3,c@email.com,PASS,1,1,1,
            """.trimIndent()

            Then("특정 평가의 평가 대상자에 대한 평가가 수정되고 결과가 저장된다") {
                shouldNotThrowAny {
                    evaluationTargetCsvService.updateTarget(file.byteInputStream(), evaluationId)
                }
                verify(exactly = 1) { evaluationTargetService.gradeAll(any(), any()) }
            }
        }

        When("평가 항목과 머리글이 모두 일치하지만 평가 상태의 대소문자를 구분하지 않는 평가지로 평가를 수정하면") {
            val file = """
                id,이름,이메일,평가 상태,평가 항목1(1),평가 항목2(3),평가 항목3(5),기타 특이사항
                1,홍길동,a@email.com,pass,1,1,1,
                2,홍길동2,b@email.com,PENDING,1,1,1,
                3,홍길동3,c@email.com,PAss,1,1,1,
            """.trimIndent()

            Then("특정 평가의 평가 대상자에 대한 평가가 수정되고 결과가 저장된다") {
                shouldNotThrowAny {
                    evaluationTargetCsvService.updateTarget(file.byteInputStream(), evaluationId)
                }
                verify(exactly = 1) { evaluationTargetService.gradeAll(any(), any()) }
            }
        }

        When("평가 항목과 일치하지 않는 머리글이 있는 평가지로 평가를 수정하면") {
            val file = """
                id,이름,이메일,평가 상태,다른 평가 항목1(1),다른 평가 항목2(3),다른 평가 항목3(5),기타 특이사항
                1,제이슨,a@email.com,PASS,1,1,1,
                2,로키,b@email.com,PASS,1,1,1,
                3,마갸,c@email.com,PASS,1,1,1,
                4,배럴,d@email.com,PASS,1,1,1,
                5,아론,e@email.com,PASS,1,1,1,
                6,아마찌,f@email.com,PASS,1,1,1,
                7,코기,g@email.com,PASS,1,1,1,
            """.trimIndent()

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    evaluationTargetCsvService.updateTarget(file.byteInputStream(), evaluationId)
                }
            }
        }

        When("평가 항목의 최대 점수보다 높은 점수가 포함된 평가지로 평가를 수정하면") {
            val file = """
                id,이름,이메일,평가 상태,평가 항목1(1),평가 항목2(3),평가 항목3(5),기타 특이사항
                1,제이슨,a@email.com,PASS,7,7,7,
                2,로키,b@email.com,PASS,7,7,7,
                3,마갸,c@email.com,PASS,7,7,7,
                4,배럴,d@email.com,PASS,7,7,7,
                5,아론,e@email.com,PASS,7,7,7,
                6,아마찌,f@email.com,PASS,7,7,7,
                7,코기,g@email.com,PASS,7,7,7,
            """.trimIndent()

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    evaluationTargetCsvService.updateTarget(file.byteInputStream(), evaluationId)
                }
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})
