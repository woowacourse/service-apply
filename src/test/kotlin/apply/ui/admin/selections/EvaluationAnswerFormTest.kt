package apply.ui.admin.selections

import apply.application.EvaluationAnswerRequest
import apply.application.GradeEvaluationItemResponse
import com.vaadin.flow.component.UI
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private fun createEvaluationAnswerForm(
    title: String = "평가항목 제목",
    description: String = "평가항목 설명",
    maximumScore: Int = 5,
    id: Long = 1L,
    score: Int = 3
): EvaluationAnswerForm {
    return EvaluationAnswerForm(GradeEvaluationItemResponse(title, description, maximumScore, id, score)) {}
}

@ExtendWith(MockKExtension::class)
class EvaluationAnswerFormTest {

    @BeforeEach
    internal fun setUp() {
        mockkStatic("com.vaadin.flow.component.UI")
        every { UI.getCurrent() }.returns(UI())
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationAnswerForm().bindOrNull()
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(EvaluationAnswerRequest(3, 1L))
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createEvaluationAnswerForm(score = -1).bindOrNull()
        Assertions.assertThat(actual).isNull()
    }
}
