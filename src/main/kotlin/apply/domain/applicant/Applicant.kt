package apply.domain.applicant

import apply.domain.user.Gender
import apply.domain.user.User
import apply.domain.user.UserInformation
import support.domain.BaseEntity
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class Applicant(
    @Embedded
    val information: UserInformation,

    @Column(nullable = false)
    val userId: Long = 0L,
    id: Long = 0L
) : BaseEntity(id) {
    val name: String
        get() = information.name

    val email: String
        get() = information.email

    val phoneNumber: String
        get() = information.phoneNumber

    val gender: Gender
        get() = information.gender

    val birthday: LocalDate
        get() = information.birthday

    constructor(
        user: User,
        id: Long = 0L
    ) : this(
        user.information, user.id, id
    )
}
