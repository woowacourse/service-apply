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
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.springframework.data.repository.findByIdOrNull
import support.test.UnitTest


@UnitTest
internal class RecruitmentServiceTest : AnnotationSpec() {
    @MockK
    lateinit var recruitmentRepository: RecruitmentRepository

    @MockK
    private lateinit var recruitmentItemRepository: RecruitmentItemRepository

    @MockK
    private lateinit var termRepository: TermRepository

    private lateinit var recruitmentService: RecruitmentService

    @BeforeEach
    internal fun setUp() {
        recruitmentService = RecruitmentService(recruitmentRepository, recruitmentItemRepository, termRepository)
    }

    @Nested
    inner class Save : AnnotationSpec() {
        @BeforeEach
        internal fun setUp() {
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
    fun `모집 중인 모집은 삭제할 수 없다`() {
        val recruitment = createRecruitment(recruitable = true)
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        shouldThrowExactly<IllegalStateException> { recruitmentService.deleteById(recruitment.id) }
    }
}
