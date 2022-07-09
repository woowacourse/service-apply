package apply.domain.mission

import apply.createMission
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import support.test.RepositoryTest

@RepositoryTest
class MissionRepositoryTest(
    private val missionRepository: MissionRepository,
    private val entityManager: TestEntityManager
) : AnnotationSpec() {

    @Test
    fun `해당 평가에 이미 등록된 과제가 있는지 확인한다`() {
        val mission = createMission(evaluationId = 1L)
        missionRepository.save(mission)
        assertSoftly {
            missionRepository.existsByEvaluationId(1L).shouldBeTrue()
            missionRepository.existsByEvaluationId(2L).shouldBeFalse()
        }
    }

    @Test
    fun `삭제 시 논리적 삭제가 적용된다`() {
        val mission = missionRepository.save(createMission())
        flushAndClear()
        missionRepository.deleteById(mission.id)
        assertSoftly {
            missionRepository.findAll() shouldHaveSize 0
            missionRepository.findByIdOrNull(mission.id).shouldBeNull()
            missionRepository.existsById(mission.id).shouldBeFalse()
        }
    }

    @Test
    fun `해당 평가들에 해당하는 모든 과제를 찾는다`() {
        missionRepository.saveAll(
            listOf(
                createMission(evaluationId = 1L),
                createMission(evaluationId = 2L),
                createMission(evaluationId = 3L),
                createMission(evaluationId = 4L)
            )
        )
        missionRepository.findAllByEvaluationIdIn(listOf(1, 2, 3, 4)) shouldHaveSize 4
    }

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}
