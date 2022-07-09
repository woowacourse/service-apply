package apply.ui.admin.selections

import apply.application.EvaluationItemScoreData
import apply.createEvaluationAnswerForm
import com.vaadin.flow.component.UI
import dev.mett.vaadin.tooltip.Tooltips
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic

class EvaluationItemScoreFormTest : AnnotationSpec() {
    @BeforeEach
    internal fun setUp() {
        mockkStatic("dev.mett.vaadin.tooltip.Tooltips")
        every { Tooltips.getCurrent() }.returns(Tooltips(UI()))
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationAnswerForm(score = 3, evaluationItemId = 1L).bindOrNull()
        actual.shouldNotBeNull()
        actual shouldBe EvaluationItemScoreData(score = 3, id = 1L)
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createEvaluationAnswerForm(score = -1).bindOrNull()
        actual.shouldBeNull()
    }
}
