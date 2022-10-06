package apply.domain.judgment

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class JudgmentItemTest : StringSpec({
    "evaluationItemId가 0L이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 0L, "testName", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeFalse()
    }

    "testName이 빈문자열이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 1L, "", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeFalse()
    }

    "ProgrammingLanguage가 NONE이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 1L, "testName", ProgrammingLanguage.NONE, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeFalse()
    }

    "evaluationItemId 이 0이 아니고, testName이 빈문자열이 아니고, ProgrammingLanguage 이 NONE이 아니면 유효하다" {
        val judgmentItem = JudgmentItem(1L, 1L, "testName", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeTrue()
    }
})
