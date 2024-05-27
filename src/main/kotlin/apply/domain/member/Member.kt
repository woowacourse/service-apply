package apply.domain.member

import support.domain.BaseRootEntity
import java.time.LocalDate
import java.time.Period
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
    val email: String
        get() = information.email

    val name: String
        get() = information.name

    val birthday: LocalDate
        get() = information.birthday

    val phoneNumber: String
        get() = information.phoneNumber

    val githubUsername: String
        get() = information.githubUsername

    init {
        val age = Period.between(birthday, LocalDate.now()).years
        require(age >= 14) { "만 14세 미만은 회원 가입할 수 없습니다." }
    }

    constructor(
        email: String,
        password: Password,
        name: String,
        birthday: LocalDate,
        phoneNumber: String,
        githubUsername: String,
        id: Long = 0L,
    ) : this(
        MemberInformation(
            email = email,
            name = name,
            birthday = birthday,
            phoneNumber = phoneNumber,
            githubUsername = githubUsername
        ),
        password,
        id,
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
