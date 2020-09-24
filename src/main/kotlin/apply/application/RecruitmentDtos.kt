package apply.application

import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RecruitmentRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotNull
    var startDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotNull
    var endDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotNull
    var canRecruit: Boolean = false,

    @field:NotNull
    var isHidden: Boolean = true,

    @field:NotNull
    @field:Valid
    var recruitmentItems: List<RecruitmentItemRequest> = emptyList()
)

data class RecruitmentItemRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 255)
    var title: String = "",

    @field:NotNull
    @field:Min(1)
    @field:Max(10)
    var position: Int = 0,

    @field:NotNull
    @field:Min(1)
    @field:Max(10000)
    var maximumLength: Int = 0,

    @field:NotBlank
    var description: String = ""
)
