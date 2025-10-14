package apply.domain.member.tobe

import support.infra.PersistenceOnly
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_member_information", columnNames = ["email"])]
)
@Entity
class MemberInformation(
    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, length = 30)
    val name: String,

    @Column(nullable = false)
    val birthday: LocalDate,

    @Column(nullable = false, length = 13)
    var phoneNumber: String,

    @Column(nullable = false, length = 39)
    val githubUsername: String,
) {
    @PersistenceOnly
    @Id
    private val memberId: Long = 0L

    @PersistenceOnly
    @MapsId
    @OneToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_information_member_id_ref_member_id"))
    internal var member: Member? = null

    fun same(name: String, birthday: LocalDate): Boolean {
        return this.name == name && this.birthday == birthday
    }
}
