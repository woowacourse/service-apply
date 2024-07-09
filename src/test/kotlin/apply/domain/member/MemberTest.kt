package apply.domain.member

import apply.PASSWORD
import apply.WRONG_PASSWORD
import apply.createMember
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class MemberTest : StringSpec({
    "만 14세 미만은 회원 가입할 수 없다" {
        val now = LocalDate.now()
        val requirement = MinimumAgeRequirement(age = 14, baseDate = now)
        shouldThrow<IllegalArgumentException> {
            createMember(birthday = now, authorizationRequirement = requirement)
        }
    }

    "회원의 비밀번호와 일치하는지 확인한다" {
        val member = createMember()
        shouldNotThrowAny { member.authenticate(PASSWORD) }
    }

    "회원의 비밀번호와 다를 경우 예외가 발생한다" {
        val member = createMember()
        shouldThrow<UnidentifiedMemberException> { member.authenticate(WRONG_PASSWORD) }
    }

    "회원이 휴대전화 번호를 수정한다" {
        val member = createMember(phoneNumber = "010-0000-0000")
        val newPhoneNumber = "010-1111-1111"
        member.changePhoneNumber(newPhoneNumber)
        member.phoneNumber shouldBe newPhoneNumber
    }

    "회원 탈퇴" {
        val member = createMember(password = PASSWORD)
        member.withdraw(PASSWORD)
        member.name shouldBe MemberInformation.DELETED.name
    }

    "비밀번호가 일치하지 않으면 회원 탈퇴가 실패한다" {
        val member = createMember(password = PASSWORD)
        shouldThrow<UnidentifiedMemberException> { member.withdraw(WRONG_PASSWORD) }
    }
})
