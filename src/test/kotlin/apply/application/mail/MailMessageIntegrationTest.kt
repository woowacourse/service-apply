package apply.application.mail

import apply.createMailData
import apply.domain.mail.MailMessageRepository
import apply.domain.mail.getOrThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.transaction.annotation.Transactional
import support.test.IntegrationTest
import java.time.LocalDateTime.now

@Transactional
@IntegrationTest
class MailMessageIntegrationTest(
    private val mailMessageService: MailMessageService,
    private val mailMessageRepository: MailMessageRepository
) : BehaviorSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    Given("예약하고 싶은 메일 내용이 있는 경우") {
        val reservationTime = now().plusHours(3).withMinute(10)
        val mailData = createMailData(sentTime = reservationTime)

        When("메일을 예약하면") {
            val mailMessage = mailMessageService.reserve(mailData)

            Then("메일 메시지와 예약이 생성된다") {
                val actual = mailMessageRepository.getOrThrow(mailMessage.id)

                actual.subject shouldBe mailData.subject
                actual.reservation().shouldNotBeNull()
                actual.reservation()!!.reservationTime shouldBe reservationTime
            }
        }
    }

    Given("메일 예약 후 발송 작업이 아직 시작되지 않았을 경우") {
        val reservationTime = now().plusHours(3).withMinute(10)
        val mailData = createMailData(sentTime = reservationTime)
        val mailMessage = mailMessageService.reserve(mailData)

        When("메일 내용을 변경하면") {
            val updatedMailData = createMailData(
                subject = "변경된 제목",
                body = "변경된 본문",
                recipients = listOf("변경된 수신자")
            )
            mailMessageService.updateReservedMessage(mailMessage.id, updatedMailData)

            Then("변경된 내용이 반영된다") {
                val actual = mailMessageRepository.getOrThrow(mailMessage.id)

                actual.subject shouldBe updatedMailData.subject
                actual.body shouldBe updatedMailData.body
                actual.recipients shouldBe updatedMailData.recipients
                actual.reservation().shouldNotBeNull()
                actual.reservation()!!.reservationTime shouldBe reservationTime
            }
        }
    }
})
