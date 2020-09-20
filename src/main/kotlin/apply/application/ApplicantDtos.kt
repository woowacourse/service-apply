package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ApplicantInfo(
    @field:NotBlank
    val name: String,

    @field:NotBlank
    val email: String,

    @field:NotBlank
    val phoneNumber: String,

    @field:NotNull
    val gender: Gender,

    @field:NotNull
    val birthday: LocalDate,

    @field:NotBlank
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
