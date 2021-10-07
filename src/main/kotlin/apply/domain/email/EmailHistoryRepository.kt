package apply.domain.email

import org.springframework.data.jpa.repository.JpaRepository

interface EmailHistoryRepository : JpaRepository<EmailHistory, Long>
