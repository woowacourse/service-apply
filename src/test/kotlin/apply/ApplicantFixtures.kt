package apply

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import support.createLocalDate
import java.time.LocalDate

const val NAME: String = "지원자"
const val EMAIL: String = "test@email.com"
private const val PHONE_NUMBER: String = "010-0000-0000"
val BIRTHDAY: LocalDate = createLocalDate(1995, 2, 2)
private val PASSWORD: Password = Password("password")

const val RANDOM_PASSWORD_TEXT: String = "nEw_p@ssw0rd"

fun createApplicant(
    name: String = NAME,
    email: String = EMAIL,
    phoneNumber: String = PHONE_NUMBER,
    gender: Gender = Gender.MALE,
    birthday: LocalDate = BIRTHDAY,
    password: Password = PASSWORD,
    id: Long = 0L
): Applicant {
    return Applicant(name, email, phoneNumber, gender, birthday, password, id)
}
