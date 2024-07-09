package apply.application

import apply.EMAIL
import apply.INVALID_CODE
import apply.PASSWORD
import apply.VALID_CODE
import apply.VALID_TOKEN
import apply.WRONG_PASSWORD
import apply.createAuthenticateMemberRequest
import apply.createAuthenticationCode
import apply.createMember
import apply.createRegisterMemberRequest
import apply.domain.authenticationcode.AuthenticationCodeRepository
import apply.domain.authenticationcode.getLastByEmail
import apply.domain.member.MemberRepository
import apply.domain.member.UnidentifiedMemberException
import apply.domain.member.existsByEmail
import apply.domain.member.findByEmail
import apply.security.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class MemberAuthenticationServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val authenticationCodeRepository = mockk<AuthenticationCodeRepository>()
    val jwtTokenProvider = mockk<JwtTokenProvider>()

    val memberAuthenticationService = MemberAuthenticationService(
        memberRepository, authenticationCodeRepository, jwtTokenProvider
    )

    Given("특정 이메일의 회원이 있는 경우") {
        every { memberRepository.existsByEmail(any()) } returns true

        When("해당 이메일로 회원 가입을 하고 토큰을 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    memberAuthenticationService.generateTokenByRegister(createRegisterMemberRequest())
                }
            }
        }
    }

    Given("새 이메일이지만 해당 이메일의 인증 코드가 존재하지 않는 경우") {
        every { memberRepository.existsByEmail(any()) } returns false
        every { authenticationCodeRepository.getLastByEmail(any()) } throws IllegalStateException()

        When("해당 이메일로 회원 가입을 하고 토큰을 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    memberAuthenticationService.generateTokenByRegister(createRegisterMemberRequest())
                }
            }
        }
    }

    Given("새 이메일이고 해당 이메일에 대해 인증된 인증 코드가 있는 경우") {
        val email = EMAIL
        val authenticationCode = VALID_CODE

        every { memberRepository.existsByEmail(any()) } returns false
        every { authenticationCodeRepository.getLastByEmail(any()) } returns createAuthenticationCode(
            email, authenticationCode, true
        )
        every { memberRepository.save(any()) } returns createMember(email = email)
        every { jwtTokenProvider.createToken(any()) } returns VALID_TOKEN

        When("비밀번호와 확인 비밀번호를 일치시키지 않고 회원 가입을 하고 토큰을 생성하면") {
            val request = createRegisterMemberRequest(
                email = email,
                password = PASSWORD,
                confirmPassword = WRONG_PASSWORD,
                authenticationCode = authenticationCode
            )

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    memberAuthenticationService.generateTokenByRegister(request)
                }
            }
        }

        When("해당 인증 코드와 다른 인증 코드로 회원 가입하고 토큰을 생성하면") {
            val request = createRegisterMemberRequest(email = email, authenticationCode = INVALID_CODE)

            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    memberAuthenticationService.generateTokenByRegister(request)
                }
            }
        }

        When("해당 이메일과 인증 코드로 회원 가입을 하고 토큰을 생성하면") {
            val actual = memberAuthenticationService.generateTokenByRegister(
                createRegisterMemberRequest(email = email, authenticationCode = authenticationCode)
            )

            Then("회원을 저장하고 토큰을 반환한다") {
                verify { memberRepository.save(any()) }
                actual shouldBe VALID_TOKEN
            }
        }
    }

    Given("특정 비밀번호를 가진 회원이 있을 경우") {
        val member = createMember(password = PASSWORD)

        every { memberRepository.findByEmail(any()) } returns member

        When("다른 비밀번호로 로그인하고 토큰을 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<UnidentifiedMemberException> {
                    memberAuthenticationService.generateTokenByLogin(
                        createAuthenticateMemberRequest(member.email, WRONG_PASSWORD)
                    )
                }
            }
        }

        When("동일한 비밀번호로 로그인하고 토큰을 생성하면") {
            val actual = memberAuthenticationService.generateTokenByLogin(
                createAuthenticateMemberRequest(member.email, member.password)
            )

            Then("유효한 토큰을 반환한다") {
                actual shouldBe VALID_TOKEN
            }
        }
    }

    Given("특정 이메일의 회원이 존재하지 않는 경우") {
        every { memberRepository.findByEmail(any()) } returns null

        When("해당 이메일로 로그인하고 토큰을 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<UnidentifiedMemberException> {
                    memberAuthenticationService.generateTokenByLogin(createAuthenticateMemberRequest())
                }
            }
        }
    }

    Given("새 이메일이 있는 경우") {
        val email = EMAIL

        every { memberRepository.existsByEmail(any()) } returns false
        every { authenticationCodeRepository.save(any()) } returns createAuthenticationCode(email)

        When("해당 이메일에 대한 인증 코드를 생성하면") {
            val actual = memberAuthenticationService.generateAuthenticationCode(email)

            Then("인증 코드가 생성된다") {
                actual.shouldNotBeEmpty()
            }
        }
    }

    Given("특정 이메일에 대한 회원이 있는 경우") {
        every { memberRepository.existsByEmail(any()) } returns true

        When("해당 이메일에 대한 인증 코드를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    memberAuthenticationService.generateAuthenticationCode(EMAIL)
                }
            }
        }
    }

    Given("특정 인증 코드가 있는 경우") {
        val authenticationCode = createAuthenticationCode()

        every { authenticationCodeRepository.getLastByEmail(any()) } returns authenticationCode

        When("동일한 이메일과 인증 코드로 인증하면") {
            memberAuthenticationService.authenticateEmail(authenticationCode.email, authenticationCode.code)

            Then("인증된 인증 코드가 된다") {
                authenticationCode.authenticated.shouldBeTrue()
            }
        }

        When("이메일은 동일하지만 다른 인증 코드로 인증하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    memberAuthenticationService.authenticateEmail(authenticationCode.email, INVALID_CODE)
                }
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})
