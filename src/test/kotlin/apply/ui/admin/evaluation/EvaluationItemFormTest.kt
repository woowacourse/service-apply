package apply.ui.admin.evaluation

import apply.EVALUATION_ITEM_POSITION
import apply.EVALUATION_ITEM_TITLE
import apply.createEvaluationItemData
import apply.createEvaluationItemForm
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class EvaluationItemFormTest : StringSpec({
    "유효한 값을 입력한 경우" {
        val actual = createEvaluationItemForm().bindOrNull()
        actual.shouldNotBeNull()
        assertSoftly(actual) {
            title shouldBe EVALUATION_ITEM_TITLE
            position shouldBe EVALUATION_ITEM_POSITION
        }
    }

    "잘못된 값을 입력한 경우" {
        val actual = createEvaluationItemForm(title = "").bindOrNull()
        actual.shouldBeNull()
    }

    "양식에 값을 채울 수 있다" {
        val data = createEvaluationItemData(id = 1L)
        val actual = createEvaluationItemForm().run {
            fill(data)
            bindOrNull()
        }
        actual shouldBe createEvaluationItemData(id = 1L)
    }
})
