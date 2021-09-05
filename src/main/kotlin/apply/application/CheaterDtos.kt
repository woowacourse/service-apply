package apply.application

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CheaterData(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    var description: String
)