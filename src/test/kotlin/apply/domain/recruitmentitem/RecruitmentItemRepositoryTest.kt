package apply.domain.recruitmentitem

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.nulls.shouldNotBeNull
import support.test.RepositoryTest

@RepositoryTest
internal class RecruitmentItemRepositoryTest(
    private val recruitmentItemRepository: RecruitmentItemRepository
) : DescribeSpec({
    describe("RecruitmentItemRepository") {
        context("특정 모집의 모집 항목을") {
            val recruitmentItems = listOf(
                RecruitmentItem(
                    RECRUITMENT_ID,
                    "모집 항목 제목",
                    position = 1,
                    maximumLength = 1,
                    description = "모집 항목 설명"
                ),
                RecruitmentItem(
                    RECRUITMENT_ID,
                    "모집 항목 제목2",
                    position = 3,
                    maximumLength = 1,
                    description = "모집 항목 설명2"
                ),
                RecruitmentItem(
                    RECRUITMENT_ID,
                    "모집 항목 제목3",
                    position = 2,
                    maximumLength = 1,
                    description = "모집 항목 설명3"
                ),
                RecruitmentItem(
                    DIFFERENT_RECRUITMENT_ID,
                    "모집 항목 제목4",
                    position = 2,
                    maximumLength = 1,
                    description = "모집 항목 설명4"
                )
            )
            recruitmentItemRepository.saveAll(recruitmentItems)

            it("특정 모집의 모집 항목을 순서대로 조회한다") {
                val results = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(RECRUITMENT_ID)
                val expected = listOf(recruitmentItems[0], recruitmentItems[2], recruitmentItems[1])
                val zip = results.zip(expected)

                assertSoftly(zip) {
                    zip.forEach { recruitmentItems ->
                        recruitmentItems.first.shouldBeEqualToIgnoringFields(
                            recruitmentItems.second,
                            RecruitmentItem::recruitmentId
                        )
                    }
                    zip.forEach { recruitmentItems -> recruitmentItems.first.recruitmentId.shouldNotBeNull() }
                }
            }
        }
    }
}) {
    companion object {
        private const val RECRUITMENT_ID = 1L
        private const val DIFFERENT_RECRUITMENT_ID = 2L
    }
}
