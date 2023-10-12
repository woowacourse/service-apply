package apply.application.mail

import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.MailMessage
import apply.domain.mail.MailMessageRepository
import apply.domain.mail.MailReservation
import apply.domain.mail.MailReservationRepository
import apply.domain.mail.MailReservationStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class MailMessageService(
    private val sendingMailService: SendingMailService,
    private val mailMessageRepository: MailMessageRepository,
    private val mailReservationRepository: MailReservationRepository,
    private val mailHistoryRepository: MailHistoryRepository
) {
    fun findSentMails(): List<MailMessageResponse> {
        val histories = mailHistoryRepository.findAll()
        val messagesById = findMessageMapById(histories.map { it.mailMessageId })
        return messagesById.map { (id, message) ->
            MailMessageResponse(
                mailMessage = message,
                mailHistories = histories.filter { it.mailMessageId == id }
            )
        }
    }

    fun findReservedMails(): List<MailMessageResponse> {
        val reservations = mailReservationRepository.findByStatus(MailReservationStatus.WAITING)
        val messagesById = findMessageMapById(reservations.map { it.mailMessageId })

        return reservations
            .filter { messagesById.contains(it.mailMessageId) }
            .map {
                MailMessageResponse(
                    mailMessage = messagesById.getValue(it.mailMessageId),
                    mailReservation = it
                )
            }
    }

    fun reserve(request: MailData): MailMessageResponse {
        val mailMessage = mailMessageRepository.save(request.toMailMessage())
        val mailReservation = mailReservationRepository.save(
            MailReservation(mailMessageId = mailMessage.id, reservationTime = request.sentTime)
        )
        return MailMessageResponse(mailMessage, mailReservation)
    }

    fun cancelReservation(mailMessageId: Long) {
        val mailReservation = mailReservationRepository.findByMailMessageId(mailMessageId)
            ?: throw IllegalArgumentException("메일 예약이 존재하지 않습니다. email: $mailMessageId")
        check(mailReservation.canCancel()) { "예약 취소할 수 없는 메일입니다." }
        mailReservationRepository.deleteById(mailReservation.id)
        mailMessageRepository.deleteById(mailReservation.mailMessageId)
    }

    fun sendReservedMails(standardTime: LocalDateTime = LocalDateTime.now()) {
        val reservations = mailReservationRepository.findByReservationTimeBeforeAndStatus(
            standardTime,
            MailReservationStatus.WAITING
        )
        val messagesById = findMessageMapById(reservations.map { it.mailMessageId })

        reservations.forEach { mailReservation ->
            mailReservation.send()
            mailReservationRepository.save(mailReservation)
            sendingMailService.sendMailByBccSynchronous(MailData(messagesById.getValue(mailReservation.mailMessageId)))
            mailReservation.finish()
        }
    }

    private fun findMessageMapById(mailMessageIds: List<Long>): Map<Long, MailMessage> {
        return mailMessageRepository
            .findAllById(mailMessageIds)
            .associateBy { it.id }
    }
}
