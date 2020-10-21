package apply.domain.applicationform

import apply.createApplicationForm
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import support.test.RepositoryTest

@RepositoryTest
class ApplicationFormRepositoryTest(
    private val applicationFormRepository: ApplicationFormRepository
) {
    @BeforeEach
    internal fun setUp() {
        val applicationForm = createApplicationForm()
        val submittedApplicationForm = createApplicationForm(recruitmentId = 2L).apply { submit() }

        applicationFormRepository.saveAll(listOf(applicationForm, submittedApplicationForm))
    }

    @Test
    fun `지원자가 지원한 모집의 지원서를 가져온다`() {
        val form = applicationFormRepository.findByRecruitmentIdAndApplicantId(1L, 1L)!!

        assertAll(
            { assertThat(form.referenceUrl).isEqualTo("http://example.com") },
            { assertThat(form.id).isEqualTo(1L) },
            { assertThat(form.answers.items[0].contents).isEqualTo("스타트업을 하고 싶습니다.") },
            { assertThat(form.answers.items[1].contents).isEqualTo("책임감") }
        )
    }

    @Test
    fun `지원자가 지원서를 제출한 이력이 있는지 확인한다`() {
        assertThat(applicationFormRepository.existsByApplicantIdAndSubmittedTrue(1L)).isTrue()
    }
}
