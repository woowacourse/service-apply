package apply.domain.user

import support.domain.BaseRootEntity
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class User(
    @Embedded
    var information: UserInformation,

    @AttributeOverride(name = "value", column = Column(name = "password", nullable = false))
    @Embedded
    var password: Password,
    id: Long = 0L
) : BaseRootEntity<User>(id) {
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

    fun authenticate(password: Password) {
        identify(this.password == password) { "비밀번호가 일치하지 않습니다." }
    }

    fun changePassword(oldPassword: Password, newPassword: Password) {
        identify(this.password == oldPassword) { "기존 비밀번호가 일치하지 않습니다." }
        this.password = newPassword
    }

    fun resetPassword(name: String, birthday: LocalDate, password: String) {
        identify(information.same(name, birthday)) { "사용자 정보가 일치하지 않습니다." }
        this.password = Password(password)
        registerEvent(PasswordResetEvent(id, name, email, password))
    }

    fun changePhoneNumber(phoneNumber: String) {
        information = information.copy(phoneNumber = phoneNumber)
    }

    private fun identify(value: Boolean, lazyMessage: () -> Any = {}) {
        if (!value) {
            val message = lazyMessage()
            throw UserAuthenticationException(message.toString())
        }
    }
}
