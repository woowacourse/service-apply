package apply.ui.admin.recruitment

import com.vaadin.flow.component.UI
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import support.createLocalDateTime
import java.time.LocalDateTime

private fun createRecruitmentForm(
    title: String = "웹 백엔드 3기",
    startDateTime: LocalDateTime = createLocalDateTime(2020, 10, 25),
    endDateTime: LocalDateTime = createLocalDateTime(2020, 11, 5),
    canRecruit: Boolean = false,
    isHidden: Boolean = true
): RecruitmentForm {
    return RecruitmentForm(title, startDateTime, endDateTime, canRecruit, isHidden)
}

@ExtendWith(MockKExtension::class)
internal class RecruitmentFormTest {
    /**
     * Static classes are mocked for the following reasons:
     * [issue](https://github.com/vaadin/vaadin-date-picker-flow/issues/262)
     */
    @BeforeEach
    internal fun setUp() {
        mockkStatic("com.vaadin.flow.component.UI")
        every { UI.getCurrent() }.returns(UI())
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = createRecruitmentForm().bindOrNull()
        assertThat(actual).isNotNull()
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createRecruitmentForm(title = "").bindOrNull()
        assertThat(actual).isNull()
    }

    @ValueSource(booleans = [true, false])
    @ParameterizedTest
    fun `공개 여부 값이 설정되어 있는지 확인`(isHidden: Boolean) {
        val actual = createRecruitmentForm(isHidden = isHidden).bindOrNull()
        assertThat(actual).isNotNull()
        assertThat(actual!!.isHidden).isEqualTo(isHidden)
    }
}
