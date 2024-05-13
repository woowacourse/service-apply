package apply

import apply.application.AuthenticateMemberRequest
import apply.application.RegisterMemberRequest
import apply.domain.member.Gender
import apply.domain.member.Member
import apply.domain.member.Password
import support.createLocalDate
import java.time.LocalDate

const val NAME: String = "지원자"
const val EMAIL: String = "test@email.com"
const val PHONE_NUMBER: String = "010-0000-0000"
const val GITHUB_USERNAME: String = "jaeyeonling"
val GENDER: Gender = Gender.MALE
val BIRTHDAY: LocalDate = createLocalDate(1995, 2, 2)
val PASSWORD: Password = Password("password")
val CONFIRM_PASSWORD: Password = Password("password")
val NEW_PASSWORD: Password = Password("new_password")
val WRONG_PASSWORD: Password = Password("wrong_password")

const val RANDOM_PASSWORD_TEXT: String = "nEw_p@ssw0rd"
const val VALID_TOKEN: String = "SOME_VALID_TOKEN"

fun createMember(
    name: String = NAME,
    email: String = EMAIL,
    phoneNumber: String = PHONE_NUMBER,
    gender: Gender = GENDER,
    githubUsername: String = GITHUB_USERNAME,
    birthday: LocalDate = BIRTHDAY,
    password: Password = PASSWORD,
    id: Long = 0L
): Member {
    return Member(name, email, phoneNumber, gender, githubUsername, birthday, password, id)
}

fun createRegisterMemberRequest(
    name: String = NAME,
    email: String = EMAIL,
    phoneNumber: String = PHONE_NUMBER,
    githubUsername: String = GITHUB_USERNAME,
    gender: Gender = GENDER,
    birthday: LocalDate = BIRTHDAY,
    password: Password = PASSWORD,
    confirmPassword: Password = CONFIRM_PASSWORD,
    authenticationCode: String = VALID_CODE
): RegisterMemberRequest {
    return RegisterMemberRequest(
        name,
        email,
        phoneNumber,
        gender,
        githubUsername,
        birthday,
        password,
        confirmPassword,
        authenticationCode
    )
}

fun createAuthenticateMemberRequest(
    email: String = EMAIL,
    password: Password = PASSWORD
): AuthenticateMemberRequest {
    return AuthenticateMemberRequest(email, password)
}
