package apply.domain.judgment

import apply.createCommit
import apply.createJudgment
import apply.createJudgmentItem
import apply.domain.judgment.JudgmentType.EXAMPLE
import apply.domain.judgment.JudgmentType.REAL
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import support.test.RepositoryTest
import support.test.spec.afterRootTest
import java.time.LocalDateTime.now

@RepositoryTest
class JudgmentRepositoryTest(
    private val judgmentRepository: JudgmentRepository,
    private val entityManager: TestEntityManager
) : ExpectSpec({
    extensions(SpringExtension)

    context("자동 채점 저장") {
        expect("자동 채점만 저장한다") {
            val actual = judgmentRepository.save(createJudgment(id = 0L))
            actual.id.shouldNotBeZero()
        }

        expect("자동 채점과 자동 채점 기록을 함께 저장한다") {
            val judgment = createJudgment(records = listOf(JudgmentRecord(createCommit())))
            judgmentRepository.save(judgment)
        }
    }

    context("자동 채점 조회") {
        val assignmentId = 1L
        judgmentRepository.saveAll(
            listOf(
                createJudgment(assignmentId, EXAMPLE, listOf(JudgmentRecord(createCommit()))),
                createJudgment(assignmentId, REAL, listOf(JudgmentRecord(createCommit())))
            )
        )

        expect("과제 및 해당 유형의 자동 채점이 있는지 조회한다") {
            val actual = judgmentRepository.findByAssignmentIdAndType(assignmentId, EXAMPLE)
            actual.shouldNotBeNull()
            actual.lastCommit shouldBe createCommit()
        }
    }

    context("자동 채점 수정") {
        val judgmentItem = createJudgmentItem()
        val commit = createCommit("commit1")
        val judgment = judgmentRepository.save(
            createJudgment(records = listOf(JudgmentRecord(commit, startedDateTime = now().minusDays(1))))
        )

        expect("새 커밋이 추가되면 자동 채점 기록을 저장한다") {
            val actual = judgmentRepository.getById(judgment.id)
            actual.start(createCommit("commit2"))
        }

        expect("특정 커밋의 자동 채점 기록을 수정한다") {
            val actual = judgmentRepository.getById(judgment.id)
            actual.success(commit, passCount = 9, totalCount = 10)
        }
    }

    afterEach {
        entityManager.flush()
        entityManager.clear()
    }

    afterRootTest {
        judgmentRepository.deleteAll()
    }
})
