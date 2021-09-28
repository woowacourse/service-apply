package apply.domain.mission

import apply.createMission
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import support.test.RepositoryTest

@RepositoryTest
class MissionRepositoryTest(
    private val missionRepository: MissionRepository
) {
    @BeforeEach
    internal fun setUp() {
        val mission = createMission(evaluationId = 1L)
        missionRepository.save(mission)
    }

    @Test
    fun `해당 평가에 이미 등록된 과제가 있는지 확인한다`() {
        assertAll(
            { assertThat(missionRepository.existsByEvaluationId(1L)).isTrue() },
            { assertThat(missionRepository.existsByEvaluationId(2L)).isFalse() }
        )
    }
}
