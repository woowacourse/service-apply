package apply.application.mail

import apply.config.TestMailConfiguration
import apply.createAvailableReservationTime
import apply.createMailMessage
import apply.createMailReservation
import apply.domain.mail.MailMessageRepository
import apply.domain.mail.MailReservationRepository
import apply.domain.mail.MailReservationStatus
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import support.test.IntegrationTest

@Import(TestMailConfiguration::class)
@Transactional
@IntegrationTest
class MailReservationIntegrationTest(
    private val mailReservationService: MailReservationService,
    private val mailReservationRepository: MailReservationRepository,
    private val mailMessageRepository: MailMessageRepository
) : BehaviorSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    Given("특정 시간에 발송할 예약 메일이 있는 경우") {
        val reservationTime = createAvailableReservationTime()
        val mailMessage1 = mailMessageRepository.save(createMailMessage())
        val mailMessage2 = mailMessageRepository.save(createMailMessage())

        mailReservationRepository.save(createMailReservation(mailMessage1.id, reservationTime))
        mailReservationRepository.save(createMailReservation(mailMessage2.id, reservationTime.plusHours(3)))

        When("해당 시간에 메일 발송 요청을 하면") {
            mailReservationService.sendMail(reservationTime)

            Then("메일 전송이 완료된다") {
                val actual = mailReservationRepository.findByStatus(MailReservationStatus.FINISHED)
                actual.size shouldBe 1
            }
        }
    }
})
