package apply.domain.recruitmentitem

import apply.createRecruitmentItem
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldBeSortedBy
import support.test.RepositoryTest

@RepositoryTest
class RecruitmentItemRepositoryTest(
    private val recruitmentItemRepository: RecruitmentItemRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("모집 항목 조회") {
        val recruitmentId = 1L
        recruitmentItemRepository.saveAll(
            listOf(
                createRecruitmentItem(recruitmentId = recruitmentId, position = 1),
                createRecruitmentItem(recruitmentId = recruitmentId, position = 3),
                createRecruitmentItem(recruitmentId = recruitmentId, position = 2)
            )
        )

        expect("특정 모집의 모집 항목을 순서대로 조회한다") {
            val actual = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId)
            actual.shouldBeSortedBy { it.position }
        }
    }
})
