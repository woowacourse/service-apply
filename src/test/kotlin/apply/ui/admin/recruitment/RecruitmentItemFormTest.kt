package apply.ui.admin.recruitment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RecruitmentItemFormTest {
    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createRecruitmentItemForm().bindOrNull()
        assertThat(actual).isEqualTo(createRecruitmentItemData())
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createRecruitmentItemForm(title = "").bindOrNull()
        assertThat(actual).isNull()
    }

    @Test
    fun `양식에 값을 채울 수 있다`() {
        val data = createRecruitmentItemData(id = 1L)
        val actual = createRecruitmentItemForm().run {
            fill(data)
            bindOrNull()
        }
        assertThat(actual).isEqualTo(createRecruitmentItemData(id = 1L))
    }
}
