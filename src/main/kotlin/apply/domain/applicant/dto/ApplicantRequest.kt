package apply.domain.applicant.dto

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import java.time.LocalDate

class ApplicantRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate,
    val password: String
) {
    fun toEntity(): Applicant {
        return Applicant(
            name = name,
            email = email,
            phoneNumber = phoneNumber,
            gender = gender,
            birthday = birthday,
            password = password
        )
    }
}
