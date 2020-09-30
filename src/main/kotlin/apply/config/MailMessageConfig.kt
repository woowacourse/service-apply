package apply.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage

@Configuration
class MailMessageConfig {
    @Bean
    fun message(@Value("\${spring.mail.username}") senderAddress: String): SimpleMailMessage {
        val message = SimpleMailMessage()
        message.setFrom(senderAddress)

        return message
    }
}
