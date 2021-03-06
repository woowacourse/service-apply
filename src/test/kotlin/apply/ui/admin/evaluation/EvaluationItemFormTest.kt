package apply.ui.admin.evaluation

import apply.EVALUATION_ITEM_POSITION
import apply.EVALUATION_ITEM_TITLE
import apply.createEvaluationItemData
import apply.createEvaluationItemForm
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class EvaluationItemFormTest {
    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationItemForm().bindOrNull()
        assertThat(actual).isNotNull
        assertAll(
            { assertThat(actual!!.title).isEqualTo(EVALUATION_ITEM_TITLE) },
            { assertThat(actual!!.position).isEqualTo(EVALUATION_ITEM_POSITION) }
        )
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createEvaluationItemForm(title = "").bindOrNull()
        assertThat(actual).isNull()
    }

    @Test
    fun `양식에 값을 채울 수 있다`() {
        val data = createEvaluationItemData(id = 1L)
        val actual = createEvaluationItemForm().run {
            fill(data)
            bindOrNull()
        }
        assertThat(actual).isEqualTo(createEvaluationItemData(id = 1L))
    }
}
