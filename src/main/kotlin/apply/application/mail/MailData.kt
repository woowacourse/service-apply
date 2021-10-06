package apply.application.mail

import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class MailData(
    @field:NotEmpty
    var subject: String = "",

    @field:NotEmpty
    var body: String = "",

    @field:NotEmpty
    var recipients: List<String> = emptyList()
)

//todo
data class MailHistoryData(
    @field:NotEmpty
    var subject: String = "",

    @field:NotEmpty
    var body: String = "",

    @field:NotEmpty
    var recipients: List<String> = emptyList(),

    @field:NotNull
    var sentTime: LocalDateTime,

    @field:NotNull
    val id: Long
) {
    fun recipientsCount(): Int {
        return recipients.size
    }
}
