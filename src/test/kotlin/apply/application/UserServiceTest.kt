package apply.application

import apply.BIRTHDAY
import apply.EMAIL
import apply.NAME
import apply.PASSWORD
import apply.RANDOM_PASSWORD_TEXT
import apply.WRONG_PASSWORD
import apply.createUser
import apply.domain.user.Password
import apply.domain.user.UnidentifiedUserException
import apply.domain.user.User
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import apply.domain.user.getById
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import support.test.UnitTest

@UnitTest
internal class UserServiceTest : DescribeSpec({
    val userRepository: UserRepository = mockk()
    val passwordGenerator: PasswordGenerator = mockk()
    val userService = UserService(userRepository, passwordGenerator)

    describe("UserService") {
        var user: User = createUser()
        var request: ResetPasswordRequest = mockk()

        every { userRepository.findByEmail(EMAIL) } returns user
        every { passwordGenerator.generate() } returns RANDOM_PASSWORD_TEXT

        fun subject() {
            userService.resetPassword(request)
        }

        context("개인정보를 비교할 때") {
            it("일치한다면 초기화한다") {
                every { userRepository.save(any()) } returns user
                request = ResetPasswordRequest(NAME, EMAIL, BIRTHDAY)
                subject()
                user.password shouldBe Password(RANDOM_PASSWORD_TEXT)
            }

            it("일치하지 않는다면 예외가 발생한다") {
                request = ResetPasswordRequest("가짜 이름", EMAIL, BIRTHDAY)
                shouldThrowExactly<UnidentifiedUserException> { subject() }
            }
        }

        context("비밀번호를 비교할 때") {
            var request: EditPasswordRequest = mockk()
            fun subject() {
                userService.editPassword(1L, request)
            }
            slot<Long>().also { slot ->
                every { userRepository.getById(capture(slot)) } answers { createUser(id = slot.captured) }
            }

            it("일치한다면 변경한다") {
                request = EditPasswordRequest(PASSWORD, Password("new_password"), Password("new_password"))
                shouldNotThrow<Exception> { subject() }
            }

            it("일치하지 않다면 예외가 발생한다") {
                request = EditPasswordRequest(WRONG_PASSWORD, Password("new_password"), Password("new_password"))
                shouldThrowExactly<UnidentifiedUserException> { subject() }
            }

            it("확인용 비밀번호가 일치하지 않으면 예외가 발생한다") {
                request = EditPasswordRequest(WRONG_PASSWORD, Password("new_password"), Password("wrong_password"))
                shouldThrowExactly<IllegalArgumentException> { subject() }
            }
        }

        context("회원정보를") {
            it("조회한다") {
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

            it("변경한다") {
                val request = EditInformationRequest("010-9999-9999")
                val user = createUser(phoneNumber = "010-0000-0000")
                every { userRepository.getById(any()) } returns user

                shouldNotThrow<Exception> { userService.editInformation(user.id, request) }
                user.phoneNumber shouldBe request.phoneNumber
            }
        }
    }
})
