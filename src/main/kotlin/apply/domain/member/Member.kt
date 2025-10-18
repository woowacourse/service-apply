package apply.domain.member

import support.domain.BaseRootEntity
import support.infra.PersistenceOnly
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToOne

@Entity
class Member(
    information: MemberInformation,

    @AttributeOverride(name = "value", column = Column(name = "password", nullable = false))
    @Embedded
    var password: Password,
    authorizationRequirement: AuthorizationRequirement,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.ACTIVE,
    id: Long = 0L,
) : BaseRootEntity<Member>(id) {
    @OneToOne(mappedBy = "member", cascade = [CascadeType.PERSIST], orphanRemoval = true)
    private var _information: MemberInformation? = null
    val information: MemberInformation get() = _information ?: throw IllegalStateException("회원 정보가 존재하지 않습니다.")

    val email: String get() = information.email
    val name: String get() = information.name
    val birthday: LocalDate get() = information.birthday
    val phoneNumber: String get() = information.phoneNumber
    val githubUsername: String get() = information.githubUsername

    init {
        authorizationRequirement.require(information)
        attachInformation(information)
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
        registerEvent(PasswordResetEvent(id, name, information.email, password))
    }

    fun changePassword(oldPassword: Password, newPassword: Password) {
        identify(password == oldPassword) { "기존 비밀번호가 일치하지 않습니다." }
        password = newPassword
    }

    fun changePhoneNumber(phoneNumber: String) {
        information.phoneNumber = phoneNumber
    }

    fun withdraw(password: Password) {
        identify(this.password == password) { "사용자 정보가 일치하지 않습니다." }
        status = MemberStatus.WITHDRAWN
        detachInformation()
    }

    private fun identify(value: Boolean, lazyMessage: () -> Any) {
        if (!value) {
            val message = lazyMessage()
            throw UnidentifiedMemberException(message.toString())
        }
    }

    @PersistenceOnly
    private fun attachInformation(information: MemberInformation) {
        _information = information
        information.member = this
    }

    @PersistenceOnly
    private fun detachInformation() {
        _information?.member = null
        _information = null
    }
}
