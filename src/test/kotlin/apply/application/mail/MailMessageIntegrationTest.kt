package apply.application.mail

import apply.createMailData
import apply.createReservationMailMessage
import apply.domain.mail.MailMessageRepository
import apply.domain.mail.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.transaction.annotation.Transactional
import support.test.IntegrationTest
import java.time.LocalDateTime.now

@Transactional
@IntegrationTest
class MailMessageIntegrationTest(
    private val mailMessageService: MailMessageService,
    private val mailMessageRepository: MailMessageRepository,
    private val mailReservationRepository: MailMessageRepository
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

    Given("취소하고 싶은 예약 메일이 있는 경우") {
        val mailMessage = mailMessageRepository.save(createReservationMailMessage())

        When("메일 예약을 취소하면") {
            mailMessageService.cancelReservation(mailMessage.id)

            Then("메일 메시지와 예약이 삭제된다") {
                mailMessageRepository.findById(mailMessage.id).isEmpty shouldBe true
                mailReservationRepository.findAll() shouldBe emptyList()
            }
        }
    }

    Given("취소하고 싶은 예약 메일이 처리중인 경우") {
        val mailMessage = mailMessageRepository.save(createReservationMailMessage())
        mailMessage.reservation()?.process()

        When("메일 예약을 취소하면") {
            Then("에러가 발생하고 메일 메시지와 예약은 남아있다") {
                shouldThrow<IllegalStateException> {
                    mailMessageService.cancelReservation(mailMessage.id)
                }

                val actual = mailMessageRepository.getOrThrow(mailMessage.id)
                actual shouldNotBe null
                actual.reservation() shouldNotBe null
            }
        }
    }

    Given("취소하고 싶은 예약 메일이 발송 완료된 경우") {
        val mailMessage = mailMessageRepository.save(createReservationMailMessage())
        mailMessage.reservation()?.complete()

        When("메일 예약을 취소하면") {
            Then("에러가 발생하고 메일 메시지와 예약은 남아있다") {
                shouldThrow<IllegalStateException> {
                    mailMessageService.cancelReservation(mailMessage.id)
                }

                val actual = mailMessageRepository.getOrThrow(mailMessage.id)
                actual shouldNotBe null
                actual.reservation() shouldNotBe null
            }
        }
    }
})
