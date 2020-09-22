package apply.domain.applicant

import apply.domain.applicant.exception.ApplicantValidateException
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Applicant(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val phoneNumber: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(nullable = false)
    val birthday: LocalDate,

    @Column(nullable = false)
    val password: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    fun validate(targetInformation: ApplicantInformation) {
        if (getInformation() != targetInformation) {
            throw ApplicantValidateException()
        }
    }

    private fun getInformation() = ApplicantInformation(
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        gender = gender,
        birthday = birthday,
        password = password
    )
}
