package apply.config

import apply.application.mail.MailSender
import apply.infra.mail.FakeMailSender
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestMailConfig {
    @Primary
    @Bean
    fun testMailSender(): MailSender {
        return FakeMailSender()
    }
}
