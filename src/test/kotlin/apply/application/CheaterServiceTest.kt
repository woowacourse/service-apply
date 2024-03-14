package apply.application

import apply.createCheater
import apply.createCheaterData
import apply.domain.cheater.CheaterRepository
import apply.domain.member.MemberRepository
import apply.domain.member.findByEmail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest

class CheaterServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val cheaterRepository = mockk<CheaterRepository>()

    val cheaterService = CheaterService(memberRepository, cheaterRepository)

    Given("특정 이메일의 회원이 존재하지 않는 경우") {
        val email = "cheater@email.com"

        every { cheaterRepository.existsByEmail(any()) } returns false
        every { cheaterRepository.save(any()) } returns createCheater(email = email)
        every { memberRepository.findByEmail(any()) } returns null

        When("특정 이메일로 부정행위자를 등록하면") {
            val actual = cheaterService.save(createCheaterData(email = email))

            Then("이름 없이 등록된다") {
                actual.email shouldBe email
                actual.name.shouldBeNull()
            }
        }
    }

    Given("특정 이메일로 등록된 부정행위자가 있는 경우") {
        every { cheaterRepository.existsByEmail(any()) } returns true

        When("해당 이메일로 부정행위자를 등록하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    cheaterService.save(createCheaterData(email = "cheater@email.com"))
                }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
