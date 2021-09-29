package apply

import apply.domain.user.User
import apply.domain.user.Gender
import apply.domain.user.Password
import support.createLocalDate
import java.time.LocalDate

const val NAME: String = "지원자"
const val EMAIL: String = "test@email.com"
const val PHONE_NUMBER: String = "010-0000-0000"
val GENDER: Gender = Gender.MALE
val BIRTHDAY: LocalDate = createLocalDate(1995, 2, 2)
val PASSWORD: Password = Password("password")
val WRONG_PASSWORD: Password = Password("wrong_password")

const val RANDOM_PASSWORD_TEXT: String = "nEw_p@ssw0rd"
const val VALID_TOKEN: String = "SOME_VALID_TOKEN"

fun createUser(
    name: String = NAME,
    email: String = EMAIL,
    phoneNumber: String = PHONE_NUMBER,
    gender: Gender = GENDER,
    birthday: LocalDate = BIRTHDAY,
    password: Password = PASSWORD,
    id: Long = 0L
): User {
    return User(name, email, phoneNumber, gender, birthday, password, id)
}
