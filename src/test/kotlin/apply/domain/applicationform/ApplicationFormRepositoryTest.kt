package apply.domain.applicationform

import apply.createApplicationForm
import apply.pass
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import support.test.RepositoryTest
import javax.transaction.Transactional

@RepositoryTest
class ApplicationFormRepositoryTest(
    private val applicationFormRepository: ApplicationFormRepository
) : AnnotationSpec() {
    @BeforeAll
    internal fun setUp() {
        val applicationForm = createApplicationForm()
        val submittedApplicationForm = createApplicationForm(recruitmentId = 2L).apply { submit(pass) }

        applicationFormRepository.saveAll(listOf(applicationForm, submittedApplicationForm))
    }

    @Test
    fun `지원자가 지원한 모집의 지원서를 가져온다`() {
        val form = applicationFormRepository.findByRecruitmentIdAndUserId(1L, 1L)!!

        assertSoftly {
            form.referenceUrl shouldBe "https://example.com"
            form.id shouldBe 1L
            form.answers.items[0].contents shouldBe "스타트업을 하고 싶습니다."
            form.answers.items[1].contents shouldBe "책임감"
        }
    }

    @Test
    fun `지원자가 지원서를 제출한 이력이 있는지 확인한다`() {
        applicationFormRepository.existsByUserIdAndSubmittedTrue(1L).shouldBeTrue()
    }
}
