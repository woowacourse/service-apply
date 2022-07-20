package apply.domain.applicationform

import apply.createApplicationForm
import apply.pass
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertAll
import support.test.RepositoryTest

@RepositoryTest
internal class ApplicationFormRepositoryTest(
    private val applicationFormRepository: ApplicationFormRepository
) : DescribeSpec({
    describe("ApplicationFormRepositoryT") {
        context("지원자가 지원서를 제출하면") {
            val applicationForm = createApplicationForm()
            val submittedApplicationForm = createApplicationForm(recruitmentId = 2L).apply { submit(pass) }
            applicationFormRepository.saveAll(listOf(applicationForm, submittedApplicationForm))

            it("지원한 모집의 지원서를 가져올 수 있다") {
                val form = applicationFormRepository.findByRecruitmentIdAndUserId(1L, 1L)!!

                assertSoftly(form) {
                    referenceUrl.shouldBe("https://example.com")
                    id.shouldBe(1L)
                    answers.items[0].contents.shouldBe("스타트업을 하고 싶습니다.")
                    answers.items[1].contents.shouldBe("책임감")
                }
            }

            it("제출한 이력을 확인할 수 있다") {
                applicationFormRepository.existsByUserIdAndSubmittedTrue(1L).shouldBeTrue()
            }
        }
    }
})
