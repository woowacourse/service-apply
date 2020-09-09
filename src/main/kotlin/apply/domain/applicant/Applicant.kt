package apply.domain.applicant

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Applicant(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var phoneNumber: String,

    @Column(nullable = false)
    var gender: String,

    @Column(nullable = false)
    var birthday: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    constructor(name: String, email: String, phoneNumber: String, gender: Gender, birthday: String) : this(
        name,
        email,
        phoneNumber,
        gender.korean,
        birthday
    )
}
