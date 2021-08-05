package apply.domain.applicant

import support.domain.BaseEntity
import java.time.LocalDate
import java.util.UUID
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
    id: Long = 0L,
    authenticated: Boolean = false
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

    @Column(nullable = false)
    var authenticated: Boolean = authenticated
        private set

    @Column(nullable = false)
    val authenticateCode: String = UUID.randomUUID().toString().take(8)

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

    fun authenticate(applicant: Applicant) {
        authenticate(applicant.password)
        identify(this.information == applicant.information)
    }

    fun authenticate(password: Password) {
        identify(this.password == password)
    }

    fun changePassword(oldPassword: Password, newPassword: Password) {
        authenticate(oldPassword)
        this.password = newPassword
    }

    fun resetPassword(name: String, birthday: LocalDate, password: String) {
        identify(information.same(name, birthday))
        this.password = Password(password)
    }

    private fun identify(value: Boolean) {
        if (!value) {
            throw ApplicantAuthenticationException()
        }
    }

    fun authenticateEmail(authenticateCode: String) {
        if (this.authenticated) {
            throw ApplicantAuthenticationException("이미 이메일이 인증된 지원자 입니다.")
        }
        identify(this.authenticateCode == authenticateCode)
        authenticated = true
    }
}
