package apply.domain.mail

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

const val MAIL_RESERVATION_PERIOD_MINUTES: Long = 15

@Entity
class MailReservation(
    val mailMessageId: Long = 0L,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MailReservationStatus = MailReservationStatus.WAITING,

    @Column(nullable = false)
    val reservationTime: LocalDateTime,
    id: Long = 0L
) : BaseEntity(id) {

    init {
        validateTime(reservationTime)
    }

    fun send() {
        status = MailReservationStatus.SENDING
    }

    fun finish() {
        status = MailReservationStatus.FINISHED
    }

    fun canCancel(): Boolean {
        return status == MailReservationStatus.WAITING
    }

    private fun validateTime(reservationTime: LocalDateTime) {
        require(reservationTime.isAfter(LocalDateTime.now())) {
            "예약 메일의 예약시간은 현재시간보다 늦은 시간이어야 합니다."
        }

        // TODO: validator 추출
        require(reservationTime.minute % MAIL_RESERVATION_PERIOD_MINUTES == 0L) {
            "예약 메일의 예약시간은 ${MAIL_RESERVATION_PERIOD_MINUTES}분 단위로 설정해야 합니다."
        }
    }
}
