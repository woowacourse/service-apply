package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Past
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
    @field:Pattern(regexp = "[가-힣]{1,30}", message = "올바른 형식의 이름이어야 합니다")
    val name: String,

    @field:Email
    val email: String,

    @field:Pattern(regexp = "010-\\d{4}-\\d{4}", message = "올바른 형식의 전화번호여야 합니다")
    val phoneNumber: String,
    val gender: Gender,

    @field:Past
    val birthday: LocalDate,
    val password: Password,
    val confirmPassword: Password,

    @field:NotBlank
    val authenticationCode: String
) {
    fun toEntity(): User {
        return User(name, email, phoneNumber, gender, birthday, password)
    }
}

data class AuthenticateUserRequest(
    @field:Email
    val email: String,
    val password: Password
)

data class ResetPasswordRequest(
    @field:Pattern(regexp = "[가-힣]{1,30}", message = "올바른 형식의 이름이어야 합니다")
    val name: String,

    @field:Email
    val email: String,

    @field:Past
    val birthday: LocalDate
)

data class EditPasswordRequest(
    val oldPassword: Password,
    val password: Password,
    val confirmPassword: Password
)

data class EditInformationRequest(
    @field:Pattern(regexp = "010-\\d{4}-\\d{4}", message = "올바른 형식의 전화번호여야 합니다")
    val phoneNumber: String
)
