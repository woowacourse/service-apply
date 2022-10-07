package apply.domain.judgment

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class JudgmentItemTest : StringSpec({
    "자동 채점 항목의 평가항목이 0이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 0L, "testName", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeFalse()
    }

    "자동 채점 항목의 테스트 이름이 빈문자열이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 1L, "", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeFalse()
    }

    "자동 채점 항목의 프로그래밍 언어가 NONE이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 1L, "testName", ProgrammingLanguage.NONE, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeFalse()
    }

    "자동 채점 항목의 평가 항목이 0이 아니고, 테스트 이름이 빈 문자열이 아니고, 프로그래밍 언어가 NONE 이 아니라면 유효하다" {
        val judgmentItem = JudgmentItem(1L, 1L, "testName", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid

        isValid.shouldBeTrue()
    }
})
