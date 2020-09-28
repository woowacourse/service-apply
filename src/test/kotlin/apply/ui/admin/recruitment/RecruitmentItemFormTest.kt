package apply.ui.admin.recruitment

import apply.application.RecruitmentItemRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

private const val TITLE: String = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
private const val POSITION: Int = 1
private const val MAXIMUM_LENGTH: Int = 1000
private const val DESCRIPTION: String =
    "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요."

private fun createRecruitmentItemForm(
    title: String = TITLE,
    position: Int = POSITION,
    maximumLength: Int = MAXIMUM_LENGTH,
    description: String = DESCRIPTION
): RecruitmentItemForm {
    return RecruitmentItemForm(title, position, maximumLength, description)
}

internal class RecruitmentItemFormTest {
    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createRecruitmentItemForm().bindOrNull()
        assertThat(actual).isNotNull()
        assertAll(
            { assertThat(actual!!.title).isEqualTo("프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?") },
            { assertThat(actual!!.position).isEqualTo(1) },
            { assertThat(actual!!.maximumLength).isEqualTo(1000) }
        )
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createRecruitmentItemForm(title = "").bindOrNull()
        assertThat(actual).isNull()
    }

    @Test
    fun `양식에 값을 채울 수 있다`() {
        val form = createRecruitmentItemForm()
        form.fillIn(RecruitmentItemRequest(TITLE, POSITION, MAXIMUM_LENGTH, DESCRIPTION))
        val actual = form.bindOrNull()
        assertThat(actual!!.title).isEqualTo(TITLE)
    }
}
