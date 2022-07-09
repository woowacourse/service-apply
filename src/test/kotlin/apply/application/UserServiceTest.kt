package apply.application

import apply.*
import apply.domain.user.*
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import org.junit.jupiter.api.DisplayName
import support.test.UnitTest

@UnitTest
internal class UserServiceTest : AnnotationSpec() {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var passwordGenerator: PasswordGenerator

    private lateinit var userService: UserService

    @BeforeEach
    internal fun setUp() {
        userService = UserService(userRepository, passwordGenerator)
    }

    @DisplayName("비밀번호 초기화는")
    @Nested
    inner class ResetPassword {
        private lateinit var user: User
        private lateinit var request: ResetPasswordRequest

        @BeforeEach
        internal fun setUp() {
            user = createUser()
            every { userRepository.findByEmail(EMAIL) } returns user
            every { passwordGenerator.generate() } returns RANDOM_PASSWORD_TEXT
        }

        fun subject() {
            userService.resetPassword(request)
        }

        @Test
        fun `만약 개인정보가 일치한다면 초기화한다`() {
            every { userRepository.save(any()) } returns user
            request = ResetPasswordRequest(NAME, EMAIL, BIRTHDAY)
            subject()
            user.password shouldBe Password(RANDOM_PASSWORD_TEXT)
        }

        @Test
        fun `만약 개인정보가 일치하지 않는다면 예외가 발생한다`() {
            request = ResetPasswordRequest("가짜 이름", EMAIL, BIRTHDAY)
            shouldThrowExactly<UnidentifiedUserException> { subject() }
        }
    }

    @DisplayName("비밀번호 변경은")
    @Nested
    inner class EditPassword {
        private lateinit var request: EditPasswordRequest

        @BeforeEach
        internal fun setUp() {
            slot<Long>().also { slot ->
                every { userRepository.getById(capture(slot)) } answers { createUser(id = slot.captured) }
            }
        }

        fun subject() {
            userService.editPassword(1L, request)
        }

        @Test
        fun `만약 기존 비밀번호가 일치한다면 변경한다`() {
            request = EditPasswordRequest(PASSWORD, Password("new_password"), Password("new_password"))
            shouldNotThrow<Exception> { subject() }
        }

        @Test
        fun `만약 기존 비밀번호가 일치하지 않다면 예외가 발생한다`() {
            request = EditPasswordRequest(WRONG_PASSWORD, Password("new_password"), Password("new_password"))
            shouldThrowExactly<UnidentifiedUserException> { subject() }
        }

        @Test
        fun `확인용 비밀번호가 일치하지 않으면 예외가 발생한다`() {
            request = EditPasswordRequest(WRONG_PASSWORD, Password("new_password"), Password("wrong_password"))
            shouldThrowExactly<IllegalArgumentException> { subject() }
        }
    }

    @Test
    fun `회원이 정보를 조회한다`() {
        val user = createUser()
        every { userRepository.getById(any()) } returns user

        val expected = userService.getInformation(user.id)

        assertSoftly {
            expected.id.shouldNotBeNull()
            expected.name.shouldNotBeNull()
            expected.email.shouldNotBeNull()
            expected.phoneNumber.shouldNotBeNull()
            expected.gender.shouldNotBeNull()
            expected.birthday.shouldNotBeNull()
        }
    }

    @Test
    fun `회원이 정보(전화번호)를 변경한다`() {
        val request = EditInformationRequest("010-9999-9999")
        val user = createUser(phoneNumber = "010-0000-0000")
        every { userRepository.getById(any()) } returns user

        shouldNotThrow<Exception> { userService.editInformation(user.id, request) }
        user.phoneNumber shouldBe request.phoneNumber
    }
}
