package apply.domain.assignment

import apply.createAssignment
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import support.test.RepositoryTest

@RepositoryTest
class AssignmentRepositoryTest(
    private val assignmentRepository: AssignmentRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("과제 제출물 조회") {
        assignmentRepository.save(createAssignment(memberId = 1L, missionId = 1L))

        expect("지원자 및 과제에 해당하는 과제 제출물이 있는지 확인한다") {
            val actual = assignmentRepository.existsByMemberIdAndMissionId(1L, 1L)
            actual.shouldBeTrue()
        }

        expect("지원자 및 과제에 해당하는 과제 제출물이 있는지 조회한다") {
            val actual = assignmentRepository.findByMemberIdAndMissionId(1L, 1L)
            actual.shouldNotBeNull()
        }

        expect("지원자의 모든 과제 제출물을 조회한다") {
            val actual = assignmentRepository.findAllByMemberId(1L)
            actual shouldHaveSize 1
        }
    }
})
