package apply.application

import apply.createMission
import apply.createMissionData
import apply.domain.evaluation.EvaluationRepository
import apply.domain.mission.MissionRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest

@UnitTest
class MissionServiceTest {
    @MockK
    lateinit var missionRepository: MissionRepository

    @MockK
    lateinit var evaluationRepository: EvaluationRepository

    private lateinit var missionService: MissionService

    @BeforeEach
    internal fun setUp() {
        missionService = MissionService(missionRepository, evaluationRepository)
    }

    @Test
    fun `과제를 추가한다`() {
        val missionData = createMissionData()
        every { evaluationRepository.existsById(any()) } returns true
        every { missionRepository.existsByEvaluationId(any()) } returns false
        every { missionRepository.save(any()) } returns createMission()
        assertDoesNotThrow { missionService.save(missionData) }
    }

    @Test
    fun `존재하지 않는 평가 id인 경우 예외를 던진다`() {
        val missionData = createMissionData()
        every { evaluationRepository.existsById(any()) } returns false
        assertThrows<IllegalStateException> { missionService.save(missionData) }
    }

    @Test
    fun `해당 평가에 이미 등록된 과제가 있는 경우 예외를 던진다`() {
        val missionData = createMissionData()
        every { evaluationRepository.existsById(any()) } returns true
        every { missionRepository.existsByEvaluationId(any()) } returns true
        assertThrows<IllegalStateException> { missionService.save(missionData) }
    }
}
