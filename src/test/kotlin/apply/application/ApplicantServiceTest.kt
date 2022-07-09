package apply.application

import apply.createApplicationForm
import apply.createUser
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.user.UserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import support.test.UnitTest

@UnitTest
class ApplicantServiceTest : DescribeSpec({
    val userRepository: UserRepository = mockk()
    val cheaterRepository: CheaterRepository = mockk()
    val applicationFormRepository: ApplicationFormRepository = mockk()
    val applicantService: ApplicantService =
        ApplicantService(applicationFormRepository, userRepository, cheaterRepository)

    val userId = 1L
    val email = "email@email.com"

    slot<Long>().also { slot ->
        every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(capture(slot)) } answers {
            listOf(createApplicationForm(recruitmentId = slot.captured))
        }
    }

    every { cheaterRepository.findAll() } returns listOf(Cheater(email))
    slot<Iterable<Long>>().also { slot ->
        every { userRepository.findAllById(capture(slot)) } answers {
            slot.captured.map { createUser(id = it, email = email) }
        }
    }

    slot<String>().also { slot ->
        every { userRepository.findAllByKeyword(capture(slot)) } answers {
            listOf(createUser(name = slot.captured, id = userId, email = email))
        }
    }

    describe("ApplicantService") {
        context("지원자 찾기 기능") {
            it("지원자 정보와 부정 행위자 여부를 함께 제공한다") {
                val actual = applicantService.findAllByRecruitmentIdAndKeyword(1L)

                actual shouldHaveSize 1
                actual[0].isCheater.shouldBeTrue()
            }

            it("키워드로 찾은 지원자 정보와 부정 행위자 여부를 함께 제공한다") {
                val actual = applicantService.findAllByRecruitmentIdAndKeyword(1L, "amazzi")

                actual shouldHaveSize 1
                actual[0].isCheater.shouldBeTrue()
            }
        }
    }
})
