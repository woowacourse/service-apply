package apply.domain.user

import support.domain.BaseEntity
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class User(
    @Embedded
    val information: UserInformation,

    @AttributeOverride(name = "value", column = Column(name = "password", nullable = false))
    @Embedded
    var password: Password,
    id: Long = 0L
) : BaseEntity(id) {
    val name: String
        get() = information.name

    val email: String
        get() = information.email

    val phoneNumber: String
        get() = information.phoneNumber

    val gender: Gender
        get() = information.gender

    val birthday: LocalDate
        get() = information.birthday

    constructor(
        name: String,
        email: String,
        phoneNumber: String,
        gender: Gender,
        birthday: LocalDate,
        password: Password,
        id: Long = 0L
    ) : this(
        UserInformation(name, email, phoneNumber, gender, birthday), password, id
    )

    fun authenticate(applicant: User) {
        authenticate(applicant.password)
        identify(this.information == applicant.information)
    }

    fun authenticate(password: Password) {
        identify(this.password == password)
    }

    fun changePassword(oldPassword: Password, newPassword: Password) {
        authenticate(oldPassword)
        this.password = newPassword
    }

    fun resetPassword(name: String, birthday: LocalDate, password: String) {
        identify(information.same(name, birthday))
        this.password = Password(password)
    }

    private fun identify(value: Boolean, lazyMessage: () -> Any = {}) {
        if (!value) {
            val message = lazyMessage()
            throw UserAuthenticationException(message.toString())
        }
    }
}
