package apply.domain.recruitment

import apply.createRecruitment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
internal class RecruitmentTest(
    @Autowired
    val recruitmentRepository: RecruitmentRepository
) {
    @Test
    fun updateRecruitment() {
        val recruitment = createRecruitment(title = "4기 백엔드 모집", term = 4L, id = 1L)
        recruitmentRepository.save(recruitment)
        assertThat(recruitment.term).isEqualTo(4L)
    }
}
