package apply.domain.member

import java.time.LocalDate
import java.time.Period

class MinimumAgeRequirement(
    private val age: Int = DEFAULT_MINIMUM_AGE,
    private val baseDate: LocalDate = LocalDate.now(),
) : AuthorizationRequirement {
    override fun meets(information: MemberInformation): Boolean {
        val age = Period.between(information.birthday, baseDate).years
        return age >= DEFAULT_MINIMUM_AGE
    }

    companion object {
        private const val DEFAULT_MINIMUM_AGE: Int = 14
    }
}
