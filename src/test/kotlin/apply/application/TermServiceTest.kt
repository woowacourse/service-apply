package apply.application

import apply.domain.recruitment.RecruitmentRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest

@UnitTest
internal class TermServiceTest {
    @MockK
    lateinit var termRepository: TermRepository

    @MockK
    lateinit var recruitmentRepository: RecruitmentRepository

    private lateinit var termService: TermService

    @BeforeEach
    internal fun setUp() {
        termService = TermService(termRepository, recruitmentRepository)
    }

    @Test
    fun `기수를 생성한다`() {
        every { termRepository.save(any()) } returns Term("3기")
        assertDoesNotThrow { termService.save(TermData("3기")) }
    }

    @Test
    fun `기본 기수명으로 기수를 생성할 수 없다`() {
        val request = TermData(Term.SINGLE.name)
        assertThrows<IllegalStateException> { termService.save(request) }
    }

    @Test
    fun `기수를 삭제한다`() {
        val termId = 1L
        every { recruitmentRepository.existsByTermId(termId) } returns false
        every { termRepository.existsById(termId) } returns true
        every { termRepository.deleteById(any()) } just Runs
        assertDoesNotThrow { termService.deleteById(termId) }
    }

    @Test
    fun `모집이 존재하는 기수는 삭제할 수 없다`() {
        val termId = 1L
        every { recruitmentRepository.existsByTermId(termId) } returns true
        assertThrows<IllegalStateException> { termService.deleteById(termId) }
    }

    @Test
    fun `존재하지 않는 기수를 삭제하면 예외가 발생한다`() {
        val termId = 1L
        every { recruitmentRepository.existsByTermId(termId) } returns false
        every { termRepository.existsById(termId) } returns false
        assertThrows<IllegalStateException> { termService.deleteById(termId) }
    }
}
