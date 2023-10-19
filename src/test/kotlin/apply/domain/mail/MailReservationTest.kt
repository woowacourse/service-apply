package apply.domain.mail

import apply.createAvailableReservationTime
import apply.createMailReservation
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class MailReservationTest : StringSpec({
    "메일 예약을 생성한다" {
        val mailReservation = createMailReservation()

        mailReservation shouldNotBe null
        mailReservation.status shouldBe MailReservationStatus.WAITING
    }

    "예약 메일의 예약 시간은 15분 단위로 설정할 수 있다" {
        val future = createAvailableReservationTime()
        val mailReservation = createMailReservation(reservationTime = future)

        mailReservation shouldNotBe null
        mailReservation.reservationTime shouldBe future
    }

    "예약 메일의 예약 시간이 15분 단위가 아닐 경우 예약이 불가능하다" {
        val future = createAvailableReservationTime()

        listOf(future.withMinute(7), future.withMinute(23)).forAll { reservationTime ->
            shouldThrow<IllegalArgumentException> {
                createMailReservation(reservationTime = reservationTime)
            }
        }
    }

    "예약 메일의 예약 시간이 과거라면 예약이 불가능하다" {
        val past = createAvailableReservationTime().minusDays(1)
        shouldThrow<IllegalArgumentException> {
            createMailReservation(reservationTime = past.withMinute(0))
        }
    }

    "예약 메일 전송을 시작한다" {
        val mailReservation = createMailReservation()

        mailReservation.send()
        mailReservation.status shouldBe MailReservationStatus.SENDING
    }

    "예약 메일 전송을 완료한다" {
        val mailReservation = createMailReservation()

        mailReservation.finish()
        mailReservation.status shouldBe MailReservationStatus.FINISHED
    }
})
