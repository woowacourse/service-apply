package apply.application.mail

import apply.createMailReservation
import apply.domain.mail.MailReservationRepository
import apply.domain.mail.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk

class MailReservationServiceTest : BehaviorSpec({
    val mailService = mockk<MailService>()
    val mailReservationRepository = mockk<MailReservationRepository>()
    val mailReservationService = MailReservationService(mailService, mailReservationRepository)

    Given("발송 전 상태의 에약 메일이 있는 경우") {
        val mailReservation = createMailReservation()

        every { mailReservationRepository.getOrThrow(any()) } returns mailReservation
        every { mailReservationRepository.deleteById(any()) } just Runs

        When("해당 메일 예약 삭제를 요청하면") {
            Then("메일 예약이 삭제된다") {
                mailReservationService.deleteReservation(mailReservation.id)
            }
        }
    }

    Given("예약 발송이 진행중이거나 완료 상태의 에약 메일이 있는 경우") {
        listOf(
            createMailReservation().apply { complete() },
            createMailReservation().apply { process() }
        ).forAll { mailReservation ->
            every { mailReservationRepository.getOrThrow(any()) } returns mailReservation

            When("해당 메일 예약 삭제를 요청하면") {
                Then("예외가 발생한다") {
                    shouldThrow<IllegalStateException> {
                        mailReservationService.deleteReservation(mailReservation.id)
                    }
                }
            }
        }
    }
})
