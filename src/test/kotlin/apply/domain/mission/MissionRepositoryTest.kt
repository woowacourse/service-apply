package apply.domain.mission

import apply.createMission
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import support.test.RepositoryTest

@RepositoryTest
class MissionRepositoryTest(
    private val missionRepository: MissionRepository,
    private val entityManager: TestEntityManager
) {
    @Test
    fun `해당 평가에 이미 등록된 과제가 있는지 확인한다`() {
        val mission = createMission(evaluationId = 1L)
        missionRepository.save(mission)
        assertAll(
            { assertThat(missionRepository.existsByEvaluationId(1L)).isTrue() },
            { assertThat(missionRepository.existsByEvaluationId(2L)).isFalse() }
        )
    }

    @Test
    fun `삭제 시 논리적 삭제가 적용된다`() {
        val mission = missionRepository.save(createMission())
        flushAndClear()
        missionRepository.deleteById(mission.id)
        assertAll(
            { assertThat(missionRepository.findAll()).hasSize(0) },
            { assertThat(missionRepository.findByIdOrNull(mission.id)).isNull() },
            { assertThat(missionRepository.existsById(mission.id)).isFalse() }
        )
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
        assertThat(missionRepository.findAllByEvaluationIdIn(listOf(1, 2, 3, 4))).hasSize(4)
    }

    private fun flushAndClear() {
        entityManager.flush()
        entityManager.clear()
    }
}
