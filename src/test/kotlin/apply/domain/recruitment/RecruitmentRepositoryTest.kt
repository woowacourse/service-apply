package apply.domain.recruitment

import apply.createRecruitment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.findByIdOrNull
import support.test.RepositoryTest

@EnableJpaRepositories("apply.domain.recruitment")
@EntityScan("apply.domain.recruitment")
@RepositoryTest
class RecruitmentRepositoryTest(
    private val recruitmentRepository: RecruitmentRepository,
    private val entityManager: TestEntityManager
) {
    @Test
    fun `논리 삭제가 적용된 경우 조회하지 않는다`() {
        recruitmentRepository.save(createRecruitment(deleted = true, id = 1L))
        flushAndClear()
        assertAll(
            { assertThat(recruitmentRepository.findAll()).hasSize(0) },
            { assertThat(recruitmentRepository.findByIdOrNull(1L)).isNull() }
        )
    }

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}
