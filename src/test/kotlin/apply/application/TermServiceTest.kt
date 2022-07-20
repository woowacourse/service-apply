package apply.application

import apply.domain.recruitment.RecruitmentRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
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
            shouldNotThrowAny { termService.save(TermData("3기")) }
        }

        context("기본 기수명으로") {
            it("기수를 생성할 수 없다") {
                val request = TermData(Term.SINGLE.name)
                shouldThrow<IllegalStateException> { termService.save(request) }
            }
        }

        context("중복된 기수명으로") {
            it("기수를 생성할 수 없다") {
                every { termRepository.existsByName(any()) } returns true
                shouldThrow<IllegalStateException> { termService.save(TermData("3기")) }
            }
        }

        it("기수를 조회한다") {
            val terms = listOf(Term("3기"), Term("4기"))
            every { termRepository.findAll() } returns terms
            val responses = termService.findAll()
            responses shouldContainExactly listOf(
                TermResponse(Term.SINGLE),
                TermResponse(terms[0]),
                TermResponse(terms[1])
            )
        }

        context("기수 조회시") {
            it("단독 모집을 포함한다") {
                every { termRepository.findAll() } returns emptyList()
                val responses = termService.findAll()
                responses shouldContain TermResponse(Term.SINGLE)
            }
        }

        it("기수를 삭제한다") {
            every { recruitmentRepository.existsByTermId(any()) } returns false
            every { termRepository.existsById(any()) } returns true
            every { termRepository.deleteById(any()) } just Runs
            shouldNotThrowAny { termService.deleteById(1L) }
        }

        context("모집이 존재하는 기수는") {
            it("삭제할 수 없다") {
                every { recruitmentRepository.existsByTermId(any()) } returns true
                shouldThrow<IllegalStateException> { termService.deleteById(1L) }
            }
        }

        context("존재하지 않는 기수를 삭제하면") {
            it("예외가 발생한다") {
                every { recruitmentRepository.existsByTermId(any()) } returns false
                every { termRepository.existsById(any()) } returns false
                shouldThrow<IllegalArgumentException> { termService.deleteById(1L) }
            }
        }
    }
})
