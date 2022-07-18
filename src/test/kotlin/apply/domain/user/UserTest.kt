package apply.domain.user

import apply.PASSWORD
import apply.WRONG_PASSWORD
import apply.createUser
import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class UserTest : StringSpec({
    lateinit var user: User

    beforeEach { user = createUser() }

    "회원의 비밀번호와 일치하는지 확인한다" {
        assertDoesNotThrow { user.authenticate(PASSWORD) }
    }

    "회원의 비밀번호와 다를 경우 예외가 발생한다" {
        assertThrows<UnidentifiedUserException> { user.authenticate(WRONG_PASSWORD) }
    }

    "회원이 전화번호를 수정한다" {
        val user = createUser(phoneNumber = "010-0000-0000")
        val newPhoneNumber = "010-1111-1111"
        user.changePhoneNumber(newPhoneNumber)
        assertThat(user.phoneNumber).isEqualTo(newPhoneNumber)
    }
})
