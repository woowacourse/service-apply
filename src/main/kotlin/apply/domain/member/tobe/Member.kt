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
    @OneToOne(mappedBy = "member", cascade = [CascadeType.PERSIST])
    var information: MemberInformation? = null
        private set

    init {
        attach(information)
    }

    private fun attach(information: MemberInformation) {
        this.information = information
        information.member = this
    }

    init {
        // authorizationRequirement.require(information)
    }
}
