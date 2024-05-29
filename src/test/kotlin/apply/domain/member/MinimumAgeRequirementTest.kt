package apply.domain.member

import apply.createMemberInformation
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate

class MinimumAgeRequirementTest : StringSpec({
    val requirement = MinimumAgeRequirement(14, LocalDate.of(2024, 5, 30))

    "만 14세 이상은 최소 연령 요건을 충족한다" {
        val information = createMemberInformation(birthday = LocalDate.of(2010, 5, 1))
        val actual = requirement.meets(information)
        actual.shouldBeTrue()
    }

    "만 14세 미만은 최소 연령 요건을 충족하지 않는다" {
        val information = createMemberInformation(birthday = LocalDate.of(2010, 5, 31))
        val actual = requirement.meets(information)
        actual.shouldBeFalse()
    }

    "기준일이 생일과 같으면 만 나이가 늘어난다" {
        val information = createMemberInformation(birthday = LocalDate.of(2010, 5, 30))
        val actual = requirement.meets(information)
        actual.shouldBeTrue()
    }
})
