package apply.domain.user

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldNotBe

internal class PasswordTest : AnnotationSpec() {
    @Test
    fun `변환된 비밀번호를 생성한다`() {
        val input = "password"
        val password = Password(input)
        password.value shouldNotBe input
    }
}
