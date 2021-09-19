package apply.application

import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class RegisterUserRequest(
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
