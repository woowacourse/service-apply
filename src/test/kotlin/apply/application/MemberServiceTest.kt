package apply.application

import apply.NEW_PASSWORD
import apply.PASSWORD
import apply.RANDOM_PASSWORD_TEXT
import apply.WRONG_PASSWORD
import apply.createMember
import apply.domain.member.MemberRepository
import apply.domain.member.Password
import apply.domain.member.UnidentifiedMemberException
import apply.domain.member.findByEmail
import apply.domain.member.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest

class MemberServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val passwordGenerator = mockk<PasswordGenerator>()

    val memberService = MemberService(memberRepository, passwordGenerator)

    Given("특정 회원의 개인정보가 있는 경우") {
        val member = createMember()

        every { memberRepository.findByEmail(any()) } returns member
        every { passwordGenerator.generate() } returns RANDOM_PASSWORD_TEXT
        every { memberRepository.save(any()) } returns member

        When("동일한 개인정보로 비밀번호를 초기화하면") {
            memberService.resetPassword(ResetPasswordRequest(member.name, member.email, member.birthday))

            Then("비밀번호가 초기화된다") {
                member.password shouldBe Password(RANDOM_PASSWORD_TEXT)
            }
        }

        When("일치하지 않는 개인정보로 비밀번호를 초기화하면") {
            Then("예외가 발생한다") {
                shouldThrow<UnidentifiedMemberException> {
                    memberService.resetPassword(ResetPasswordRequest("가짜 이름", member.email, member.birthday))
                }
            }
        }
    }

    Given("특정 회원이 있고 변경할 비밀번호가 있는 경우") {
        val member = createMember(id = 1L, password = PASSWORD)
        val password = NEW_PASSWORD

        every { memberRepository.getOrThrow(any()) } returns member

        When("기존 비밀번호와 함께 새 비밀번호를 변경하면") {
            memberService.editPassword(member.id, EditPasswordRequest(member.password, password, password))

            Then("새 비밀번호로 변경된다") {
                member.password shouldBe password
            }
        }

        When("일치하지 않는 기존 비밀번호와 함께 새 비밀번호를 변경하면") {
            Then("예외가 발생한다") {
                shouldThrow<UnidentifiedMemberException> {
                    memberService.editPassword(member.id, EditPasswordRequest(WRONG_PASSWORD, password, password))
                }
            }
        }

        When("이전 비밀번호는 일치하지만 새 비밀번호와 확인 비밀번호가 일치하지 않으면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    memberService.editPassword(member.id, EditPasswordRequest(member.password, password, WRONG_PASSWORD))
                }
            }
        }
    }

    Given("특정 회원이 있는 경우") {
        val member = createMember(id = 1L)

        every { memberRepository.getOrThrow(any()) } returns member

        When("해당 회원의 정보를 조회하면") {
            val actual = memberService.getInformation(member.id)

            Then("회원 정보를 확인할 수 있다") {
                actual shouldBe MemberResponse(member)
            }
        }
    }

    Given("특정 회원이 존재하고 변경할 정보(휴대전화 번호)가 있는 경우") {
        val member = createMember(phoneNumber = "010-0000-0000")
        val phoneNumber = "010-9999-9999"

        every { memberRepository.getOrThrow(any()) } returns member

        When("특정 회원의 정보(휴대전화 번호)를 변경하면") {
            memberService.editInformation(member.id, EditInformationRequest(phoneNumber))

            Then("정보(휴대전화 번호)가 변경된다") {
                member.phoneNumber shouldBe phoneNumber
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
