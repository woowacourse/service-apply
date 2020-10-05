package apply.utils

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UUIDBasedPasswordGenerator : RandomPasswordGenerator {
    override fun generate(): String {
        return UUID.randomUUID().toString().substring(0, 18)
    }
}
