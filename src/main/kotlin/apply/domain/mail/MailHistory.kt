package apply.domain.mail

import support.domain.BaseEntity
import support.domain.StringToListConverter
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class MailHistory(
    @Column(nullable = false)
    val subject: String,

    @Column(nullable = false)
    @Lob
    val body: String,

    @Column(nullable = false)
    val sender: String,

    @Column(nullable = false)
    @Convert(converter = StringToListConverter::class)
    @Lob
    val recipients: List<String>,

    @Column(nullable = false)
    val sentTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
) : BaseEntity(id)
