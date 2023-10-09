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
    val mailMessageId: Long = 0L,

    @Column(nullable = false)
    @Convert(converter = StringToListConverter::class)
    @Lob
    val recipients: List<String>,

    @Column(nullable = false)
    val success: Boolean,

    @Column(nullable = false)
    val sentTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
) : BaseEntity(id)
