package apply.ui.admin.evaluation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

private fun createEvaluationItemForm(
    title: String = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?",
    maximumScore: Int = 2,
    position: Int = 1,
    description: String = "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요."
): EvaluationItemForm {
    return EvaluationItemForm(title, maximumScore, position, description)
}

internal class EvaluationItemFormTest {
    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationItemForm().bindOrNull()
        assertThat(actual).isNotNull
        assertAll(
            { assertThat(actual!!.title).isEqualTo("프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?") },
            { assertThat(actual!!.position).isEqualTo(1) },
            { assertThat(actual!!.maximumScore).isEqualTo(2) }
        )
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createEvaluationItemForm(title = "").bindOrNull()
        assertThat(actual).isNull()
    }
}
