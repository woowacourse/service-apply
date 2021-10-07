package apply.application.mail

import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class SimpleMailData(
    @field:NotEmpty
    var subject: String = "",

    @field:NotNull
    var recipientsCount: Int = 0,

    @field:NotNull
    var sentTime: LocalDateTime = LocalDateTime.now(),

    @field:NotNull
    var id: Long = 0L
)

//todo
data class MailData(
    @field:NotEmpty
    var subject: String = "",

    @field:NotEmpty
    var body: String = "",

    @field:NotEmpty
    var sender: String = "",

    @field:NotEmpty
    var recipients: List<String> = emptyList(),

    @field:NotNull
    var sentTime: LocalDateTime = LocalDateTime.now(),

    @field:NotNull
    var id: Long = 0L
) {
    fun recipientsCount(): Int {
        return recipients.size
    }

    fun recipientsToString(): String {
        return recipients.joinToString(",")
    }
}
