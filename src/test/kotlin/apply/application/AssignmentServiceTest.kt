package apply.application

import apply.createAssignment
import apply.createAssignmentRequest
import apply.createEvaluationTarget
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest
import java.time.LocalDateTime

@UnitTest
class AssignmentServiceTest {
    @MockK
    private lateinit var assignmentRepository: AssignmentRepository

    @MockK
    private lateinit var missionRepository: MissionRepository

    @MockK
    private lateinit var evaluationTargetRepository: EvaluationTargetRepository

    private lateinit var assignmentService: AssignmentService

    @BeforeEach
    internal fun setUp() {
        assignmentService = AssignmentService(assignmentRepository, missionRepository, evaluationTargetRepository)
    }

    @Test
    fun `과제 제출물을 생성한다`() {
        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getById(any()) } returns createMission()
        every { evaluationTargetRepository.findByEvaluationIdAndUserId(any(), any()) } returns createEvaluationTarget()
        every { assignmentRepository.save(any()) } returns createAssignment()
        assertDoesNotThrow { assignmentService.create(1L, 1L, createAssignmentRequest()) }
    }

    @Test
    fun `과제 제출 기간이 아니면 생성할 수 없다`() {
        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = LocalDateTime.now().minusDays(2), endDateTime = LocalDateTime.now().minusDays(1)
        )
        assertThrows<IllegalStateException> { assignmentService.create(1L, 1L, createAssignmentRequest()) }
    }

    @Test
    fun `이미 제출한 이력이 있는 경우 새로 제출할 수 없다`() {
        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns true
        assertThrows<IllegalStateException> { assignmentService.create(1L, 1L, createAssignmentRequest()) }
    }

    @Test
    fun `평가 대상자가 아닌 경우 과제를 제출할 수 없다`() {
        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getById(any()) } returns createMission()
        every { evaluationTargetRepository.findByEvaluationIdAndUserId(any(), any()) } returns null
        assertThrows<IllegalArgumentException> { assignmentService.create(1L, 1L, createAssignmentRequest()) }
    }

    @Test
    fun `평가 상태가 'Waiting'이라면, 'Pass'로 업데이트한다`() {
        val evaluationTarget = createEvaluationTarget(evaluationStatus = EvaluationStatus.WAITING)

        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getById(any()) } returns createMission()
        every { evaluationTargetRepository.findByEvaluationIdAndUserId(any(), any()) } returns evaluationTarget
        every { assignmentRepository.save(any()) } returns createAssignment()

        assignmentService.create(1L, 1L, createAssignmentRequest())
        assertThat(evaluationTarget.isPassed).isTrue
    }
}
