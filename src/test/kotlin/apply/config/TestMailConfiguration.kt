package apply.config

import apply.application.mail.FakeMailSender
import apply.application.mail.MailSender
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestMailConfiguration {
    @Primary
    @Bean
    fun fakeMailSender(): MailSender {
        return FakeMailSender()
    }
}
