package apply.application

import apply.createAssignment
import apply.createAssignmentRequest
import apply.createEvaluationTarget
import apply.createMission
import apply.createUser
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

    private val loginUser = createUser()
    private val missionId = 1L

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
        assertDoesNotThrow { assignmentService.create(missionId, loginUser.id, createAssignmentRequest()) }
    }

    @Test
    fun `과제 제출 기간이 아니면 생성할 수 없다`() {
        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = LocalDateTime.now().minusDays(2), endDateTime = LocalDateTime.now().minusDays(1)
        )
        assertThrows<IllegalStateException> { assignmentService.create(missionId, loginUser.id, createAssignmentRequest()) }
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
        assertThrows<IllegalArgumentException> { assignmentService.create(missionId, loginUser.id, createAssignmentRequest()) }
    }

    @Test
    fun `평가 상태가 'Waiting'이라면, 'Pass'로 업데이트한다`() {
        val evaluationTarget = createEvaluationTarget(evaluationStatus = EvaluationStatus.WAITING)

        every { assignmentRepository.existsByUserIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getById(any()) } returns createMission()
        every { evaluationTargetRepository.findByEvaluationIdAndUserId(any(), any()) } returns evaluationTarget
        every { assignmentRepository.save(any()) } returns createAssignment()

        assignmentService.create(missionId, loginUser.id, createAssignmentRequest())
        assertThat(evaluationTarget.isPassed).isTrue
    }

    @Test
    fun `제출한 과제물을 수정할 수 있다`() {
        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.findByUserIdAndMissionId(any(), any()) } returns createAssignment()
        assertDoesNotThrow { assignmentService.update(1L, 1L, createAssignmentRequest()) }
    }

    @Test
    fun `과제를 제출한 적이 있는 경우 제출물 조회시 제출물을 반환한다`() {
        every { assignmentRepository.findByUserIdAndMissionId(any(), any()) } returns createAssignment()
        assertDoesNotThrow { assignmentService.getAssignmentByUserIdAndMissionId(loginUser.id, missionId) }
    }

    @Test
    fun `과제를 제출한 적이 없는 경우 제출물 조회시 예외를 반환한다`() {
        every { assignmentRepository.findByUserIdAndMissionId(any(), any()) } returns null
        assertThrows<IllegalArgumentException> { assignmentService.getAssignmentByUserIdAndMissionId(loginUser.id, missionId) }
    }

    @Test
    fun `제출 불가능한 과제의 과제물을 수정할 수 없다`() {
        every { missionRepository.getById(any()) } returns createMission(submittable = false)
        assertThrows<IllegalStateException> { assignmentService.update(1L, 1L, createAssignmentRequest()) }
    }

    @Test
    fun `과제 제출 기간이 아니면 과제물을 수정할 수 없다`() {
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = LocalDateTime.now().minusDays(2), endDateTime = LocalDateTime.now().minusDays(1)
        )
        assertThrows<IllegalStateException> { assignmentService.update(1L, 1L, createAssignmentRequest()) }
    }

    @Test
    fun `제출한 과제물이 없는 경우 수정할 수 없다`() {
        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.findByUserIdAndMissionId(any(), any()) } returns null
        assertThrows<IllegalArgumentException> { assignmentService.update(1L, 1L, createAssignmentRequest()) }
    }
}
