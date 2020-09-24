package apply.ui.admin.evaluation

import apply.domain.evaluation.Evaluation
import apply.domain.recruitment.Recruitment
import com.vaadin.flow.component.UI
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import support.createLocalDateTime

private fun createRecruitments(): List<Recruitment> {
    return listOf(
        Recruitment(
            title = "웹 백엔드 2기",
            canRecruit = true,
            startDateTime = createLocalDateTime(2019, 10, 25, 10),
            endDateTime = createLocalDateTime(2019, 11, 5, 10)
        ),
        Recruitment(
            title = "웹 백엔드 3기",
            canRecruit = true,
            startDateTime = createLocalDateTime(2020, 10, 25, 15),
            endDateTime = createLocalDateTime(2020, 11, 5, 10)
        ),
        Recruitment(
            title = "웹 프론트엔드 3기",
            canRecruit = false,
            startDateTime = createLocalDateTime(2020, 10, 25, 15),
            endDateTime = createLocalDateTime(2020, 11, 5, 10)
        )
    )
}

private fun createEvaluations(): List<Evaluation> {
    return listOf(
        Evaluation(
            title = "프리코스 대상자 선발",
            description = "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            recruitmentId = 1L
        ),
        Evaluation(
            title = " 1주차 - 숫자야구게임",
            description = "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            recruitmentId = 1L,
            beforeEvaluationId = 1L
        ),
        Evaluation(
            title = "2주차 - 자동차경주게임 ",
            description = "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            recruitmentId = 1L,
            beforeEvaluationId = 2L
        )
    )
}

private fun createEvaluationForm(
    recruitments: List<Recruitment> = createRecruitments()
): EvaluationForm {
    return EvaluationForm(recruitments) {
        createEvaluations()
    }
}

@ExtendWith(MockKExtension::class)
internal class EvaluationFormTest {

    @BeforeEach
    internal fun setUp() {
        mockkStatic("com.vaadin.flow.component.UI")
        every { UI.getCurrent() }.returns(UI())
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createEvaluationForm().bindOrNull()
        assertThat(actual).isNotNull
    }
}
