package apply.domain.mail

import java.time.LocalDateTime

private const val PERIOD_MINUTES: Long = 10L

class MailReservation(
    val mailMessage: MailMessage,
    var status: MailReservationStatus = MailReservationStatus.WAITING,
    val creatorId: Long,
    val reservationTime: LocalDateTime
) {

    init {
        require(reservationTime.isAfter(LocalDateTime.now())) {
            "예약 메일의 예약시간은 현재시간보다 늦은 시간이어야 합니다."
        }

        // TODO: validator 추출
        require(reservationTime.minute % PERIOD_MINUTES == 0L) {
            "예약 메일의 예약시간은 10분 단위로 설정해야 합니다."
        }
    }

    fun process() {
        status = MailReservationStatus.SENDING
    }

    fun complete() {
        status = MailReservationStatus.FINISHED
    }
}

enum class MailReservationStatus {
    WAITING, SENDING, FINISHED
}
