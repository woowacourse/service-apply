package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest

@UnitTest
internal class CheaterServiceTest {
    @MockK
    private lateinit var applicantRepository: ApplicantRepository

    @MockK
    private lateinit var cheaterRepository: CheaterRepository
    private lateinit var cheaterService: CheaterService

    @BeforeEach
    internal fun setUp() {
        cheaterService = CheaterService(applicantRepository, cheaterRepository)
    }

    @Test
    fun `부정 행위자를 추가한다`() {
        every { cheaterRepository.existsByApplicantId(ofType(Long::class)) } answers { false }
        every { cheaterRepository.save(ofType(Cheater::class)) } answers { Cheater(1L) }
        assertDoesNotThrow { cheaterService.save(1L) }
    }

    @Test
    fun `이미 등록된 부정 행위자를 추가하는 경우 예외를 던진다`() {
        every { cheaterRepository.existsByApplicantId(ofType(Long::class)) } answers { true }
        assertThrows<IllegalArgumentException> { cheaterService.save(1L) }
    }
}
