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
})
