package apply.utils

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RandomStringGenerator {
    @Bean
    fun generateRandomString(): String {
        return UUID.randomUUID().toString()
    }
}
