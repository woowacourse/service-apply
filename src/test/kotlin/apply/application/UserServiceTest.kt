package apply.application

import apply.BIRTHDAY
import apply.EMAIL
import apply.NAME
import apply.PASSWORD
import apply.RANDOM_PASSWORD_TEXT
import apply.WRONG_PASSWORD
import apply.createUser
import apply.domain.user.UserAuthenticationException
import apply.domain.user.Password
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest

@UnitTest
internal class UserServiceTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var passwordGenerator: PasswordGenerator

    private lateinit var userService: UserService

    @BeforeEach
    internal fun setUp() {
        userService = UserService(
            userRepository,
            passwordGenerator
        )
    }

    @DisplayName("비밀번호 초기화는")
    @Nested
    inner class ResetPassword {
        private lateinit var request: ResetPasswordRequest

        @BeforeEach
        internal fun setUp() {
            every { userRepository.findByEmail(EMAIL) } returns createUser()
            every { passwordGenerator.generate() } returns RANDOM_PASSWORD_TEXT
        }

        fun subject(): String {
            return userService.resetPassword(request)
        }

        @Test
        fun `만약 개인정보가 일치한다면 초기화한다`() {
            request = ResetPasswordRequest(NAME, EMAIL, BIRTHDAY)
            Assertions.assertThat(subject()).isEqualTo(RANDOM_PASSWORD_TEXT)
        }

        @Test
        fun `만약 개인정보가 일치하지 않는다면 예외가 발생한다`() {
            request = ResetPasswordRequest("가짜 이름", EMAIL, BIRTHDAY)
            org.junit.jupiter.api.assertThrows<UserAuthenticationException> { subject() }
        }
    }

    @DisplayName("비밀번호 변경은")
    @Nested
    inner class EditPassword {
        private lateinit var request: EditPasswordRequest

        @BeforeEach
        internal fun setUp() {
            slot<Long>().also { slot ->
                every { userRepository.getOne(capture(slot)) } answers { createUser(id = slot.captured) }
            }
        }

        fun subject() {
            userService.editPassword(1L, request)
        }

        @Test
        fun `만약 기존 비밀번호가 일치한다면 변경한다`() {
            request = EditPasswordRequest(PASSWORD, Password("new_password"))
            assertDoesNotThrow { subject() }
        }

        @Test
        fun `만약 기존 비밀번호가 일치하지 않다면 예외가 발생한다`() {
            request = EditPasswordRequest(WRONG_PASSWORD, Password("new_password"))
            assertThrows<UserAuthenticationException> { subject() }
        }
    }
}
