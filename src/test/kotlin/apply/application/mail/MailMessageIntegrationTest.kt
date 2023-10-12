package apply.application.mail

import apply.createAvailableReservationTime
import apply.createMailData
import apply.createMailMessage
import apply.createMailReservation
import apply.domain.mail.MailMessageRepository
import apply.domain.mail.MailReservationRepository
import apply.domain.mail.MailReservationStatus
import apply.domain.mail.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import support.test.IntegrationTest

@Transactional
@IntegrationTest
class MailMessageIntegrationTest(
    private val mailMessageService: MailMessageService,
    private val mailMessageRepository: MailMessageRepository,
    private val mailReservationRepository: MailReservationRepository
) : BehaviorSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    Given("예약하고 싶은 메일 내용이 있는 경우") {
        val reservationTime = createAvailableReservationTime()
        val mailData = createMailData(sentTime = reservationTime)

        When("메일을 예약하면") {
            val mailMessage = mailMessageService.reserve(mailData)

            Then("메일 메시지와 예약이 생성된다") {
                mailMessageRepository.getOrThrow(mailMessage.id).subject shouldBe mailData.subject

                val reservation = mailReservationRepository.findByMailMessageId(mailMessage.id)
                reservation shouldNotBe null
                reservation?.reservationTime shouldBe reservationTime
            }
        }
    }

    Given("취소하고 싶은 예약 메일이 있는 경우") {
        val mailMessage = mailMessageRepository.save(createMailMessage())
        val mailReservation = mailReservationRepository.save(createMailReservation(mailMessage.id))

        When("메일 예약을 취소하면") {
            mailMessageService.cancelReservation(mailMessage.id)

            Then("메일 메시지와 예약이 삭제된다") {
                mailMessageRepository.findByIdOrNull(mailMessage.id) shouldBe null
                mailReservationRepository.findByIdOrNull(mailReservation.id) shouldBe null
            }
        }
    }

    Given("취소하고 싶은 예약 메일이 처리중인 경우") {
        val mailMessage = mailMessageRepository.save(createMailMessage())
        val mailReservation = mailReservationRepository.save(createMailReservation(mailMessage.id))
        mailReservation.send()

        When("메일 예약을 취소하면") {
            Then("에러가 발생하고 메일 메시지와 예약은 남아있다") {
                shouldThrow<IllegalStateException> {
                    mailMessageService.cancelReservation(mailMessage.id)
                }

                mailMessageRepository.findByIdOrNull(mailMessage.id) shouldNotBe null
                mailReservationRepository.findByIdOrNull(mailReservation.id) shouldNotBe null
            }
        }
    }

    Given("취소하고 싶은 예약 메일이 발송 완료된 경우") {
        val mailMessage = mailMessageRepository.save(createMailMessage())
        val mailReservation = mailReservationRepository.save(createMailReservation(mailMessage.id))
        mailReservation.finish()

        When("메일 예약을 취소하면") {
            Then("에러가 발생하고 메일 메시지와 예약은 남아있다") {
                shouldThrow<IllegalStateException> {
                    mailMessageService.cancelReservation(mailMessage.id)
                }

                mailMessageRepository.findByIdOrNull(mailMessage.id) shouldNotBe null
                mailReservationRepository.findByIdOrNull(mailReservation.id) shouldNotBe null
            }
        }
    }

    Given("특정 시간에 발송할 예약 메일이 있는 경우") {
        val reservationTime = createAvailableReservationTime()
        val mailMessage1 = mailMessageRepository.save(createMailMessage())
        val mailMessage2 = mailMessageRepository.save(createMailMessage())
        val mailMessage3 = mailMessageRepository.save(createMailMessage())

        mailReservationRepository.save(createMailReservation(mailMessage1.id, reservationTime.minusHours(3)))
        mailReservationRepository.save(createMailReservation(mailMessage2.id, reservationTime))
        mailReservationRepository.save(createMailReservation(mailMessage3.id, reservationTime.plusHours(3)))

        When("해당 시간에 메일 발송 요청을 하면") {
            mailMessageService.sendReservedMail(reservationTime)

            Then("메일 전송이 완료된다") {
                val actual = mailReservationRepository.findByStatus(MailReservationStatus.FINISHED)
                actual.size shouldBe 1
            }
        }
    }
})
