package apply.application

import apply.createRecruitment
import apply.createRecruitmentData
import apply.createRecruitmentItem
import apply.createRecruitmentItemData
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import apply.domain.term.getById
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class RecruitmentServiceTest : BehaviorSpec({
    val recruitmentRepository = mockk<RecruitmentRepository>()
    val recruitmentItemRepository = mockk<RecruitmentItemRepository>()
    val termRepository = mockk<TermRepository>()

    val recruitmentService = RecruitmentService(recruitmentRepository, recruitmentItemRepository, termRepository)

    Given("특정 기수가 있는 경우") {
        every { termRepository.getById(any()) } returns Term.SINGLE
        every { recruitmentRepository.save(any()) } returns createRecruitment(id = 1L)
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()
        every { recruitmentItemRepository.deleteAll(any()) } just Runs
        every { recruitmentItemRepository.saveAll<RecruitmentItem>(any()) } returns emptyList()

        When("모집 항목 없이 해당 기수에 대한 모집을 생성하면") {
            recruitmentService.save(createRecruitmentData())

            Then("모집만 저장된다") {
                verify(exactly = 1) { recruitmentRepository.save(createRecruitment()) }
                verify(exactly = 1) { recruitmentItemRepository.deleteAll(emptyList()) }
                verify(exactly = 1) { recruitmentItemRepository.saveAll(emptyList()) }
            }
        }

        When("해당 기수에 대한 모집을 모집 항목과 함께 생성하면") {
            recruitmentService.save(createRecruitmentData(recruitmentItems = listOf(createRecruitmentItemData())))

            Then("모집 및 모집 항목이 저장된다") {
                verify(exactly = 1) { recruitmentRepository.save(createRecruitment()) }
                verify(exactly = 1) { recruitmentItemRepository.deleteAll(emptyList()) }
                verify(exactly = 1) { recruitmentItemRepository.saveAll<RecruitmentItem>(any()) }
            }
        }
    }

    Given("모집 항목이 없는 특정 모집이 있는 경우") {
        val recruitment = createRecruitment(id = 1L)

        every { termRepository.getById(any()) } returns Term.SINGLE
        every { recruitmentRepository.save(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()
        every { recruitmentItemRepository.deleteAll(any()) } just Runs
        every { recruitmentItemRepository.saveAll<RecruitmentItem>(any()) } returns emptyList()

        When("해당 모집을 수정하면") {
            recruitmentService.save(createRecruitmentData(id = recruitment.id))

            Then("해당 모집만 수정된다") {
                verify(exactly = 1) { recruitmentRepository.save(createRecruitment(id = 1L)) }
                verify(exactly = 1) { recruitmentItemRepository.deleteAll(emptyList()) }
                verify(exactly = 1) { recruitmentItemRepository.saveAll<RecruitmentItem>(any()) }
            }
        }
    }

    Given("모집 항목이 있는 특정 모집이 있는 경우") {
        val recruitment = createRecruitment(id = 1L)
        val recruitmentItem1 = createRecruitmentItem(id = 1L)
        val recruitmentItem2 = createRecruitmentItem(id = 2L)
        val recruitmentItems = listOf(recruitmentItem1, recruitmentItem2)

        every { termRepository.getById(any()) } returns Term.SINGLE
        every { recruitmentRepository.save(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { recruitmentItemRepository.deleteAll(any()) } just Runs
        every { recruitmentItemRepository.saveAll<RecruitmentItem>(any()) } returns emptyList()

        When("일부 모집 항목을 삭제하고 모집을 수정하면") {
            recruitmentService.save(
                createRecruitmentData(
                    recruitmentItems = listOf(createRecruitmentItemData(id = recruitmentItem1.id)),
                    id = recruitment.id
                )
            )

            Then("나머지 모집 항목만 수정된다") {
                verify(exactly = 1) { recruitmentItemRepository.deleteAll(listOf(recruitmentItem2)) }
                verify(exactly = 1) { recruitmentItemRepository.saveAll(listOf(recruitmentItem1)) }
            }
        }

        When("모든 모집 항목을 삭제하고 모집을 수정하면") {
            recruitmentService.save(createRecruitmentData(recruitmentItems = emptyList(), id = recruitment.id))

            Then("모든 모집 항목이 삭제된다") {
                verify(exactly = 1) { recruitmentItemRepository.deleteAll(recruitmentItems) }
                verify(exactly = 1) { recruitmentItemRepository.saveAll(emptyList()) }
            }
        }
    }

    Given("모집 중인 모집이 있는 경우") {
        val recruitment = createRecruitment(recruitable = true)

        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment

        When("해당 모집을 삭제하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    recruitmentService.deleteById(recruitment.id)
                }
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})
