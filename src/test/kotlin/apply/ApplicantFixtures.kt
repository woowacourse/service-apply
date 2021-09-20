package apply

import apply.domain.applicant.Applicant
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import java.time.LocalDate

fun createApplicant(
    name: String = NAME,
    email: String = EMAIL,
    phoneNumber: String = PHONE_NUMBER,
    gender: Gender = GENDER,
    birthday: LocalDate = BIRTHDAY,
    password: Password = PASSWORD,
    id: Long = 0L
): Applicant {
    return Applicant(createUser(name, email, phoneNumber, gender, birthday, password, id), id)
}

fun createApplicant(
    user: User,
    id: Long = 0L
): Applicant {
    return Applicant(user, id)
}
