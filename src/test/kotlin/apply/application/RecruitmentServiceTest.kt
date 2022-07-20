package apply.application

import apply.createRecruitment
import apply.createRecruitmentData
import apply.createRecruitmentItem
import apply.createRecruitmentItemData
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.domain.term.TermRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import support.test.UnitTest

@UnitTest
internal class RecruitmentServiceTest : DescribeSpec({
    val recruitmentRepository: RecruitmentRepository = mockk()
    val recruitmentItemRepository: RecruitmentItemRepository = mockk()
    val termRepository: TermRepository = mockk()
    val recruitmentService = RecruitmentService(recruitmentRepository, recruitmentItemRepository, termRepository)

    slot<Recruitment>().also { slot ->
        every { recruitmentRepository.save(capture(slot)) } answers {
            slot.captured.run {
                Recruitment(title, period, 0L, recruitable, hidden, id)
            }
        }
    }
    every { recruitmentItemRepository.deleteAll(any()) } just Runs
    slot<List<RecruitmentItem>>().also { slot ->
        every { recruitmentItemRepository.saveAll(capture(slot)) } answers {
            slot.captured.run {
                map {
                    RecruitmentItem(
                        it.recruitmentId,
                        it.title,
                        it.position,
                        it.maximumLength,
                        it.description,
                        it.id
                    )
                }
            }
        }
    }

    describe("RecruitmentService") {
        context("지원 항목 없이 새 지원을 생성하면") {
            it("지원만 저장한다") {
                every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()

                recruitmentService.save(createRecruitmentData())

                verify { recruitmentRepository.save(any<Recruitment>()) }
                verify { recruitmentItemRepository.deleteAll(mutableListOf()) }
                verify { recruitmentItemRepository.saveAll(mutableListOf()) }
            }
        }

        context("지원 항목이 있는 경우") {
            it("새 지원을 생성하면 지원 및 지원 항목을 저장한다") {
                every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()

                recruitmentService.save(createRecruitmentData(recruitmentItems = listOf(createRecruitmentItemData())))

                verify { recruitmentRepository.save(any<Recruitment>()) }
                verify { recruitmentItemRepository.deleteAll(mutableListOf()) }
                verify { recruitmentItemRepository.saveAll(any<List<RecruitmentItem>>()) }
            }
        }

        context("지원 항목이 없는 지원을 수정하면") {
            it("지원만 수정한다") {
                every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()

                recruitmentService.save(createRecruitmentData(id = 1L))

                verify { recruitmentRepository.save(createRecruitment(id = 1L)) }
                verify { recruitmentItemRepository.deleteAll(mutableListOf()) }
                verify { recruitmentItemRepository.saveAll(mutableListOf()) }
            }
        }

        context("지원 항목이 있는 지원에 대한 일부 지원 항목을 삭제하면") {
            it("나머지 지원 항목만 수정한다") {
                every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(1L) } returns listOf(
                    createRecruitmentItem(id = 1L),
                    createRecruitmentItem(id = 2L)
                )

                recruitmentService.save(
                    createRecruitmentData(
                        id = 1L,
                        recruitmentItems = listOf(createRecruitmentItemData(id = 2L))
                    )
                )

                verify { recruitmentRepository.save(createRecruitment(id = 1L)) }
                verify { recruitmentItemRepository.deleteAll(listOf(createRecruitmentItem(id = 1L))) }
                verify { recruitmentItemRepository.saveAll(listOf(createRecruitmentItem(id = 2L))) }
            }
        }

        context("지원 항목이 있는 지원에 대한 모든 지원 항목을 삭제하면") {
            it("모든 지원 항목을 삭제한다") {
                every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(1L) } returns listOf(
                    createRecruitmentItem(id = 1L)
                )

                recruitmentService.save(createRecruitmentData(id = 1L))

                verify { recruitmentRepository.save(createRecruitment(id = 1L)) }
                verify { recruitmentItemRepository.deleteAll(listOf(createRecruitmentItem(id = 1L))) }
                verify { recruitmentItemRepository.saveAll(mutableListOf()) }
            }
        }

        context("모집 중인 모집은") {
            it("삭제할 수 없다") {
                val recruitment = createRecruitment(recruitable = true)
                every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
                shouldThrow<IllegalStateException> { recruitmentService.deleteById(recruitment.id) }
            }
        }
    }
})
