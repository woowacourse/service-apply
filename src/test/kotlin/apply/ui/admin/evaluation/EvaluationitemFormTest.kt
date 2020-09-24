package apply.ui.admin.evaluation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

private fun createEvaluationItemForm(
    title: String = "README.md 파일에 기능 목록이 추가되어 있는가?",
    maximumScore: Int = 2,
    position: Int = 1,
    description: String = "[리뷰 절차]\n" +
        "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse"
): EvaluationItemForm {
    return EvaluationItemForm(title, maximumScore, position, description)
}

internal class EvaluationItemFormTest {
    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationItemForm().bindOrNull()
        assertThat(actual).isNotNull
        assertAll(
            { assertThat(actual!!.title).isEqualTo("README.md 파일에 기능 목록이 추가되어 있는가?") },
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
