package apply.domain.recruitmentitem

import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentPeriod
import apply.domain.recruitment.RecruitmentRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import support.createLocalDateTime

@DataJpaTest
internal class RecruitmentItemRepositoryTest @Autowired constructor(
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository
) {
    @Test
    fun `모집의 id로 모집 항목들을 조회한다`() {
        val recruitmentPeriod = RecruitmentPeriod(
            createLocalDateTime(2019, 10, 25, 10),
            createLocalDateTime(2019, 11, 5, 10)
        )
        val recruitment = Recruitment("가짜 모집", true, recruitmentPeriod, 1L)
        recruitmentRepository.save(recruitment)

        val recruitmentItems = listOf(
            RecruitmentItem("모집 항목 제목", "모집 항목 설명", recruitment, 0),
            RecruitmentItem("모집 항목 제목2", "모집 항목 설명2", recruitment, 0),
            RecruitmentItem("모집 항목 제목3", "모집 항목 설명3", recruitment, 0)
        )

        recruitmentItemRepository.saveAll(recruitmentItems)
        val results = recruitmentItemRepository.findByRecruitmentIdOrderByOrder(1L)

        Assertions.assertThat(results)
            .usingElementComparatorIgnoringFields("id")
            .isEqualTo(recruitmentItems)
    }
}
