package apply.application

import apply.domain.administrator.Administrator
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class AdministratorData(
    @field:NotBlank
    @field:Size(min = 1, max = 20)
    @field:Pattern(
        regexp = "[0-9a-zA-Z가-힣]+",
        message = "이름은 영어, 한글, 숫자만 가능합니다."
    )
    var name: String = "",

    @field:NotBlank
    var username: String = "",

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
