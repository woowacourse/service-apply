package apply.domain.member.tobe

import apply.domain.member.AuthorizationRequirement
import apply.domain.member.Password
import apply.domain.member.PasswordResetEvent
import apply.domain.member.UnidentifiedMemberException
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: MemberStatus = MemberStatus.ACTIVE,
    authorizationRequirement: AuthorizationRequirement,
) : BaseRootEntity<Member>() {
    @OneToOne(mappedBy = "member", cascade = [CascadeType.PERSIST], orphanRemoval = true)
    private var _information: MemberInformation? = null
    val information: MemberInformation
        get() = _information ?: throw IllegalStateException("회원 정보가 존재하지 않습니다.")

    init {
        // authorizationRequirement.require(information)
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
        this.password = newPassword
    }

    fun changePhoneNumber(phoneNumber: String) {
        _information?.phoneNumber = phoneNumber
    }

    fun withdraw(password: Password) {
        detachInformation()
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
