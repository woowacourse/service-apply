package apply.domain.evaluationtarget

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import support.test.RepositoryTest

@RepositoryTest
class EvaluationTargetRepositoryTest : AnnotationSpec() {

    @Autowired
    private lateinit var evaluationTargetRepository: EvaluationTargetRepository

    private val evaluationTargets: List<EvaluationTarget> = listOf(
        EvaluationTarget(
            EVALUATION_ID,
            userId = 1L
        ),
        EvaluationTarget(
            EVALUATION_ID,
            userId = 2L
        )
    )

    companion object {
        private const val EVALUATION_ID = 1L
    }

    @BeforeEach
    fun setUp() {
        evaluationTargetRepository.saveAll(evaluationTargets)
    }

    @Test
    fun `평가의 id로 평가 대상자를 찾는다`() {
        val results = evaluationTargetRepository.findAllByEvaluationId(EVALUATION_ID)

        results.zip(evaluationTargets)
            .forEach {
                it.first.shouldBeEqualToUsingFields(it.second, EvaluationTarget::evaluationId)
            }
    }

    @Test
    fun `평가의 id를 가지고 있는 평가 대상자의 존재 여부를 확인한다`() {
        listOf(
            1L to true,
            2L to false
        ).forAll { (evaluationId: Long, expected: Boolean) ->
            val actual = evaluationTargetRepository.existsByEvaluationId(evaluationId)
            actual shouldBe expected
        }
    }

    @Test
    fun `지원자의 id들에 해당되는 평가 대상자를 제거한다`() {
        evaluationTargetRepository.deleteByUserIdIn(setOf(1L, 2L))

        evaluationTargetRepository.count() shouldBe 0
    }

    @Test
    fun `지정한 평가에 해당되고 지원자의 id들에 해당되는 평가 대상자를 제거한다`() {
        evaluationTargetRepository.save(
            EvaluationTarget(
                evaluationId = 2L,
                userId = 1L
            )
        )
        evaluationTargetRepository.deleteByEvaluationIdAndUserIdIn(1L, setOf(1L, 2L))

        evaluationTargetRepository.count() shouldBe 1
    }

    fun `지정한 평가에 해당되고 평가대상자들의 평가 상태가 특정 평가 상태와 일치하는 평가 대상자를 찾는다`() {
        listOf(
            EvaluationStatus.PASS to 2,
            EvaluationStatus.FAIL to 1
        ).forAll { (evaluationStatus: EvaluationStatus, expectedSize: Int) ->
            evaluationTargetRepository.saveAll(
                listOf(
                    EvaluationTarget(EVALUATION_ID, userId = 1L, evaluationStatus = EvaluationStatus.PASS),
                    EvaluationTarget(EVALUATION_ID, userId = 2L, evaluationStatus = EvaluationStatus.PASS),
                    EvaluationTarget(EVALUATION_ID, userId = 3L, evaluationStatus = EvaluationStatus.FAIL)
                )
            )

            val actual =
                evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(EVALUATION_ID, evaluationStatus)

            actual shouldHaveSize expectedSize
        }
    }

    fun `지정한 평가와 회원에 해당하는 대상자의 존재 여부를 확인한다`() {
        listOf(1L, 2L).forAll { userId: Long ->
            val isExists = evaluationTargetRepository.existsByUserIdAndEvaluationId(userId, EVALUATION_ID)
            isExists.shouldBeTrue()
        }
    }
}
