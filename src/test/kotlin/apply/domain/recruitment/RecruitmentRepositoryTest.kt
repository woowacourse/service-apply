package apply.domain.recruitment

import apply.createRecruitment
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldBeEmpty
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import support.test.RepositoryTest

@RepositoryTest
class RecruitmentRepositoryTest(
    private val recruitmentRepository: RecruitmentRepository,
    private val entityManager: TestEntityManager
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("모집 삭제") {
        val recruitment = recruitmentRepository.save(createRecruitment())

        expect("삭제 시 논리적 삭제가 적용된다") {
            recruitmentRepository.deleteById(recruitment.id)
            val actual = recruitmentRepository.findAll()
            actual.shouldBeEmpty()
        }
    }
})
