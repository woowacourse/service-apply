package apply.application

import apply.createEvaluation
import apply.createEvaluationItem
import apply.createRecruitment
import apply.createRecruitmentData
import apply.createRecruitmentItem
import apply.createRecruitmentItemData
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.domain.term.TermRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import support.test.UnitTest

@UnitTest
internal class RecruitmentServiceTest {
    @MockK
    lateinit var recruitmentRepository: RecruitmentRepository

    @MockK
    private lateinit var recruitmentItemRepository: RecruitmentItemRepository

    @MockK
    private lateinit var applicationFormRepository: ApplicationFormRepository

    @MockK
    private lateinit var evaluationRepository: EvaluationRepository

    @MockK
    private lateinit var evaluationItemRepository: EvaluationItemRepository

    @MockK
    private lateinit var termRepository: TermRepository

    private lateinit var recruitmentService: RecruitmentService

    @BeforeEach
    internal fun setUp() {
        recruitmentService =
            RecruitmentService(
                recruitmentRepository,
                recruitmentItemRepository,
                applicationFormRepository,
                evaluationRepository,
                evaluationItemRepository,
                termRepository
            )
    }

    @Nested
    inner class Save {
        @BeforeEach
        internal fun setUp() {
            slot<Recruitment>().also { slot ->
                every { recruitmentRepository.save(capture(slot)) } answers {
                    slot.captured.run {
                        Recruitment(title, period, 0L, recruitable, hidden, id)
                    }
                }
            }
            every { recruitmentItemRepository.deleteAll(any()) } returns Unit
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
        }

        @Test
        fun `지원 항목 없이 새 지원을 생성하면 지원만 저장한다`() {
            every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()

            recruitmentService.save(createRecruitmentData())

            verify { recruitmentRepository.save(any<Recruitment>()) }
            verify { recruitmentItemRepository.deleteAll(mutableListOf()) }
            verify { recruitmentItemRepository.saveAll(mutableListOf()) }
        }

        @Test
        fun `지원 항목이 있는 경우 새 지원을 생성하면 지원 및 지원 항목을 저장한다`() {
            every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()

            recruitmentService.save(createRecruitmentData(recruitmentItems = listOf(createRecruitmentItemData())))

            verify { recruitmentRepository.save(any<Recruitment>()) }
            verify { recruitmentItemRepository.deleteAll(mutableListOf()) }
            verify { recruitmentItemRepository.saveAll(any<List<RecruitmentItem>>()) }
        }

        @Test
        fun `지원 항목이 없는 지원을 수정하면 지원만 수정한다`() {
            every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns emptyList()

            recruitmentService.save(createRecruitmentData(id = 1L))

            verify { recruitmentRepository.save(createRecruitment(id = 1L)) }
            verify { recruitmentItemRepository.deleteAll(mutableListOf()) }
            verify { recruitmentItemRepository.saveAll(mutableListOf()) }
        }

        @Test
        fun `지원 항목이 있는 지원에 대한 일부 지원 항목을 삭제하면 나머지 지원 항목만 수정한다`() {
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

        @Test
        fun `지원 항목이 있는 지원에 대한 모든 지원 항목을 삭제하면 모든 지원 항목을 삭제한다`() {
            every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(1L) } returns listOf(
                createRecruitmentItem(id = 1L)
            )

            recruitmentService.save(createRecruitmentData(id = 1L))

            verify { recruitmentRepository.save(createRecruitment(id = 1L)) }
            verify { recruitmentItemRepository.deleteAll(listOf(createRecruitmentItem(id = 1L))) }
            verify { recruitmentItemRepository.saveAll(mutableListOf()) }
        }

    }

    @Test
    fun `지원서가 존재하는 모집을 삭제하는 경우 예외를 던진다`() {
        every { recruitmentRepository.findByIdOrNull(1L) } returns createRecruitment(id = 1L, recruitable = false)
        every { applicationFormRepository.existsByRecruitmentId(1L) } returns true

        assertThrows<IllegalStateException> { recruitmentService.deleteById(1L) }
    }

    @Test
    fun `모집 삭제 시 평가, 평가 항목, 모집 항목을 함께 삭제한다`() {
        every { recruitmentRepository.findByIdOrNull(1L) } returns createRecruitment(id = 1L, recruitable = false)
        every { applicationFormRepository.existsByRecruitmentId(1L) } returns false
        every { recruitmentItemRepository.findAllByRecruitmentId(1L) } returns listOf(
            createRecruitmentItem(recruitmentId = 1L, id = 2L)
        )
        every { evaluationRepository.findAllByRecruitmentId(1L) } returns listOf(
            createEvaluation(recruitmentId = 1L, id = 3L)
        )
        every { evaluationItemRepository.findAllByEvaluationId(3L) } returns listOf(
            createEvaluationItem(evaluationId = 3L, id = 4L)
        )
        every { recruitmentRepository.delete(createRecruitment(id = 1L)) } returns Unit
        every { recruitmentItemRepository.deleteInBatch(listOf(createRecruitmentItem(id = 2L))) } returns Unit
        every { evaluationRepository.deleteInBatch(listOf(createEvaluation(id = 3L))) } returns Unit
        every { evaluationItemRepository.deleteInBatch(listOf(createEvaluationItem(id = 4L))) } returns Unit

        recruitmentService.deleteById(1L)

        verify { recruitmentRepository.delete(createRecruitment(id = 1L)) }
        verify { recruitmentItemRepository.deleteInBatch(listOf(createRecruitmentItem(id = 2L))) }
        verify { evaluationRepository.deleteInBatch(listOf(createEvaluation(id = 3L))) }
        verify { evaluationItemRepository.deleteInBatch(listOf(createEvaluationItem(id = 4L))) }
    }
}
