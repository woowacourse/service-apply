package apply.domain.mail

import org.springframework.data.jpa.repository.JpaRepository

interface MailReservationRepository : JpaRepository<MailReservation, Long>
