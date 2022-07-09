package apply.domain.assignment

import apply.createAssignment
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import support.test.RepositoryTest

@RepositoryTest
class AssignmentRepositoryTest(
    private val assignmentRepository: AssignmentRepository
) : AnnotationSpec() {
    private val assignment = createAssignment(userId = 1L, missionId = 1L)

    @BeforeEach
    fun setUp() {
        assignmentRepository.save(assignment)
    }

    @Test
    fun `지원자와 과제에 해당하는 제출물의 존재 여부를 조회힌다`() {
        assignmentRepository.existsByUserIdAndMissionId(assignment.userId, assignment.missionId).shouldBeTrue()
    }

    @Test
    fun `지원자와 과제에 해당하는 제출물을 반환한다`() {
        assignmentRepository.findByUserIdAndMissionId(assignment.userId, assignment.missionId).shouldNotBeNull()
    }

    @Test
    fun `지원자의 모든 제출물을 조회한다`() {
        assignmentRepository.saveAll(
            listOf(
                createAssignment(userId = 1L, missionId = 2L),
                createAssignment(userId = 1L, missionId = 3L)
            )
        )
        assignmentRepository.findAllByUserId(1L).shouldHaveSize(3)
    }
}
