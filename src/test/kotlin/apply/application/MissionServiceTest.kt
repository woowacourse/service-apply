package apply.application

import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
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

@UnitTest
class MissionServiceTest {
    @MockK
    lateinit var missionRepository: MissionRepository

    @MockK
    lateinit var evaluationRepository: EvaluationRepository

    @MockK
    lateinit var evaluationTargetRepository: EvaluationTargetRepository

    @MockK
    lateinit var assignmentRepository: AssignmentRepository

    private lateinit var missionService: MissionService

    @BeforeEach
    internal fun setUp() {
        missionService = MissionService(
            missionRepository, evaluationRepository, evaluationTargetRepository, assignmentRepository
        )
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
        assertThrows<IllegalArgumentException> { missionService.save(missionData) }
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
                    MissionAndEvaluationResponse(firstMission, firstEvaluation),
                    MissionAndEvaluationResponse(secondMission, secondEvaluation)
                )
            }
        )
    }

    @Test
    fun `특정 모집에 해당하는 나의 과제들을 조회한다`() {
        val recruitmentId = 1L
        val userId = 1L
        val missions = listOf(createMission(id = 1L), createMission(id = 2L))
        every { evaluationRepository.findAllByRecruitmentId(any()) } returns
            listOf(createEvaluation(id = 1L), createEvaluation(id = 2L))
        every { evaluationTargetRepository.existsByUserIdAndEvaluationId(any(), any()) } returns true
        every { missionRepository.findAllByEvaluationIdIn(any()) } returns missions
        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns true

        val responses = missionService.findAllByUserIdAndRecruitmentId(userId, recruitmentId)

        assertAll(
            { assertThat(responses).hasSize(2) },
            { assertThat(responses[0].title).isEqualTo(missions[0].title) },
            { assertThat(responses[0].description).isEqualTo(missions[0].description) },
            { assertThat(responses[0].submittable).isEqualTo(missions[0].submittable) },
            { assertThat(responses[0].submitted).isEqualTo(true) },
            { assertThat(responses[0].startDateTime).isEqualTo(missions[0].period.startDateTime) },
            { assertThat(responses[0].endDateTime).isEqualTo(missions[0].period.endDateTime) },
            { assertThat(responses[0].status).isEqualTo(missions[0].status) }
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
}
