package apply.ui.admin.recruitment

import apply.createRecruitmentData
import apply.createRecruitmentForm
import apply.createRecruitmentItemData
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
        assertThat(actual).isEqualTo(createRecruitmentData())
    }

    @Test
    fun `잘못된 값을 입력한 경우`() {
        val actual = createRecruitmentForm(title = "").bindOrNull()
        assertThat(actual).isNull()
    }

    @ValueSource(booleans = [true, false])
    @ParameterizedTest
    fun `공개 여부 값이 설정되어 있는지 확인`(hidden: Boolean) {
        val actual = createRecruitmentForm(hidden = hidden).bindOrNull()
        assertThat(actual).isNotNull()
        assertThat(actual!!.hidden).isEqualTo(hidden)
    }

    @Test
    fun `양식에 값을 채울 수 있다`() {
        val data = createRecruitmentData(id = 1L, recruitmentItems = listOf(createRecruitmentItemData(id = 1L)))
        val form = createRecruitmentForm()
        form.fill(data)
        assertThat(form.bindOrNull()).isEqualTo(
            createRecruitmentData(
                id = 1L,
                recruitmentItems = listOf(createRecruitmentItemData(id = 1L))
            )
        )
    }
}
