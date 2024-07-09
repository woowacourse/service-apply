package apply.domain.member

import support.domain.BaseRootEntity
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class Member(
    information: MemberInformation,
    @AttributeOverride(name = "value", column = Column(name = "password", nullable = false))
    @Embedded
    var password: Password,
    authorizationRequirement: AuthorizationRequirement,
    id: Long = 0L,
) : BaseRootEntity<Member>(id) {
    @OneToOne(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.REMOVE], orphanRemoval = true)
    private var _information: MemberInformation? = information
    private val information: MemberInformation
        get() = _information ?: MemberInformation.DELETED

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
        information.member = this
        authorizationRequirement.require(information)
    }

    fun authenticate(password: Password) {
        identify(this.password == password) { "사용자 정보가 일치하지 않습니다." }
    }

    fun resetPassword(
        name: String,
        birthday: LocalDate,
        password: String,
    ) {
        identify(information.same(name, birthday)) { "사용자 정보가 일치하지 않습니다." }
        this.password = Password(password)
        registerEvent(PasswordResetEvent(id, name, email, password))
    }

    fun changePassword(
        oldPassword: Password,
        newPassword: Password,
    ) {
        identify(this.password == oldPassword) { "기존 비밀번호가 일치하지 않습니다." }
        this.password = newPassword
    }

    fun changePhoneNumber(phoneNumber: String) {
        information.phoneNumber = phoneNumber
    }

    fun withdraw(password: Password) {
        identify(this.password == password) { "사용자 정보가 일치하지 않습니다." }
        information.member = null
        _information = null
    }

    private fun identify(
        value: Boolean,
        lazyMessage: () -> Any = {},
    ) {
        if (!value) {
            val message = lazyMessage()
            throw UnidentifiedMemberException(message.toString())
        }
    }
}
