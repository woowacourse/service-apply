package apply.domain.mail

import org.springframework.data.jpa.repository.JpaRepository

interface MailMessageRepository : JpaRepository<MailMessage, Long>
