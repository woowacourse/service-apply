package apply.domain.mail

import apply.createMailMessage
import apply.createReservationMailMessage
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class MailMessageTest : StringSpec({
    "즉시 발송하는 메일 메시지를 생성한다" {
        val mailMessage = createMailMessage(subject = "제목", body = "내용")

        mailMessage.subject shouldBe "제목"
        mailMessage.body shouldBe "내용"
        mailMessage.reservation() shouldBe null
    }

    "예약 메일 메시지를 생성한다" {
        val mailMessage = createReservationMailMessage(subject = "제목", body = "내용")

        mailMessage.subject shouldBe "제목"
        mailMessage.body shouldBe "내용"
        mailMessage.reservation() shouldNotBe null
    }

    "예약 메일 메시지의 내용을 수정한다" {
        val mailMessage = createReservationMailMessage(subject = "제목", body = "내용")

        mailMessage.update("수정된 제목", "수정된 내용", mailMessage.recipients)

        mailMessage.subject shouldBe "수정된 제목"
        mailMessage.body shouldBe "수정된 내용"
        mailMessage.reservation() shouldNotBe null
    }

    "일반 메일 메시지의 내용은 수정할 수 없다" {
        val mailMessage = createMailMessage()

        shouldThrow<IllegalStateException> {
            mailMessage.update("수정된 제목", "수정된 내용", mailMessage.recipients)
        }
    }

    "발송 이후 상태인 예약 메일 메시지의 내용은 수정할 수 없다" {
        val mailMessage = createReservationMailMessage()
        mailMessage.reservation()?.process()

        shouldThrow<IllegalStateException> {
            mailMessage.update("수정된 제목", "수정된 내용", mailMessage.recipients)
        }
    }
})
