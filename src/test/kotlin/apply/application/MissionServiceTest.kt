package apply.application

import apply.createAssignment
import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.mission.MissionRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAnyOf
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import org.springframework.data.repository.findByIdOrNull
import support.test.UnitTest

@UnitTest
class MissionServiceTest : AnnotationSpec() {
    @MockK
    lateinit var missionRepository: MissionRepository

    @MockK
    lateinit var evaluationRepository: EvaluationRepository

    @MockK
    lateinit var evaluationTargetRepository: EvaluationTargetRepository

    @MockK
    lateinit var assignmentRepository: AssignmentRepository

    private lateinit var missionService: MissionService

    private val recruitmentId = 1L
    private val userId = 1L

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
        shouldNotThrow<Exception> { missionService.save(missionData) }
    }

    @Test
    fun `존재하지 않는 평가 id인 경우 예외를 던진다`() {
        val missionData = createMissionData()
        every { evaluationRepository.existsById(any()) } returns false
        shouldThrowExactly<IllegalArgumentException> { missionService.save(missionData) }
    }

    @Test
    fun `해당 평가에 이미 등록된 과제가 있는 경우 예외를 던진다`() {
        val missionData = createMissionData()
        every { evaluationRepository.existsById(any()) } returns true
        every { missionRepository.existsByEvaluationId(any()) } returns true
        shouldThrowExactly<IllegalStateException> { missionService.save(missionData) }
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
        assertSoftly {
            actual shouldHaveSize 2
            actual shouldContainAnyOf listOf(
                MissionAndEvaluationResponse(firstMission, firstEvaluation),
                MissionAndEvaluationResponse(secondMission, secondEvaluation)
            )
        }
    }

    @Test
    fun `특정 모집에 해당하는 나의 숨겨지지 않은 과제들을 조회한다`() {
        val missions = listOf(createMission(id = 1L), createMission(id = 2L))
        every { evaluationRepository.findAllByRecruitmentId(any()) } returns
            listOf(createEvaluation(id = 1L), createEvaluation(id = 2L))
        every { evaluationTargetRepository.existsByUserIdAndEvaluationId(any(), any()) } returns true
        every { missionRepository.findAllByEvaluationIdIn(any()) } returns missions
        every { assignmentRepository.findAllByUserId(any()) } returns listOf(
            createAssignment(userId = userId, missionId = 1L),
            createAssignment(userId = userId, missionId = 2L)
        )

        val responses = missionService.findAllByUserIdAndRecruitmentId(userId, recruitmentId)

        assertSoftly {
            responses shouldHaveSize 2
            responses shouldContainExactlyInAnyOrder listOf(
                MissionResponse(missions[0], true),
                MissionResponse(missions[1], true)
            )
        }
    }

    @Test
    fun `과제의 상태가 hidden인 경우 조회할 수 없다`() {
        val missions = listOf(createMission(id = 1L, hidden = true), createMission(id = 2L, hidden = true))
        every { evaluationRepository.findAllByRecruitmentId(any()) } returns
            listOf(createEvaluation(id = 1L), createEvaluation(id = 2L))
        every { evaluationTargetRepository.existsByUserIdAndEvaluationId(any(), any()) } returns true
        every { missionRepository.findAllByEvaluationIdIn(any()) } returns missions
        every { assignmentRepository.findAllByUserId(any()) } returns listOf(
            createAssignment(userId = userId, missionId = 1L),
            createAssignment(userId = userId, missionId = 2L)
        )

        val responses = missionService.findAllByUserIdAndRecruitmentId(userId, recruitmentId)

        responses.shouldBeEmpty()
    }

    @Test
    fun `과제를 삭제한다`() {
        val mission = createMission(submittable = false)
        every { missionRepository.findByIdOrNull(mission.id) } returns mission
        every { missionRepository.deleteById(any()) } just Runs

        shouldNotThrow<Exception> { missionService.deleteById(mission.id) }
    }

    @Test
    fun `제출 가능한 상태의 과제를 삭제하면 예외가 발생한다`() {
        val mission = createMission(submittable = true)
        every { missionRepository.findByIdOrNull(mission.id) } returns mission
        every { missionRepository.deleteById(any()) } just Runs

        shouldThrowExactly<IllegalStateException> { missionService.deleteById(mission.id) }
    }

    @Test
    fun `존재하지 않는 과제를 삭제하면 예외가 발생한다`() {
        val missionId = 1L
        every { missionRepository.findByIdOrNull(missionId) } returns null
        every { missionRepository.deleteById(any()) } just Runs

        shouldThrowExactly<NoSuchElementException> { missionService.deleteById(missionId) }
    }
}
