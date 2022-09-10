package apply.domain.mission

import apply.createMission
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import support.test.RepositoryTest

@RepositoryTest
class MissionRepositoryTest(
    private val missionRepository: MissionRepository,
    private val entityManager: TestEntityManager
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("과제 조회") {
        missionRepository.saveAll(
            listOf(
                createMission(evaluationId = 1L),
                createMission(evaluationId = 2L),
                createMission(evaluationId = 3L),
                createMission(evaluationId = 4L)
            )
        )

        expect("평가에 이미 등록된 과제가 있는지 확인한다") {
            val actual = missionRepository.existsByEvaluationId(1L)
            actual.shouldBeTrue()
        }

        expect("평가와 일치하는 모든 과제를 조회한다") {
            val actual = missionRepository.findAllByEvaluationIdIn(setOf(1L, 2L, 3L, 4L))
            actual shouldHaveSize 4
        }
    }

    context("과제 삭제") {
        val mission = missionRepository.save(createMission())

        expect("삭제 시 논리적 삭제가 적용된다") {
            missionRepository.deleteById(mission.id)
            missionRepository.findAll().shouldBeEmpty()
            missionRepository.findByIdOrNull(mission.id).shouldBeNull()
            missionRepository.existsById(mission.id).shouldBeFalse()
        }
    }
})
