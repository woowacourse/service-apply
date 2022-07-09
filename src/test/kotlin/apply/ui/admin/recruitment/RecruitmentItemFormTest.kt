package apply.ui.admin.recruitment

import apply.createRecruitmentItemData
import apply.createRecruitmentItemForm
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

internal class RecruitmentItemFormTest : AnnotationSpec() {
    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createRecruitmentItemForm().bindOrNull()
        actual shouldBe createRecruitmentItemData()
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createRecruitmentItemForm(title = "").bindOrNull()
        actual.shouldBeNull()
    }

    @Test
    fun `양식에 값을 채울 수 있다`() {
        val data = createRecruitmentItemData(id = 1L)
        val actual = createRecruitmentItemForm().run {
            fill(data)
            bindOrNull()
        }
        actual shouldBe createRecruitmentItemData(id = 1L)
    }
}
