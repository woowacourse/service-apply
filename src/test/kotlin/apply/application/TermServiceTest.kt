package apply.application

import apply.domain.recruitment.RecruitmentRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest

@UnitTest
internal class TermServiceTest : DescribeSpec({
    var termRepository: TermRepository = mockk()
    var recruitmentRepository: RecruitmentRepository = mockk()
    lateinit var termService: TermService

    beforeEach {
        termService = TermService(termRepository, recruitmentRepository)
    }

    describe("TermService") {
        it("기수를 생성한다") {
            every { termRepository.save(any()) } returns Term("3기")
            every { termRepository.existsByName(any()) } returns false
            assertDoesNotThrow { termService.save(TermData("3기")) }
        }

        context("기본 기수명으로") {
            it("기수를 생성할 수 없다") {
                val request = TermData(Term.SINGLE.name)
                assertThrows<IllegalStateException> { termService.save(request) }
            }
        }

        context("중복된 기수명으로") {
            it("기수를 생성할 수 없다") {
                every { termRepository.existsByName(any()) } returns true
                assertThrows<IllegalStateException> { termService.save(TermData("3기")) }
            }
        }

        it("기수를 조회한다") {
            val terms = listOf(Term("3기"), Term("4기"))
            every { termRepository.findAll() } returns terms
            val responses = termService.findAll()
            assertThat(responses).containsExactlyInAnyOrder(
                TermResponse(Term.SINGLE),
                TermResponse(terms[0]),
                TermResponse(terms[1])
            )
        }

        context("기수 조회시") {
            it("단독 모집을 포함한다") {
                every { termRepository.findAll() } returns emptyList()
                val responses = termService.findAll()
                assertThat(responses).contains(TermResponse(Term.SINGLE))
            }
        }

        it("기수를 삭제한다") {
            every { recruitmentRepository.existsByTermId(any()) } returns false
            every { termRepository.existsById(any()) } returns true
            every { termRepository.deleteById(any()) } just Runs
            assertDoesNotThrow { termService.deleteById(1L) }
        }

        context("모집이 존재하는 기수는") {
            it("삭제할 수 없다") {
                every { recruitmentRepository.existsByTermId(any()) } returns true
                assertThrows<IllegalStateException> { termService.deleteById(1L) }
            }
        }

        context("존재하지 않는 기수를 삭제하면") {
            it("예외가 발생한다") {
                every { recruitmentRepository.existsByTermId(any()) } returns false
                every { termRepository.existsById(any()) } returns false
                assertThrows<IllegalArgumentException> { termService.deleteById(1L) }
            }
        }
    }
}) {
    // @MockK
    // lateinit var termRepository: TermRepository
    //
    // @MockK
    // lateinit var recruitmentRepository: RecruitmentRepository
    //
    // private lateinit var termService: TermService
    //
    // @BeforeEach
    // internal fun setUp() {
    //     termService = TermService(termRepository, recruitmentRepository)
    // }

    // @Test
    // fun `기수를 생성한다`() {
    //     every { termRepository.save(any()) } returns Term("3기")
    //     every { termRepository.existsByName(any()) } returns false
    //     assertDoesNotThrow { termService.save(TermData("3기")) }
    // }
    //
    // @Test
    // fun `기본 기수명으로 기수를 생성할 수 없다`() {
    //     val request = TermData(Term.SINGLE.name)
    //     assertThrows<IllegalStateException> { termService.save(request) }
    // }
    //
    // @Test
    // fun `중복된 기수명으로 기수를 생성할 수 없다`() {
    //     every { termRepository.existsByName(any()) } returns true
    //     assertThrows<IllegalStateException> { termService.save(TermData("3기")) }
    // }
    //
    // @Test
    // fun `기수를 조회한다`() {
    //     val terms = listOf(Term("3기"), Term("4기"))
    //     every { termRepository.findAll() } returns terms
    //     val responses = termService.findAll()
    //     assertThat(responses).containsExactlyInAnyOrder(
    //         TermResponse(Term.SINGLE),
    //         TermResponse(terms[0]),
    //         TermResponse(terms[1])
    //     )
    // }
    //
    // @Test
    // fun `기수 조회시 단독 모집을 포함한다`() {
    //     every { termRepository.findAll() } returns emptyList()
    //     val responses = termService.findAll()
    //     assertThat(responses).contains(TermResponse(Term.SINGLE))
    // }
    //
    // @Test
    // fun `기수를 삭제한다`() {
    //     every { recruitmentRepository.existsByTermId(any()) } returns false
    //     every { termRepository.existsById(any()) } returns true
    //     every { termRepository.deleteById(any()) } just Runs
    //     assertDoesNotThrow { termService.deleteById(1L) }
    // }
    //
    // @Test
    // fun `모집이 존재하는 기수는 삭제할 수 없다`() {
    //     every { recruitmentRepository.existsByTermId(any()) } returns true
    //     assertThrows<IllegalStateException> { termService.deleteById(1L) }
    // }
    //
    // @Test
    // fun `존재하지 않는 기수를 삭제하면 예외가 발생한다`() {
    //     every { recruitmentRepository.existsByTermId(any()) } returns false
    //     every { termRepository.existsById(any()) } returns false
    //     assertThrows<IllegalArgumentException> { termService.deleteById(1L) }
    // }
}
