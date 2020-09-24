package apply.domain.applicationform

import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ApplicationFormTest {
    private lateinit var applicationForm: ApplicationForm

    @BeforeEach
    internal fun setUp() {
        applicationForm = ApplicationForm(
            1L,
            1L,
            "http://example.com",
            Answers(
                mutableListOf(
                    Answer("스타트업을 하고 싶습니다.", 1L),
                    Answer("책임감", 2L)
                )
            )
        )
    }

    @Test
    internal fun saveApplicationFormTest() {
        assertFalse(applicationForm.submitted)
        assertEquals(1L, applicationForm.applicantId)
        assertEquals("http://example.com", applicationForm.referenceUrl)
        assertEquals("스타트업을 하고 싶습니다.", applicationForm.answers.items[0].contents)
        assertEquals("책임감", applicationForm.answers.items[1].contents)
    }

    @Test
    internal fun updateApplicationFormTest() {
        applicationForm.update(
            "http://h2f.kr", Answers(
                mutableListOf(
                    Answer("대기업에 취직하고 싶습니다.", 1L),
                    Answer("책임감", 2L)
                )
            )
        )
        assertEquals("http://h2f.kr", applicationForm.referenceUrl)
        assertEquals("대기업에 취직하고 싶습니다.", applicationForm.answers.items[0].contents)
    }

    @Test
    internal fun submitApplicationFormTest() {
        applicationForm.submit()

        assertTrue(applicationForm.submitted)

        assertThatThrownBy {
            applicationForm.update(
                "http://h2f.kr", Answers(
                    mutableListOf(
                        Answer("스타트업을 하고 싶습니다.", 1L),
                        Answer("책임감", 2L)
                    )
                )
            )
        }
            .isInstanceOf(IllegalAccessException::class.java)
            .hasMessageContaining("이미 제출된")
    }
}