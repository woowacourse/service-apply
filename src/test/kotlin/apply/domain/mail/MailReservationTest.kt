package apply.domain.mail

import apply.createMailReservation
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime.now

class MailReservationTest : StringSpec({
    "메일 예약을 생성한다" {
        val mailReservation = createMailReservation()

        mailReservation shouldNotBe null
        mailReservation.status shouldBe MailReservationStatus.WAITING
    }

    "예약 메일의 예약 시간은 10분 단위로 설정할 수 있다" {
        val future = now().plusHours(1)
        val mailReservation = createMailReservation(reservationTime = future.withMinute(30))

        mailReservation shouldNotBe null
    }

    "예약 메일의 예약 시간이 10분 단위가 아닐 경우 예약이 불가능하다" {
        val future = now().plusHours(1)

        listOf(future.withMinute(7), future.withMinute(23)).forAll { reservationTime ->
            shouldThrow<IllegalArgumentException> {
                createMailReservation(reservationTime = reservationTime)
            }
        }
    }

    "예약 메일의 예약 시간이 과거라면 예약이 불가능하다" {
        val past = now().minusHours(2)
        shouldThrow<IllegalArgumentException> {
            createMailReservation(reservationTime = past.withMinute(0))
        }
    }

    "예약 메일 전송을 시작한다" {
        val mailReservation = createMailReservation()

        mailReservation.process()
        mailReservation.status shouldBe MailReservationStatus.SENDING
    }

    "예약 메일 전송을 완료한다" {
        val mailReservation = createMailReservation()

        mailReservation.complete()
        mailReservation.status shouldBe MailReservationStatus.FINISHED
    }

    "예약 메일의 예약 시간을 변경한다" {
        val mailReservation = createMailReservation()

        val updatedReservationTime = now().plusHours(1).withMinute(0)
        mailReservation.update(updatedReservationTime)

        mailReservation.reservationTime shouldBe updatedReservationTime
    }

    "예약 메일 전송이 시작거나 완료 되었다면 예약 시간을 변경할 수 없다" {
        listOf(
            createMailReservation().apply { process() },
            createMailReservation().apply { complete() }
        ).forAll { mailReservation ->
            shouldThrow<IllegalStateException> {
                mailReservation.update(now().plusHours(1).withMinute(0))
            }
        }
    }
})
