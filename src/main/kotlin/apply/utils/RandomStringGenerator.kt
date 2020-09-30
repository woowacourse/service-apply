package apply.utils

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RandomStringGenerator {
    fun generateRandomString(): String {
        return UUID.randomUUID().toString().substring(0, 18)
    }
}
