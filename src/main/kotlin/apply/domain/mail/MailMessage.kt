package apply.domain.mail

import support.domain.BaseEntity
import support.domain.StringToListConverter
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Lob
import javax.persistence.OneToMany

@Entity
class MailMessage private constructor(
    @Column(nullable = false)
    var subject: String,

    @Column(nullable = false)
    @Lob
    var body: String,

    @Column(nullable = false)
    var sender: String,

    @Column(nullable = false)
    @Convert(converter = StringToListConverter::class)
    @Lob
    var recipients: List<String>,

    @Column(nullable = false)
    val creatorId: Long,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    modifiedDateTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
) : BaseEntity(id) {

    @OneToMany(mappedBy = "mailMessage", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var reservations: MutableList<MailReservation> = mutableListOf()

    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = modifiedDateTime
        private set

    fun reservation(): MailReservation? {
        if (!hasReservation()) {
            return null
        }

        return reservations.first()
    }

    fun update(subject: String, body: String, recipients: List<String>) {
        check(hasReservation())
        reservation()?.validateStatus()

        this.subject = subject
        this.body = body
        this.recipients = recipients
        this.modifiedDateTime = LocalDateTime.now()
    }

    private fun hasReservation(): Boolean {
        return reservations.isNotEmpty()
    }

    companion object {
        fun of(
            subject: String,
            body: String,
            sender: String,
            recipients: List<String>,
            creatorId: Long
        ): MailMessage {
            return MailMessage(
                subject = subject,
                body = body,
                sender = sender,
                recipients = recipients,
                creatorId = creatorId
            )
        }

        fun withReservation(
            subject: String,
            body: String,
            sender: String,
            recipients: List<String>,
            reservationTime: LocalDateTime,
            creatorId: Long
        ): MailMessage {
            return MailMessage(
                subject = subject,
                body = body,
                sender = sender,
                recipients = recipients,
                creatorId = creatorId
            ).apply {
                reservations.add(
                    MailReservation(this, creatorId = creatorId, reservationTime = reservationTime)
                )
            }
        }
    }
}
