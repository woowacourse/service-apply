package apply.application

import apply.domain.administrator.Administrator
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class AdministratorData(
    @field:Pattern(
        regexp = "[a-z\\d가-힣]{1,30}", message = "올바른 형식의 이름이어야 합니다",
        flags = [Pattern.Flag.CASE_INSENSITIVE]
    )
    var name: String = "",

    @field:NotBlank
    var username: String = "",

    @field:NotBlank
    var password: String = "",

    @field:NotBlank
    var confirmPassword: String = ""
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
