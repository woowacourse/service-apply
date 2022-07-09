package apply.domain.recruitment

import apply.createRecruitment
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
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
) : AnnotationSpec() {
    @Test
    fun `삭제 시 논리적 삭제가 적용된다`() {
        val recruitment = recruitmentRepository.save(createRecruitment())
        flushAndClear()
        recruitmentRepository.deleteById(recruitment.id)
        assertSoftly {
            recruitmentRepository.findAll().shouldHaveSize(0)
            recruitmentRepository.findByIdOrNull(recruitment.id).shouldBeNull()
        }
    }

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}
