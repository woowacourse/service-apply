package apply.domain.applicant

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PasswordTest {
    @Test
    fun `변환된 비밀번호를 생성한다`() {
        val input = "password"
        val password = Password(input)

        assertThat(password.value).isNotEqualTo(input)
    }
}
