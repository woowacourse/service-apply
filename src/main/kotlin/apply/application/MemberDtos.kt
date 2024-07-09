package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.member.Member
import apply.domain.member.Password
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Past
import javax.validation.constraints.Pattern

data class MemberResponse(
    val id: Long,
    val email: String,
    val name: String,
    val birthday: LocalDate,
    val phoneNumber: String,
    val githubUsername: String,
) {
    constructor(member: Member) : this(
        member.id,
        member.email,
        member.name,
        member.birthday,
        member.phoneNumber,
        member.githubUsername,
    )
}

data class ApplicantAndFormResponse(
    val id: Long,
    val email: String,
    val name: String,
    val birthday: LocalDate,
    val phoneNumber: String,
    val githubUsername: String,
    val isCheater: Boolean,
    val applicationForm: ApplicationForm,
) {
    constructor(member: Member, isCheater: Boolean, applicationForm: ApplicationForm) : this(
        member.id,
        member.email,
        member.name,
        member.birthday,
        member.phoneNumber,
        member.githubUsername,
        isCheater,
        applicationForm,
    )
}

data class RegisterMemberRequest(
    @field:Email
    val email: String,
    val password: Password,
    val confirmPassword: Password,

    @field:Pattern(regexp = "[가-힣]{1,30}", message = "올바른 형식의 이름이어야 합니다")
    val name: String,

    @field:Past
    val birthday: LocalDate,

    @field:Pattern(regexp = "010-\\d{4}-\\d{4}", message = "올바른 형식의 휴대전화 번호여야 합니다")
    val phoneNumber: String,

    @field:Pattern(
        regexp = "^[a-z\\d](?:[a-z\\d]|-(?=[a-z\\d])){0,38}",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "올바른 형식의 이름이어야 합니다"
    )
    val githubUsername: String,

    @field:NotBlank
    val authenticationCode: String,
)

data class AuthenticateMemberRequest(
    @field:Email
    val email: String,
    val password: Password
)

data class ResetPasswordRequest(
    @field:Pattern(regexp = "[가-힣]{1,30}", message = "올바른 형식의 이름이어야 합니다")
    val name: String,

    @field:Email
    val email: String,

    @field:Past
    val birthday: LocalDate
)

data class EditPasswordRequest(
    val oldPassword: Password,
    val password: Password,
    val confirmPassword: Password
)

data class EditInformationRequest(
    @field:Pattern(regexp = "010-\\d{4}-\\d{4}", message = "올바른 형식의 휴대전화 번호여야 합니다")
    val phoneNumber: String
)
