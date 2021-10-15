package apply.domain.applicationform

import java.time.LocalDateTime

data class ApplicationFormSubmittedEvent(
    val applicationFormId: Long,
    val userId: Long,
    val recruitmentId: Long,
    val occurredOn: LocalDateTime = LocalDateTime.now()
)
