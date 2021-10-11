package apply.application

import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.domain.evaluation.EvaluationRepository
import apply.domain.mission.MissionRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import support.test.UnitTest
import java.time.LocalDateTime
import java.util.Optional

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

    @Test
    fun `특정 모집의 모든 과제를 찾는다`() {
        val recruitmentId = 1L
        val firstEvaluation = createEvaluation(id = 1L, title = "평가1", recruitmentId = recruitmentId)
        val secondEvaluation = createEvaluation(id = 2L, title = "평가2", recruitmentId = recruitmentId)
        every { evaluationRepository.findAllByRecruitmentId(any()) } returns listOf(firstEvaluation, secondEvaluation)

        val firstMission = createMission(title = "과제1", evaluationId = 1L)
        val secondMission = createMission(title = "과제1", evaluationId = 2L)
        every { missionRepository.findAllByEvaluationIdIn(any()) } returns listOf(firstMission, secondMission)

        val actual = missionService.findAllByRecruitmentId(recruitmentId)
        assertAll(
            { assertThat(actual).hasSize(2) },
            {
                assertThat(actual).containsAnyOf(
                    MissionResponse(firstMission, firstEvaluation),
                    MissionResponse(secondMission, secondEvaluation)
                )
            }
        )
    }

    @Test
    fun `과제를 삭제한다`() {
        val mission = createMission(submittable = false)
        every { missionRepository.findByIdOrNull(mission.id) } returns mission
        every { missionRepository.deleteById(any()) } just Runs

        assertDoesNotThrow { missionService.deleteById(mission.id) }
    }

    @Test
    fun `제출 가능한 상태의 과제를 삭제하면 예외가 발생한다`() {
        val mission = createMission(submittable = true)
        every { missionRepository.findByIdOrNull(mission.id) } returns mission
        every { missionRepository.deleteById(any()) } just Runs

        assertThrows<IllegalStateException> { missionService.deleteById(mission.id) }
    }

    @Test
    fun `존재하지 않는 과제를 삭제하면 예외가 발생한다`() {
        val missionId = 1L
        every { missionRepository.findByIdOrNull(missionId) } returns null
        every { missionRepository.deleteById(any()) } just Runs

        assertThrows<NoSuchElementException> { missionService.deleteById(missionId) }
    }

    @Test
    fun `과제를 수정한다`() {
        val missionData = createMissionData(
            title = "changedTitle",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now(),
            submittable = false
        )
        every { missionRepository.findById(any()) } returns Optional.of(createMission())
        assertDoesNotThrow { missionService.update(missionData) }
    }
}
