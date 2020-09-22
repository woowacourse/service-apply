package apply.domain.applicationForm

import com.helger.commons.annotation.UnsupportedOperation
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ApplicationFormTest {
    private lateinit var applicationForm : ApplicationForm

    @BeforeEach
    internal fun setUp() {
        applicationForm = ApplicationForm(1L, "http://example.com", ArrayList(), 1L)

    }
    @Test
    internal fun saveApplicationFormTest() {
        assertFalse(applicationForm.submitted)
        assertEquals(1L, applicationForm.applicantId)
        assertEquals("http://example.com", applicationForm.referenceUrl)
        assertThat(applicationForm.answers).hasSize(0)
        assertEquals(1L, applicationForm.id)
    }

    @Test
    internal fun updateApplicationFormTest() {
        applicationForm.update("http://h2f.kr", ArrayList())
        assertEquals("http://h2f.kr", applicationForm.referenceUrl)
    }

    @Test
    internal fun submitApplicationFormTest() {
        applicationForm.submit()

        assertTrue(applicationForm.submitted)

        assertThatThrownBy { applicationForm.update("http://h2f.kr", ArrayList()) }
                .isInstanceOf(IllegalAccessException::class.java)
                .hasMessageContaining("이미 제출된")
    }
}