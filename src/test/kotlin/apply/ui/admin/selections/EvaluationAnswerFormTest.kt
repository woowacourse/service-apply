package apply.ui.admin.selections

import apply.application.EvaluationItemScoreData
import apply.createEvaluationAnswerForm
import com.vaadin.flow.component.UI
import dev.mett.vaadin.tooltip.Tooltips
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class EvaluationAnswerFormTest {
    @BeforeEach
    internal fun setUp() {
        mockkStatic("dev.mett.vaadin.tooltip.Tooltips")
        every { Tooltips.getCurrent() }.returns(Tooltips(UI()))
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationAnswerForm(score = 3, evaluationItemId = 1L).bindOrNull()
        assertThat(actual).isNotNull
        assertThat(actual).isEqualTo(EvaluationItemScoreData(score = 3, id = 1L))
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createEvaluationAnswerForm(score = -1).bindOrNull()
        assertThat(actual).isNull()
    }
}
