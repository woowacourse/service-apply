package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import java.time.LocalDate

data class ApplicantResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate,
    var isCheater: Boolean
) {
    constructor(applicant: Applicant, isCheater: Boolean) : this(
        applicant.id,
        applicant.name,
        applicant.email,
        applicant.phoneNumber,
        applicant.gender,
        applicant.birthday,
        isCheater
    )
}
