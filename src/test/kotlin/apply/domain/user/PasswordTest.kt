package apply.domain.user

import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class PasswordTest {
    @Test
    fun `변환된 비밀번호를 생성한다`() {
        val input = "password"
        val password = Password(input)
        password.value shouldNotBe input
    }
}
