package apply.domain.member.tobe

import apply.domain.member.AuthorizationRequirement
import apply.domain.member.Password
import support.domain.BaseRootEntity
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
    var information: MemberInformation? = null
        private set

    init {
        attachInformation(information)
    }

    fun changePassword(oldPassword: Password, newPassword: Password) {
        this.password = newPassword
    }

    fun changePhoneNumber(phoneNumber: String) {
        information?.phoneNumber = phoneNumber
    }

    fun withdraw(password: Password) {
        detachInformation()
    }

    private fun attachInformation(information: MemberInformation) {
        this.information = information
        information.member = this
    }

    private fun detachInformation() {
        information?.member = null
        information = null
    }

    init {
        // authorizationRequirement.require(information)
    }
}
