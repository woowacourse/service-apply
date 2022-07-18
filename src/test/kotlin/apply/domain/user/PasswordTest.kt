package apply.domain.user

import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat

class PasswordTest : StringSpec({
    "변환된 비밀번호를 생성한다" {
        val input = "password"
        val password = Password(input)

        assertThat(password.value).isNotEqualTo(input)
    }
})
