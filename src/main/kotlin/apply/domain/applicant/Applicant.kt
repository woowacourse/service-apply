package apply.domain.applicant

import apply.domain.user.Gender
import apply.domain.user.User
import support.domain.BaseEntity
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Applicant(
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    id: Long = 0L
) : BaseEntity(id) {
    val name: String
        get() = user.information.name

    val email: String
        get() = user.information.email

    val phoneNumber: String
        get() = user.information.phoneNumber

    val gender: Gender
        get() = user.information.gender

    val birthday: LocalDate
        get() = user.information.birthday
}
