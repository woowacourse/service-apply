package apply.domain.mission

import apply.createJudgmentItem
import apply.createMission
import apply.domain.mission.SubmissionMethod.GENERIC_URL
import apply.domain.mission.SubmissionMethod.PRIVATE_REPOSITORY
import apply.domain.mission.SubmissionMethod.PUBLIC_PULL_REQUEST
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import java.time.LocalDateTime

class MissionTest : StringSpec({
    val today = LocalDateTime.now()
    val yesterday = today.minusDays(1L)
    val tomorrow = today.plusDays(1L)

    "제출이 가능하고 평가 항목이 있는 경우 자동 채점을 실행할 수 있다" {
        listOf(PUBLIC_PULL_REQUEST, PRIVATE_REPOSITORY).forAll { submissionMethod ->
            val mission = createMission(
                startDateTime = today,
                submissionStartDateTime = today,
                endDateTime = tomorrow,
                submittable = true,
                submissionMethod = submissionMethod,
            )
            val judgmentItem = createJudgmentItem()
            shouldNotThrowAny { mission.checkExampleJudgeable(judgmentItem) }
            shouldNotThrowAny { mission.checkRealJudgeable(judgmentItem) }
        }
    }

    "제출이 허용되지 않으면 예제 테스트는 실행할 수 없지만 본 자동 채점은 실행할 수 있다" {
        listOf(PUBLIC_PULL_REQUEST, PRIVATE_REPOSITORY).forAll { submissionMethod ->
            val mission = createMission(
                startDateTime = today,
                submissionStartDateTime = today,
                endDateTime = tomorrow,
                submittable = false,
                submissionMethod = submissionMethod,
            )
            val judgmentItem = createJudgmentItem()
            shouldThrow<IllegalStateException> { mission.checkExampleJudgeable(judgmentItem) }
            shouldNotThrowAny { mission.checkRealJudgeable(judgmentItem) }
        }
    }

    "제출 기간이 지난 경우 예제 테스트는 실행할 수 없지만 본 자동 채점은 실행할 수 있다" {
        listOf(PUBLIC_PULL_REQUEST, PRIVATE_REPOSITORY).forAll { submissionMethod ->
            val mission = createMission(
                startDateTime = yesterday,
                submissionStartDateTime = yesterday,
                endDateTime = yesterday,
                submittable = true,
                submissionMethod = submissionMethod,
            )
            val judgmentItem = createJudgmentItem()
            shouldThrow<IllegalStateException> { mission.checkExampleJudgeable(judgmentItem) }
            shouldNotThrowAny { mission.checkRealJudgeable(judgmentItem) }
        }
    }

    "채점 항목이 없으면 자동 채점을 실행할 수 없다" {
        listOf(PUBLIC_PULL_REQUEST, PRIVATE_REPOSITORY).forAll { submissionMethod ->
            val mission = createMission(
                startDateTime = today,
                endDateTime = tomorrow,
                submittable = true,
                submissionMethod = submissionMethod,
            )
            val judgmentItem = null
            shouldThrow<IllegalStateException> { mission.checkExampleJudgeable(judgmentItem) }
            shouldThrow<IllegalStateException> { mission.checkRealJudgeable(judgmentItem) }
        }
    }

    "제출 방식이 일반 URL이면 자동 채점을 실행할 수 없다" {
        val mission = createMission(
            startDateTime = today,
            endDateTime = tomorrow,
            submittable = true,
            submissionMethod = GENERIC_URL,
        )
        val judgmentItem = null
        shouldThrow<IllegalStateException> { mission.checkExampleJudgeable(judgmentItem) }
        shouldThrow<IllegalStateException> { mission.checkRealJudgeable(judgmentItem) }
    }
})
