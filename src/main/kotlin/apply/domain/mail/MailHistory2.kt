package apply.domain.mail

import support.domain.BaseEntity
import support.domain.StringToListConverter
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
class MailHistory2 private constructor(
    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = ForeignKey(name = "fk_mail_history_to_mail_message"))
    val mailMessage: MailMessage,

    @Column(nullable = false)
    @Convert(converter = StringToListConverter::class)
    @Lob
    val recipients: List<String>,

    @Column(nullable = false)
    val success: Boolean,

    @Column(nullable = false)
    val sentTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
) : BaseEntity(id) {
    companion object {
        fun ofSuccess(mailMessage: MailMessage, recipients: List<String>): MailHistory2 {
            return MailHistory2(mailMessage, recipients, true)
        }

        fun ofFailure(mailMessage: MailMessage, recipients: List<String>): MailHistory2 {
            return MailHistory2(mailMessage, recipients, false)
        }
    }
}
