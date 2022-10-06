package apply.application

import apply.domain.administrator.Administrator
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class CreateAdministratorFormData(
    @field:Pattern(
        regexp = "[a-z\\d가-힣]{1,30}", message = "올바른 형식의 이름이어야 합니다",
        flags = [Pattern.Flag.CASE_INSENSITIVE]
    )
    var name: String = "",

    @field:NotBlank
    @field:Size(min = 1, max = 30)
    var username: String = "",

    @field:NotBlank
    var password: String = "",

    @field:NotBlank
    var confirmPassword: String = ""
)

data class UpdateAdministratorFormData(
    val id: Long = 0L,

    @field:Size(min = 1, max = 20)
    @field:Pattern(
        regexp = "[0-9a-zA-Z가-힣]+",
        message = "이름은 영어, 한글, 숫자만 가능합니다."
    )
    var name: String = "",

    @field:NotBlank
    var password: String = "",

    @field:NotBlank
    var passwordConfirmation: String = ""
)

data class AdministratorResponse(
    val id: Long,
    val name: String,
    val username: String,
    val password: String
) {
    constructor(administrator: Administrator) : this(
        administrator.id,
        administrator.name,
        administrator.username,
        administrator.password
    )
}
