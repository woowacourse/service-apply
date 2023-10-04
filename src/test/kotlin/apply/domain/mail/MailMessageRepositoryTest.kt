package apply.domain.mail

import apply.createMailMessage
import apply.createReservationMailMessage
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import support.test.RepositoryTest
import support.test.spec.afterRootTest
import java.time.LocalDateTime.now

@RepositoryTest
class MailMessageRepositoryTest(
    private val mailMessageRepository: MailMessageRepository,
    private val entityManager: TestEntityManager
) : ExpectSpec({
    extensions(SpringExtension)

    context("메일 메시지 저장") {
        expect("즉시 발송할 메일 메시지를 저장한다") {
            val actual = mailMessageRepository.save(createMailMessage())
            actual.id.shouldNotBeZero()
        }

        expect("메일 메시지와 함께 메일 예약을 저장한다") {
            val actual = mailMessageRepository.save(createReservationMailMessage())
            actual.id.shouldNotBeZero()
            actual.reservation().shouldNotBeNull()
            actual.reservation()!!.id.shouldNotBeZero()
        }
    }

    context("즉시 발송 메일 메시지 조회") {
        val mailMessage = mailMessageRepository.save(createMailMessage())

        expect("메일 메시지만 조회한다") {
            val actual = mailMessageRepository.findById(mailMessage.id).get()
            actual.shouldNotBeNull()
            actual.reservation().shouldBeNull()
        }
    }

    context("예약 메일 메시지 조회") {
        val reservationTime = now().plusHours(3).withMinute(10)
        val mailMessage = mailMessageRepository.save(createReservationMailMessage(reservationTime = reservationTime))

        expect("메일 메시지와 함께 메일 예약을 조회한다") {
            val actual = mailMessageRepository.findById(mailMessage.id).get()
            actual.shouldNotBeNull()
            actual.reservation().shouldNotBeNull()
            actual.reservation()!!.reservationTime shouldBe reservationTime
        }
    }

    afterEach {
        entityManager.flush()
        entityManager.clear()
    }

    afterRootTest {
        mailMessageRepository.deleteAll()
    }
})
