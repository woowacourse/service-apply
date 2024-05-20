package apply

import apply.application.AuthenticateMemberRequest
import apply.application.RegisterMemberRequest
import apply.domain.member.Member
import apply.domain.member.Password
import support.createLocalDate
import java.time.LocalDate

const val EMAIL: String = "test@email.com"
private const val NAME: String = "지원자"
private val BIRTHDAY: LocalDate = createLocalDate(1995, 2, 2)
private const val PHONE_NUMBER: String = "010-0000-0000"
private const val GITHUB_USERNAME: String = "jaeyeonling"
val PASSWORD: Password = Password("password")
private val CONFIRM_PASSWORD: Password = Password("password")
val NEW_PASSWORD: Password = Password("new_password")
val WRONG_PASSWORD: Password = Password("wrong_password")

const val RANDOM_PASSWORD_TEXT: String = "nEw_p@ssw0rd"
const val VALID_TOKEN: String = "SOME_VALID_TOKEN"

fun createMember(
    email: String = EMAIL,
    password: Password = PASSWORD,
    name: String = NAME,
    birthday: LocalDate = BIRTHDAY,
    phoneNumber: String = PHONE_NUMBER,
    githubUsername: String = GITHUB_USERNAME,
    id: Long = 0L,
): Member {
    return Member(email, password, name, birthday, phoneNumber, githubUsername, id)
}

fun createRegisterMemberRequest(
    email: String = EMAIL,
    password: Password = PASSWORD,
    confirmPassword: Password = CONFIRM_PASSWORD,
    name: String = NAME,
    birthday: LocalDate = BIRTHDAY,
    phoneNumber: String = PHONE_NUMBER,
    githubUsername: String = GITHUB_USERNAME,
    authenticationCode: String = VALID_CODE,
): RegisterMemberRequest {
    return RegisterMemberRequest(
        email,
        password,
        confirmPassword,
        name,
        birthday,
        phoneNumber,
        githubUsername,
        authenticationCode,
    )
}

fun createAuthenticateMemberRequest(
    email: String = EMAIL,
    password: Password = PASSWORD,
): AuthenticateMemberRequest {
    return AuthenticateMemberRequest(email, password)
}
