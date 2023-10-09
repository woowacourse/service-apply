package apply.domain.mail

import apply.createMailMessage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MailMessageTest : StringSpec({
    "즉시 발송하는 메일 메시지를 생성한다" {
        val mailMessage = createMailMessage(subject = "제목", body = "내용")

        mailMessage.subject shouldBe "제목"
        mailMessage.body shouldBe "내용"
        mailMessage.reservation() shouldBe null
    }
})
