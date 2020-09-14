package apply.domain.applicant.dto

import apply.domain.applicant.Gender
import java.time.LocalDate

class ApplicantRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthDay: LocalDate,
    val password: String
)
