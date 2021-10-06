package apply.domain.email

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column

// TODO
// @Entity
class EmailHistory(
    @Column(nullable = false)
    val subject: String,

    @Column(nullable = false)
    val body: String,

    @Column(nullable = false)
    val sender: String,

    @Column(nullable = false)
    val recipients: String,

    @Column(nullable = false)
    val sentTime: LocalDateTime = LocalDateTime.now(),

    id: Long = 0L
) : BaseEntity(id)
