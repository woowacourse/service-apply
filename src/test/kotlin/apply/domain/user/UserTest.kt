package apply.domain.user

import apply.PASSWORD
import apply.WRONG_PASSWORD
import apply.createUser
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
    fun `지원자의 개인정보가 요청받은 개인정보와 일치하는지 확인한다`() {
        assertDoesNotThrow { user.authenticate(createUser()) }
    }

    @Test
    fun `지원자의 개인정보가 요청받은 개인정보와 다를 경우 예외가 발생한다`() {
        assertThatThrownBy { user.authenticate(createUser(name = "다른 이름")) }
            .isInstanceOf(UserAuthenticationException::class.java)
    }

    @Test
    fun `지원자의 비밀번호와 일치하는지 확인한다`() {
        assertDoesNotThrow { user.authenticate(PASSWORD) }
    }

    @Test
    fun `지원자의 비밀번호와 다를 경우 예외가 발생한다`() {
        assertThatThrownBy { user.authenticate(WRONG_PASSWORD) }
            .isInstanceOf(UserAuthenticationException::class.java)
    }
}
