package apply.ui.admin.recruitment

import apply.createRecruitmentItemData
import apply.createRecruitmentItemForm
import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat

class RecruitmentItemFormTest : StringSpec({
    "유효한 값을 입력하는 경우" {
        val actual = createRecruitmentItemForm().bindOrNull()
        assertThat(actual).isEqualTo(createRecruitmentItemData())
    }

    "잘못된 값을 입력한 경우" {
        val actual = createRecruitmentItemForm(title = "").bindOrNull()
        assertThat(actual).isNull()
    }

    "양식에 값을 채울 수 있다" {
        val data = createRecruitmentItemData(id = 1L)
        val actual = createRecruitmentItemForm().run {
            fill(data)
            bindOrNull()
        }
        assertThat(actual).isEqualTo(createRecruitmentItemData(id = 1L))
    }
})
