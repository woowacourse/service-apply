package apply.application

import apply.createEvaluationItem
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.mission.MissionRepository
import apply.utils.CsvGenerator
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import support.test.UnitTest
import java.io.InputStream

@UnitTest
class EvaluationTargetCsvServiceTest : DescribeSpec({
    fun getInputStream(fileName: String): InputStream {
        return javaClass.classLoader.getResource(fileName)!!.openStream()
    }

    @MockK
    var evaluationTargetService: EvaluationTargetService = mockk()

    @MockK
    var evaluationItemRepository: EvaluationItemRepository = mockk()

    @MockK
    var missionRepository: MissionRepository = mockk()

    @MockK
    var assignmentRepository: AssignmentRepository = mockk()

    @MockK
    var csvGenerator: CsvGenerator = mockk()

    var evaluationTargetCsvService: EvaluationTargetCsvService = EvaluationTargetCsvService(
        evaluationTargetService,
        evaluationItemRepository,
        missionRepository,
        assignmentRepository,
        csvGenerator
    )

    beforeEach {
        evaluationTargetService = mockkClass(EvaluationTargetService::class)
        evaluationItemRepository = mockk()
        missionRepository = mockk()
        assignmentRepository = mockk()
        csvGenerator = mockk()
        evaluationTargetCsvService = EvaluationTargetCsvService(
            evaluationTargetService,
            evaluationItemRepository,
            missionRepository,
            assignmentRepository,
            csvGenerator
        )
    }

    describe("EvaluationTargetCsvService") {
        context("평가지 양식에 맞는 평가지 업로드 시") {
            it("csv 읽기에 성공한다") {
                val inputStream = getInputStream("evaluation.csv")
                val evaluationItems = listOf(
                    createEvaluationItem(maximumScore = 1, position = 1),
                    createEvaluationItem(maximumScore = 2, position = 2),
                    createEvaluationItem(maximumScore = 3, position = 3)
                )

                every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems
                every { evaluationTargetService.gradeAll(any(), any()) } just Runs

                shouldNotThrow<Exception> { evaluationTargetCsvService.updateTarget(inputStream, 1L) }
                verify(exactly = 1) { evaluationTargetService.gradeAll(any(), any()) }
            }
        }

        context("상태에 대해 대소문자 구분을 하지 않고 평가지 업로드 시") {
            it("csv 읽기에 성공한다") {
                val inputStream = getInputStream("status_ignore_case_evaluation.csv")
                val evaluationItems = listOf(
                    createEvaluationItem(maximumScore = 1, position = 1),
                    createEvaluationItem(maximumScore = 2, position = 2),
                    createEvaluationItem(maximumScore = 3, position = 3)
                )

                every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems
                every { evaluationTargetService.gradeAll(any(), any()) } just Runs

                shouldNotThrow<Exception> { evaluationTargetCsvService.updateTarget(inputStream, 1L) }
                verify(exactly = 1) { evaluationTargetService.gradeAll(any(), any()) }
            }
        }

        context("해당 평가에 맞지 않는 평가지 업로드 시") {
            it("예외가 발생한다") {
                val inputStream = getInputStream("another_evaluation.csv")
                val evaluationItems = listOf(
                    createEvaluationItem(position = 1),
                    createEvaluationItem(position = 2),
                    createEvaluationItem(position = 3)
                )

                every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems

                shouldThrowExactly<IllegalArgumentException> {
                    evaluationTargetCsvService.updateTarget(
                        inputStream,
                        1L
                    )
                }
            }
        }

        context("평가 항목의 최대 점수보다 높은 점수가 평가지에 포함될 시") {
            it("예외가 발생한다") {
                val inputStream = getInputStream("evaluation_with_over_maximum_score.csv")
                val evaluationItems = listOf(
                    createEvaluationItem(maximumScore = 1, position = 1),
                    createEvaluationItem(maximumScore = 2, position = 2),
                    createEvaluationItem(maximumScore = 3, position = 3)
                )

                every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems

                shouldThrowExactly<IllegalArgumentException> {
                    evaluationTargetCsvService.updateTarget(
                        inputStream,
                        1L
                    )
                }
            }
        }
    }
})
