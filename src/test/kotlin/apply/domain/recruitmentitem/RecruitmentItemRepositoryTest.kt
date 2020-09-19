package apply.domain.recruitmentitem

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class RecruitmentItemRepositoryTest(
    @Autowired
    private val recruitmentItemRepository: RecruitmentItemRepository
) {
    companion object {
        private const val RECRUITMENT_ID = 1L
        private const val DIFFERENT_RECRUITMENT_ID = 2L
    }

    @Test
    fun `특정 모집의 모집 항목을 순서대로 조회한다`() {
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
        val results = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(RECRUITMENT_ID)

        val expected = listOf(recruitmentItems[0], recruitmentItems[2], recruitmentItems[1])
        assertAll(
            { assertThat(results).usingElementComparatorIgnoringFields("id").isEqualTo(expected) },
            { assertThat(results).usingElementComparatorOnFields("id").isNotNull() }
        )
    }
}
