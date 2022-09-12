package apply.application

import apply.domain.recruitment.RecruitmentRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import support.test.spec.afterRootTest

class TermServiceTest : BehaviorSpec({
    val termRepository = mockk<TermRepository>()
    val recruitmentRepository = mockk<RecruitmentRepository>()

    val termService = TermService(termRepository, recruitmentRepository)

    Given("같은 이름의 기수가 없는 경우") {
        val name = "3기"

        every { termRepository.existsByName(any()) } returns false
        every { termRepository.save(any()) } returns Term(name)

        When("해당 이름으로 기수를 생성하면") {
            val actual = termService.save(TermData(name))

            Then("기수가 생성된다") {
                actual.name shouldBe name
            }
        }
    }

    Given("같은 이름의 기수가 존재하는 경우") {
        every { termRepository.existsByName(any()) } returns true

        When("해당 이름으로 기수를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    termService.save(TermData("3기"))
                }
            }
        }
    }

    Given("기수 이름이 단독 모집인 경우") {
        val name = "단독 모집"

        When("해당 이름으로 기수를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    termService.save(TermData(name))
                }
            }
        }
    }

    Given("기수가 여러 개인 경우") {
        val term1 = Term("3기", 1L)
        val term2 = Term("4기", 2L)

        every { termRepository.findAll() } returns listOf(term1, term2)

        When("모든 기수를 조회하면") {
            val actual = termService.findAll()

            Then("단독 모집을 포함하여 모든 기수를 확인할 수 있다") {
                actual shouldContainExactlyInAnyOrder listOf(
                    TermResponse(Term.SINGLE), TermResponse(term1), TermResponse(term2)
                )
            }
        }
    }

    Given("모집이 등록되지 않은 특정 기수가 있는 경우") {
        every { recruitmentRepository.existsByTermId(any()) } returns false
        every { termRepository.existsById(any()) } returns true
        every { termRepository.deleteById(any()) } just Runs

        When("해당 기수를 삭제하면") {
            Then("기수를 삭제할 수 있다") {
                shouldNotThrowAny {
                    termService.deleteById(1L)
                }
            }
        }
    }

    Given("모집이 등록된 특정 기수가 있는 경우") {
        every { recruitmentRepository.existsByTermId(any()) } returns true

        When("해당 기수를 삭제하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    termService.deleteById(1L)
                }
            }
        }
    }

    Given("기수가 존재하지 않는 경우") {
        every { recruitmentRepository.existsByTermId(any()) } returns false
        every { termRepository.existsById(any()) } returns false

        When("헤딩 기수를 삭제하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    termService.deleteById(1L)
                }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
