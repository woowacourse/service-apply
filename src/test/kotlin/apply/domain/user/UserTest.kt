package apply.domain.user

import apply.PASSWORD
import apply.RANDOM_PASSWORD_TEXT
import apply.WRONG_PASSWORD
import apply.createUser
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class UserTest {
    private lateinit var user: User

    @BeforeEach
    internal fun setUp() {
        user = createUser()
    }

    @Test
    fun `회원의 개인정보가 요청받은 개인정보와 일치하는지 확인한다`() {
        assertDoesNotThrow { user.authenticate(createUser()) }
    }

    @Test
    fun `회원의 개인정보가 요청받은 개인정보와 다를 경우 예외가 발생한다`() {
        assertThrows<UserAuthenticationException> { user.authenticate(createUser(name = "다른 이름")) }
    }

    @Test
    fun `회원의 비밀번호와 일치하는지 확인한다`() {
        assertDoesNotThrow { user.authenticate(PASSWORD) }
    }

    @Test
    fun `회원의 비밀번호와 다를 경우 예외가 발생한다`() {
        assertThrows<UserAuthenticationException> { user.authenticate(WRONG_PASSWORD) }
    }

    @Test
    fun `회원의 비밀번호를 초기화한다`() {
        user.resetPassword(user.name, user.birthday, RANDOM_PASSWORD_TEXT)
        assertThrows<UserAuthenticationException> { user.authenticate(PASSWORD) }
    }
}
