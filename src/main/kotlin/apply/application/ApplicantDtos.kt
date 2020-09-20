package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import java.time.LocalDate

data class ApplicantRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate,
    val password: String
) {
    fun toEntity() = toEntity(0L)

    fun toEntity(id: Long) = Applicant(
        id = id,
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        gender = gender,
        birthday = birthday,
        password = password
    )
}
