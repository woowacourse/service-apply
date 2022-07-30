package apply.application

import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentStatus
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.term.Term
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RecruitmentData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotNull
    var term: TermData = TermData(),

    @field:NotNull
    var startDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotNull
    var endDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotNull
    var recruitable: Boolean = false,

    @field:NotNull
    var hidden: Boolean = true,

    @field:NotNull
    @field:Valid
    var recruitmentItems: List<RecruitmentItemData> = emptyList(),
    var id: Long = 0L
) {
    constructor(recruitment: Recruitment, term: Term, recruitmentItems: List<RecruitmentItem>) : this(
        recruitment.title,
        TermData(term),
        recruitment.startDateTime,
        recruitment.endDateTime,
        recruitment.recruitable,
        recruitment.hidden,
        recruitmentItems.map(::RecruitmentItemData),
        recruitment.id
    )
}

data class RecruitmentItemData(
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
    var description: String = "",
    var id: Long = 0L
) {
    constructor(recruitmentItem: RecruitmentItem) : this(
        recruitmentItem.title,
        recruitmentItem.position,
        recruitmentItem.maximumLength,
        recruitmentItem.description,
        recruitmentItem.id
    )
}

data class RecruitmentResponse(
    val id: Long,
    val title: String,
    val term: TermResponse,
    val recruitable: Boolean,
    val hidden: Boolean,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: RecruitmentStatus
) {
    constructor(recruitment: Recruitment, term: Term) : this(
        recruitment.id,
        recruitment.title,
        TermResponse(term),
        recruitment.recruitable,
        recruitment.hidden,
        recruitment.startDateTime,
        recruitment.endDateTime,
        recruitment.status
    )
}
