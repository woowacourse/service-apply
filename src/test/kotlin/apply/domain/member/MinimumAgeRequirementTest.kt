package apply.domain.member

import apply.createMemberInformation
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.time.LocalDate

class MinimumAgeRequirementTest : StringSpec({
    val requirement = MinimumAgeRequirement(age = 14, baseDate = LocalDate.of(2024, 5, 30))

    "만 14세 이상은 최소 연령 요건을 충족한다" {
        val information = createMemberInformation(birthday = LocalDate.of(2010, 5, 1))
        shouldNotThrowAny { requirement.require(information) }
    }

    "만 14세 미만은 최소 연령 요건을 충족하지 않는다" {
        val information = createMemberInformation(birthday = LocalDate.of(2010, 5, 31))
        shouldThrow<IllegalArgumentException> { requirement.require(information) }
    }

    "기준일이 생일과 같으면 만 나이가 늘어난다" {
        val information = createMemberInformation(birthday = LocalDate.of(2010, 5, 30))
        shouldNotThrowAny { requirement.require(information) }
    }
})
