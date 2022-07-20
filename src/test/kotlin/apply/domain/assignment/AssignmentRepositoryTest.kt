package apply.domain.assignment

import apply.createAssignment
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import support.test.RepositoryTest

@RepositoryTest
internal class AssignmentRepositoryTest(
    private val assignmentRepository: AssignmentRepository
) : DescribeSpec({
    val assignment = createAssignment(userId = 1L, missionId = 1L)

    describe("AssignmentRepository") {
        context("지원자가 과제물을 제출하면") {
            assignmentRepository.save(assignment)

            it("지원자와 과제에 해당하는 제출물의 존재 여부를 조회할 수 있다") {
                assignmentRepository.existsByUserIdAndMissionId(assignment.userId, assignment.missionId).shouldBeTrue()
            }

            it("지원자와 과제에 해당하는 제출물을 반환할 수 있다") {
                assignmentRepository.findByUserIdAndMissionId(assignment.userId, assignment.missionId).shouldNotBeNull()
            }

            it("지원자의 모든 제출물을 조회할 수 있다") {
                assignmentRepository.saveAll(
                    listOf(
                        createAssignment(userId = 1L, missionId = 2L),
                        createAssignment(userId = 1L, missionId = 3L)
                    )
                )
                assignmentRepository.findAllByUserId(1L).shouldHaveSize(3)
            }
        }
    }
})
