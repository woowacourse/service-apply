package apply.domain.applicant

import support.domain.BaseEntity
import java.time.LocalDate
import javax.persistence.AttributeOverride
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class Applicant(
    @Embedded
    val information: ApplicantInformation,

    @AttributeOverride(name = "value", column = Column(name = "password", nullable = false))
    @Embedded
    var password: Password,
    id: Long = 0L
) : BaseEntity(id) {
    @Transient
    val name: String = information.name

    @Transient
    val email: String = information.email

    @Transient
    val phoneNumber: String = information.phoneNumber

    @Transient
    val gender: Gender = information.gender

    @Transient
    val birthday: LocalDate = information.birthday

    constructor(
        name: String,
        email: String,
        phoneNumber: String,
        gender: Gender,
        birthday: LocalDate,
        password: Password,
        id: Long = 0L
    ) : this(
        ApplicantInformation(name, email, phoneNumber, gender, birthday), password, id
    )

    fun verify(password: Password, information: ApplicantInformation) {
        if (!samePassword(password) || this.information != information) {
            throw ApplicantValidateException()
        }
    }

    fun changePassword(password: Password, newPassword: Password) {
        if (!samePassword(password)) {
            throw ApplicantValidateException()
        }
        this.password = newPassword
    }

    fun samePassword(password: Password): Boolean = this.password == password

    fun resetPassword(name: String, birthday: LocalDate, password: String) {
        if (!information.same(name, birthday)) {
            throw ApplicantValidateException()
        }
        this.password = Password(password)
    }
}
