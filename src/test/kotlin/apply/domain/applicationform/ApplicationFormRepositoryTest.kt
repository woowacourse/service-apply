package apply.domain.applicationform

import apply.createApplicationForm
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.nulls.shouldNotBeNull
import support.test.RepositoryTest
import java.time.LocalDateTime.now

@RepositoryTest
class ApplicationFormRepositoryTest(
    private val applicationFormRepository: ApplicationFormRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("지원서 조회") {
        applicationFormRepository.saveAll(
            listOf(
                createApplicationForm(memberId = 1L, recruitmentId = 1L),
                createApplicationForm(memberId = 1L, recruitmentId = 2L, submitted = true, submittedDateTime = now())
            )
        )

        expect("지원자가 지원한 모집에 대한 지원서를 조회한다") {
            val actual = applicationFormRepository.findByRecruitmentIdAndMemberId(1L, 1L)
            actual.shouldNotBeNull()
        }
    }
})
