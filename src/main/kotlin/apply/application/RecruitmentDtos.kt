package apply.application

import apply.domain.answer.Answer
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.*

data class RecruitmentRequest(
        @field:NotBlank
        @field:Size(min = 1, max = 31)
        var title: String = "",

        @field:NotNull
        var startDateTime: LocalDateTime = LocalDateTime.MIN,

        @field:NotNull
        var endDateTime: LocalDateTime = LocalDateTime.MIN,

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

data class ApplicationFormRequest(
        @field:Size(min = 0, max = 255)
        val referenceUrl: String,

        @field:NotNull
        val isSubmit: Boolean,

        @field:NotBlank
        val answers: List<Answer> = ArrayList()
)
