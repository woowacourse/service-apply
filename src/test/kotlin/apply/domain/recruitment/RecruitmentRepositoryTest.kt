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
        val recruitment = recruitmentRepository.save(createRecruitment(deleted = true))

        flushAndClear()
        assertAll(
            { assertThat(recruitmentRepository.findAll()).hasSize(0) },
            { assertThat(recruitmentRepository.findByIdOrNull(recruitment.id)).isNull() }
        )
    }

    @Test
    fun `삭제 시 논리적 삭제가 적용된다`() {
        val recruitment = recruitmentRepository.save(createRecruitment())
        flushAndClear()
        recruitmentRepository.deleteById(recruitment.id)
        assertAll(
            { assertThat(recruitmentRepository.findAll()).hasSize(0) },
            { assertThat(recruitmentRepository.findByIdOrNull(recruitment.id)).isNull() }
        )
    }

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}
