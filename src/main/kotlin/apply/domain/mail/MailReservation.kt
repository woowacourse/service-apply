package apply.domain.mail

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.ForeignKey
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

private const val PERIOD_MINUTES: Long = 10L

@Entity
class MailReservation(

    @ManyToOne
    @JoinColumn(
        name = "mail_message_id", nullable = false,
        foreignKey = ForeignKey(name = "fk_mail_reservation_to_mail_message")
    )
    val mailMessage: MailMessage,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MailReservationStatus = MailReservationStatus.WAITING,

    @Column(nullable = false)
    val creatorId: Long,

    @Column(nullable = false)
    var reservationTime: LocalDateTime,
    id: Long = 0L
) : BaseEntity(id) {

    init {
        validateTime(reservationTime)
    }

    fun process() {
        status = MailReservationStatus.SENDING
    }

    fun complete() {
        status = MailReservationStatus.FINISHED
    }

    fun update(reservationTime: LocalDateTime) {
        validateTime(reservationTime)
        validateStatus()

        this.reservationTime = reservationTime
    }

    fun validateStatus() {
        check(status == MailReservationStatus.WAITING) {
            "메일 예약은 WAITING 상태에서만 가능합니다."
        }
    }

    private fun validateTime(reservationTime: LocalDateTime) {
        require(reservationTime.isAfter(LocalDateTime.now())) {
            "예약 메일의 예약시간은 현재시간보다 늦은 시간이어야 합니다."
        }

        // TODO: validator 추출
        require(reservationTime.minute % PERIOD_MINUTES == 0L) {
            "예약 메일의 예약시간은 10분 단위로 설정해야 합니다."
        }
    }
}

enum class MailReservationStatus {
    WAITING, SENDING, FINISHED
}
