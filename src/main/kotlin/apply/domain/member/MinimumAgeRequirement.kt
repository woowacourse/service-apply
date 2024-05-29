package apply.domain.member

import java.time.LocalDate
import java.time.Period

class MinimumAgeRequirement(
    private val age: Int = DEFAULT_MINIMUM_AGE,
    private val baseDate: LocalDate = LocalDate.now(),
) : AuthorizationRequirement {
    override fun require(information: MemberInformation) {
        val age = Period.between(information.birthday, baseDate).years
        require(age >= this.age) { "만 ${this.age}세 미만은 회원 가입할 수 없습니다." }
    }

    companion object {
        private const val DEFAULT_MINIMUM_AGE: Int = 14
    }
}
