package apply.domain.user

import apply.PASSWORD
import apply.WRONG_PASSWORD
import apply.createUser
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class UserTest {
    private lateinit var user: User

    @BeforeEach
    internal fun setUp() {
        user = createUser()
    }

    @Test
    fun `회원의 비밀번호와 일치하는지 확인한다`() {
        assertDoesNotThrow { user.authenticate(PASSWORD) }
    }

    @Test
    fun `회원의 비밀번호와 다를 경우 예외가 발생한다`() {
        assertThatThrownBy { user.authenticate(WRONG_PASSWORD) }
            .isInstanceOf(UserAuthenticationException::class.java)
    }

    @Test
    fun `회원이 전화번호를 수정한다`() {
        val user = createUser(phoneNumber = "010-0000-0000")
        val newPhoneNumber = "010-1111-1111"
        user.changePhoneNumber(newPhoneNumber)
        assertThat(user.phoneNumber).isEqualTo(newPhoneNumber)
    }
}
