package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.domain.applicationform.ApplicationForm
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ApplicantBasicResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate
) {
    constructor(applicant: Applicant) : this(
        applicant.id,
        applicant.name,
        applicant.email,
        applicant.phoneNumber,
        applicant.gender,
        applicant.birthday
    )
}

data class ApplicantResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate,
    var isCheater: Boolean,
    val applicationForm: ApplicationForm
) {
    constructor(applicant: Applicant, isCheater: Boolean, applicationForm: ApplicationForm) : this(
        applicant.id,
        applicant.name,
        applicant.email,
        applicant.phoneNumber,
        applicant.gender,
        applicant.birthday,
        isCheater,
        applicationForm
    )
}

data class ApplicantInformation(
    @field:NotBlank
    val name: String,

    @field:NotNull
    @field:Email
    val email: String,

    @field:NotBlank
    val phoneNumber: String,

    @field:NotNull
    val gender: Gender,

    @field:NotNull
    val birthday: LocalDate,

    @field:NotNull
    val password: Password
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

data class VerifyApplicantRequest(
    @field:NotNull
    @field:Email
    val email: String,

    @field:NotNull
    val password: Password
)

data class ResetPasswordRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    @field:Email
    val email: String,

    @field:NotNull
    val birthday: LocalDate
)

data class EditPasswordRequest(
    @field:NotNull
    val password: Password,

    @field:NotNull
    val newPassword: Password
)
