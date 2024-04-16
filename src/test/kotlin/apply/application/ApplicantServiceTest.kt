package apply.application

import apply.createApplicationForm
import apply.createCheater
import apply.createMember
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.member.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest

class ApplicantServiceTest : BehaviorSpec({
    val applicationFormRepository = mockk<ApplicationFormRepository>()
    val memberRepository = mockk<MemberRepository>()
    val cheaterRepository = mockk<CheaterRepository>()

    val applicantService = ApplicantService(applicationFormRepository, memberRepository, cheaterRepository)

    Given("특정 모집에 지원한 부정행위자가 있는 경우") {
        val recruitmentId = 1L
        val member = createMember(id = 1L)
        val cheater = createCheater(email = member.email)

        every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns listOf(
            createApplicationForm(memberId = member.id, recruitmentId = recruitmentId)
        )
        every { cheaterRepository.findAll() } returns listOf(cheater)
        every { memberRepository.findAllById(any()) } returns listOf(createMember(email = cheater.email, id = member.id))

        When("특정 모집에 지원한 지원 정보를 조회하면") {
            val actual = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId)

            Then("지원 정보 및 부정행위 여부를 확인할 수 있다") {
                actual shouldHaveSize 1
                actual[0].isCheater.shouldBeTrue()
            }
        }
    }

    Given("이름이나 이메일에 특정 키워드를 포함하는 지원자와 부정행위자가 있는 경우") {
        val recruitmentId = 1L
        val keyword = "amazzi"
        val member1 = createMember(name = keyword, id = 1L)
        val member2 = createMember(email = "$keyword@email.com", id = 2L)
        val cheater = createCheater(email = member1.email)

        every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns listOf(
            createApplicationForm(memberId = member1.id, recruitmentId = recruitmentId),
            createApplicationForm(memberId = member2.id, recruitmentId = recruitmentId)
        )
        every { cheaterRepository.findAll() } returns listOf(cheater)
        every { memberRepository.findAllByKeyword(keyword) } returns listOf(member1, member2)

        When("특정 키워드로 특정 모집에 지원한 지원 정보를 조회하면") {
            val actual = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword)

            Then("지원 정보 및 부정행위 여부를 확인할 수 있다") {
                actual shouldHaveSize 2
                actual.filter { it.hasKeywordInNameOrEmail(keyword) } shouldHaveSize 2
                actual[0].isCheater.shouldBeTrue()
                actual[1].isCheater.shouldBeFalse()
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})

private fun ApplicantAndFormResponse.hasKeywordInNameOrEmail(keyword: String): Boolean {
    return name.contains(keyword) || email.contains(keyword)
}
