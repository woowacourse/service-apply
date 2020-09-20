package apply.domain.applicant

import apply.application.ApplicantRequest
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
    fun validate(password: String, name: String, phoneNumber: String, gender: Gender, birthday: LocalDate) {
        if (this.password != password) {
            throw ApplicantValidateException("비밀번호")
        }
        if (this.name != name) {
            throw ApplicantValidateException("이름")
        }
        if (this.phoneNumber != phoneNumber) {
            throw ApplicantValidateException("전화번호")
        }
        if (this.gender != gender) {
            throw ApplicantValidateException("성별")
        }
        if (this.birthday != birthday) {
            throw ApplicantValidateException("생일")
        }
    }

    fun validate(applicantRequest: ApplicantRequest) =
        validate(
            password = applicantRequest.password,
            name = applicantRequest.name,
            phoneNumber = applicantRequest.phoneNumber,
            gender = applicantRequest.gender,
            birthday = applicantRequest.birthday
        )
}
