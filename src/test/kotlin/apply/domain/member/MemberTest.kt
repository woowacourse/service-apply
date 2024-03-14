package apply.domain.member

import apply.PASSWORD
import apply.WRONG_PASSWORD
import apply.createMember
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MemberTest : StringSpec({
    "회원의 비밀번호와 일치하는지 확인한다" {
        val member = createMember()
        shouldNotThrowAny { member.authenticate(PASSWORD) }
    }

    "회원의 비밀번호와 다를 경우 예외가 발생한다" {
        val member = createMember()
        shouldThrow<UnidentifiedMemberException> { member.authenticate(WRONG_PASSWORD) }
    }

    "회원이 전화번호를 수정한다" {
        val member = createMember(phoneNumber = "010-0000-0000")
        val newPhoneNumber = "010-1111-1111"
        member.changePhoneNumber(newPhoneNumber)
        member.phoneNumber shouldBe newPhoneNumber
    }
})
