package apply.application

import apply.createAnswerRequest
import apply.createApplicationForm
import apply.createApplicationForms
import apply.createRecruitment
import apply.createRecruitmentItem
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.applicationform.ApplicationValidator
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitment.getOrThrow
import apply.domain.recruitmentitem.RecruitmentItemRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import support.test.spec.afterRootTest
import java.time.LocalDateTime.now

class ApplicationFormServiceTest : BehaviorSpec({
    val applicationFormRepository = mockk<ApplicationFormRepository>()
    val recruitmentRepository = mockk<RecruitmentRepository>()
    val recruitmentItemRepository = mockk<RecruitmentItemRepository>()
    val applicationValidator = mockk<ApplicationValidator>()

    val applicationFormService = ApplicationFormService(
        applicationFormRepository,
        recruitmentRepository,
        recruitmentItemRepository,
        applicationValidator
    )

    Given("지원할 수 있는 모집이 있고 특정 회원이 있는 경우") {
        val recruitment = createRecruitment(recruitable = true, id = 1L)
        val userId = 1L

        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndUserId(any(), any()) } returns false
        every { applicationValidator.validate(any(), any()) } just Runs
        every { applicationFormRepository.save(any()) } returns createApplicationForm(userId, recruitment.id)

        When("특정 회원이 해당 모집에 대한 지원서를 생성하면") {
            val actual = applicationFormService.create(userId, CreateApplicationFormRequest(recruitment.id))

            Then("임시 저장된 지원서가 생성된다") {
                actual.submitted.shouldBeFalse()
            }
        }
    }

    Given("지원할 수 있는 모집이 있고 해당 모집에 대해 특정 회원이 작성한 지원서가 없는 경우") {
        val recruitment = createRecruitment(recruitable = true, id = 1L)
        val userId = 1L

        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns null

        When("특정 회원이 해당 모집에 대한 지원서를 수정하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    applicationFormService.update(userId, UpdateApplicationFormRequest(recruitment.id))
                }
            }
        }
    }

    Given("지원할 수 없는 모집이 있고 해당 모집에 지원서를 제출한 특정 회원이 있는 경우") {
        val recruitment = createRecruitment(recruitable = false, id = 1L)
        val userId = 1L

        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment

        When("특정 회원이 해당 모집에 대한 지원서를 수정하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    applicationFormService.update(userId, UpdateApplicationFormRequest(recruitment.id))
                }
            }
        }
    }

    Given("특정 모집에 대한 임시 지원서를 작성한 지원자가 있고 모집 항목이 있는 경우") {
        val recruitmentId = 1L
        val recruitmentItemId = 1L
        val userId = 1L

        every { recruitmentRepository.getOrThrow(any()) } returns createRecruitment(id = recruitmentId)
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns listOf(
            createRecruitmentItem(recruitmentId = recruitmentId, id = recruitmentItemId)
        )
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns createApplicationForm(
            userId = userId, recruitmentId = recruitmentId, submitted = false
        )

        When("미제출 항목이 있는 상태에서 지원서를 최종 제출하면") {
            val request = UpdateApplicationFormRequest(recruitmentId = 1L, submitted = true, answers = emptyList())

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> { applicationFormService.update(userId, request) }
            }
        }

        When("항목이 비어 있는 상태에서 지원서를 최종 제출하면") {
            val request = UpdateApplicationFormRequest(
                recruitmentId = 1L,
                submitted = true,
                answers = listOf(createAnswerRequest(contents = "", recruitmentItemId))
            )

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> { applicationFormService.update(userId, request) }
            }
        }
    }

    Given("특정 모집에 대한 임시 지원서를 작성한 지원자가 있고 글자 수 제한이 있는 특정 모집 항목이 있는 경우") {
        val recruitmentId = 1L
        val recruitmentItemId = 1L
        val maximumLength = 1
        val userId = 1L

        every { recruitmentRepository.getOrThrow(any()) } returns createRecruitment(id = recruitmentId)
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns listOf(
            createRecruitmentItem(recruitmentId = recruitmentId, maximumLength = maximumLength, id = recruitmentItemId)
        )
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns createApplicationForm(
            userId = userId, recruitmentId = recruitmentId, submitted = false
        )

        When("최대 글자 수를 초과하여 항목을 작성하면") {
            val request = UpdateApplicationFormRequest(
                recruitmentId = recruitmentId,
                answers = listOf(
                    createAnswerRequest(contents = "*".repeat(maximumLength + 1), recruitmentItemId = recruitmentItemId)
                )
            )

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> { applicationFormService.update(userId, request) }
            }
        }
    }

    Given("특정 회원이 특정 모집에 대해 작성한 임시 지원서가 있는 경우") {
        val applicationForm = createApplicationForm(submitted = false)

        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns applicationForm

        When("해당 지원서를 조회하면") {
            val actual = applicationFormService.getApplicationForm(1L, 1L)

            Then("지원서를 확인할 수 있다") {
                actual shouldBe ApplicationFormResponse(applicationForm)
            }
        }
    }

    Given("특정 회원이 특정 모집에 대해 생성한 지원서가 없는 경우") {
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns null

        When("지원서를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    applicationFormService.getApplicationForm(1L, 1L)
                }
            }
        }
    }

    Given("특정 회원이 특정 모집에 대해 최종 지원서를 제출한 경우") {
        val applicationForm = createApplicationForm(submitted = true, submittedDateTime = now())

        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns applicationForm

        When("해당 지원서를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    applicationFormService.getApplicationForm(1L, 1L)
                }
            }
        }
    }

    Given("지원자가 생성한 지원서가 여러 개 있는 경우") {
        every { applicationFormRepository.findAllByUserId(any()) } returns createApplicationForms()

        When("해당 지원자에 대한 모든 지원서를 조회하면") {
            val actual = applicationFormService.getMyApplicationForms(1L)

            Then("모든 지원서가 조회된다") {
                actual shouldHaveSize 2
            }
        }
    }

    Given("지원자가 생성한 지원서가 없는 경우") {
        every { applicationFormRepository.findAllByUserId(any()) } returns emptyList()

        When("해당 지원자에 대한 모든 지원서를 조회하면") {
            val actual = applicationFormService.getMyApplicationForms(1L)

            Then("빈 목록이 조회된다") {
                actual.shouldBeEmpty()
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
