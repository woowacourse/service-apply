package apply.application

import apply.createEvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.utils.CsvGenerator
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest
import java.io.InputStream

@UnitTest
class EvaluationTargetCsvServiceTest {
    @MockK
    private lateinit var evaluationTargetService: EvaluationTargetService

    @MockK
    private lateinit var assignmentService: AssignmentService

    @MockK
    private lateinit var evaluationItemRepository: EvaluationItemRepository

    @MockK
    private lateinit var csvGenerator: CsvGenerator

    private lateinit var evaluationTargetCsvService: EvaluationTargetCsvService

    @BeforeEach
    internal fun setUp() {
        evaluationTargetCsvService =
            EvaluationTargetCsvService(
                evaluationTargetService,
                assignmentService,
                evaluationItemRepository,
                csvGenerator
            )
    }

    @Test
    fun `평가지 양식에 맞는 평가지 업로드 시 csv 읽기에 성공한다`() {
        val inputStream = getInputStream("evaluation.csv")
        val evaluationItems = listOf(
            createEvaluationItem(maximumScore = 1, position = 1),
            createEvaluationItem(maximumScore = 2, position = 2),
            createEvaluationItem(maximumScore = 3, position = 3)
        )

        every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems
        every { evaluationTargetService.gradeAll(any(), any()) } just Runs

        assertDoesNotThrow { evaluationTargetCsvService.updateTarget(inputStream, 1L) }
        verify(exactly = 1) { evaluationTargetService.gradeAll(any(), any()) }
    }

    @Test
    fun `상태에 대해 대소문자 구분을 하지 않고 평가지 업로드 시 csv 읽기에 성공한다`() {
        val inputStream = getInputStream("status_ignore_case_evaluation.csv")
        val evaluationItems = listOf(
            createEvaluationItem(maximumScore = 1, position = 1),
            createEvaluationItem(maximumScore = 2, position = 2),
            createEvaluationItem(maximumScore = 3, position = 3)
        )

        every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems
        every { evaluationTargetService.gradeAll(any(), any()) } just Runs

        assertDoesNotThrow { evaluationTargetCsvService.updateTarget(inputStream, 1L) }
        verify(exactly = 1) { evaluationTargetService.gradeAll(any(), any()) }
    }

    @Test
    fun `해당 평가에 맞지 않는 평가지 업로드 시 예외가 발생한다`() {
        val inputStream = getInputStream("another_evaluation.csv")
        val evaluationItems = listOf(
            createEvaluationItem(position = 1),
            createEvaluationItem(position = 2),
            createEvaluationItem(position = 3)
        )

        every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems

        assertThrows<IllegalArgumentException> { evaluationTargetCsvService.updateTarget(inputStream, 1L) }
    }

    @Test
    fun `평가 항목의 최대 점수보다 높은 점수가 평가지에 포함될 시 예외가 발생한다`() {
        val inputStream = getInputStream("evaluation_with_over_maximum_score.csv")
        val evaluationItems = listOf(
            createEvaluationItem(maximumScore = 1, position = 1),
            createEvaluationItem(maximumScore = 2, position = 2),
            createEvaluationItem(maximumScore = 3, position = 3)
        )

        every { evaluationItemRepository.findByEvaluationIdOrderByPosition(any()) } returns evaluationItems

        val message =
            assertThrows<IllegalArgumentException> { evaluationTargetCsvService.updateTarget(inputStream, 1L) }.message
        assertThat(message).isEqualTo("평가 항목의 최대 점수보다 높은 점수입니다.")
    }

    private fun getInputStream(fileName: String): InputStream {
        return javaClass.classLoader.getResource(fileName)!!.openStream()
    }
}
