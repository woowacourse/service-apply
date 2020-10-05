package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import apply.domain.applicationform.ApplicationForm
import apply.domain.evaluationtarget.EvaluationTarget
import java.time.LocalDate

data class ApplicantBasicResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate
) {
    constructor(applicant: Applicant) : this(
        applicant.id,
        applicant.name,
        applicant.email,
        applicant.phoneNumber,
        applicant.gender,
        applicant.birthday
    )
}

data class ApplicantResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val gender: Gender,
    val birthday: LocalDate,
    var isCheater: Boolean,
    val applicationForm: ApplicationForm
) {
    constructor(applicant: Applicant, isCheater: Boolean, applicationForm: ApplicationForm) : this(
        applicant.id,
        applicant.name,
        applicant.email,
        applicant.phoneNumber,
        applicant.gender,
        applicant.birthday,
        isCheater,
        applicationForm
    )
}

data class EvaluationTargetResponse(
    val id: Long,
    val name: String,
    val email: String,
    val target: EvaluationTarget
)
