package apply.domain.member

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_member_information", columnNames = ["email"])]
)
@Entity
data class MemberInformation(
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
    @OneToOne
    @JoinColumn(nullable = false, foreignKey = ForeignKey(name = "fk_member_information_member_id_ref_member_id"))
    var member: Member? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L

    fun same(name: String, birthday: LocalDate): Boolean {
        return this.name == name && this.birthday == birthday
    }

    companion object {
        val DELETED: MemberInformation = MemberInformation(
            email = "ghost@email.com",
            name = """👻""",
            birthday = LocalDate.EPOCH,
            phoneNumber = "010-0000-0000",
            githubUsername = "ghost"
        )
    }
}
