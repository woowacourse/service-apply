package apply.domain.member

import support.domain.BaseRootEntity
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_member", columnNames = ["email"])]
)
@Entity
class Member(
    @Embedded
    var information: MemberInformation,

    @AttributeOverride(name = "value", column = Column(name = "password", nullable = false))
    @Embedded
    var password: Password,
    id: Long = 0L,
) : BaseRootEntity<Member>(id) {
    val name: String
        get() = information.name

    val email: String
        get() = information.email

    val phoneNumber: String
        get() = information.phoneNumber

    val birthday: LocalDate
        get() = information.birthday

    constructor(
        name: String,
        email: String,
        phoneNumber: String,
        birthday: LocalDate,
        password: Password,
        id: Long = 0L,
    ) : this(
        MemberInformation(name, email, phoneNumber, birthday), password, id
    )

    fun authenticate(password: Password) {
        identify(this.password == password) { "사용자 정보가 일치하지 않습니다." }
    }

    fun resetPassword(name: String, birthday: LocalDate, password: String) {
        identify(information.same(name, birthday)) { "사용자 정보가 일치하지 않습니다." }
        this.password = Password(password)
        registerEvent(PasswordResetEvent(id, name, email, password))
    }

    fun changePassword(oldPassword: Password, newPassword: Password) {
        identify(this.password == oldPassword) { "기존 비밀번호가 일치하지 않습니다." }
        this.password = newPassword
    }

    fun changePhoneNumber(phoneNumber: String) {
        information = information.copy(phoneNumber = phoneNumber)
    }

    private fun identify(value: Boolean, lazyMessage: () -> Any = {}) {
        if (!value) {
            val message = lazyMessage()
            throw UnidentifiedMemberException(message.toString())
        }
    }
}
