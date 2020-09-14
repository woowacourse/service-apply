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
    fun `모집의 id로 모집 항목들을 Position의 오름차순으로 조회한다`() {
        val recruitmentItems = listOf(
            RecruitmentItem("모집 항목 제목", "모집 항목 설명", RECRUITMENT_ID, 0, 0),
            RecruitmentItem("모집 항목 제목2", "모집 항목 설명2", RECRUITMENT_ID, 0, 2),
            RecruitmentItem("모집 항목 제목3", "모집 항목 설명3", RECRUITMENT_ID, 0, 1),
            RecruitmentItem("모집 항목 제목4", "모집 항목 설명4", DIFFERENT_RECRUITMENT_ID, 0, 1)
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
