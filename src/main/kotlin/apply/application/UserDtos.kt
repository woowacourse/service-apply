package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate
) {
    constructor(user: User) : this(
        user.id,
        user.name,
        user.email,
        user.phoneNumber,
        user.gender,
        user.birthday
    )
}

data class ApplicantAndFormResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate,
    val isCheater: Boolean,
    val applicationForm: ApplicationForm
) {
    constructor(user: User, isCheater: Boolean, applicationForm: ApplicationForm) : this(
        user.id,
        user.name,
        user.email,
        user.phoneNumber,
        user.gender,
        user.birthday,
        isCheater,
        applicationForm
    )
}

data class RegisterUserRequest(
    @field:NotBlank
    val name: String,

    @field:NotNull
    @field:Email
    val email: String,

    @field:Pattern(regexp = "010-\\d{4}-\\d{4}")
    val phoneNumber: String,

    @field:NotNull
    val gender: Gender,

    @field:NotNull
    val birthday: LocalDate,

    @field:NotNull
    val password: Password,

    @field:NotBlank
    val authenticationCode: String
) {
    fun toEntity(): User {
        return User(name, email, phoneNumber, gender, birthday, password)
    }
}

data class AuthenticateUserRequest(
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

data class EditInformationRequest(
    @field:Pattern(regexp = "010-\\d{4}-\\d{4}")
    val phoneNumber: String
)
