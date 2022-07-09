package apply.domain.user

import apply.PASSWORD
import apply.WRONG_PASSWORD
import apply.createUser
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserTest {
    private lateinit var user: User

    @BeforeEach
    internal fun setUp() {
        user = createUser()
    }

    @Test
    fun `회원의 비밀번호와 일치하는지 확인한다`() {
        shouldNotThrow<Exception> { user.authenticate(PASSWORD) }
    }

    @Test
    fun `회원의 비밀번호와 다를 경우 예외가 발생한다`() {
        shouldThrowExactly<UnidentifiedUserException> { user.authenticate(WRONG_PASSWORD) }
    }

    @Test
    fun `회원이 전화번호를 수정한다`() {
        val user = createUser(phoneNumber = "010-0000-0000")
        val newPhoneNumber = "010-1111-1111"
        user.changePhoneNumber(newPhoneNumber)
        user.phoneNumber shouldBe newPhoneNumber
    }
}
