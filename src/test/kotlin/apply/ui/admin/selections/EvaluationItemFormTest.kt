package apply.ui.admin.selections

import apply.application.GradeEvaluationItemData
import com.vaadin.flow.component.UI
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private fun createEvaluationItemForm(
    title: String = "평가항목 제목",
    description: String = "평가항목 설명",
    maximumScore: Int = 5,
    score: Int = 3
): GradeEvaluationItemForm {
    return GradeEvaluationItemForm(title = title, description = description, maximumScore = maximumScore, score = score)
}

@ExtendWith(MockKExtension::class)
class GradeEvaluationItemFormTest {

    @BeforeEach
    internal fun setUp() {
        mockkStatic("com.vaadin.flow.component.UI")
        every { UI.getCurrent() }.returns(UI())
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationItemForm().bindOrNull()
        assertThat(actual).isNotNull
        assertThat(actual).usingRecursiveComparison().ignoringFields("maximumScore")
            .isEqualTo(
                GradeEvaluationItemData(
                    score = 3,
                    id = 0L,
                    title = "평가항목 제목",
                    description = "평가항목 설명"
                )
            )
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createEvaluationItemForm(score = -1).bindOrNull()
        assertThat(actual).isNull()
    }
}
