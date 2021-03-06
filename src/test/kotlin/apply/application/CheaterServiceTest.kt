package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class CheaterServiceTest {
    @Mock
    private lateinit var applicantRepository: ApplicantRepository

    @Mock
    private lateinit var cheaterRepository: CheaterRepository

    private lateinit var cheaterService: CheaterService

    @BeforeEach
    internal fun setUp() {
        cheaterService = CheaterService(applicantRepository, cheaterRepository)
    }

    @Test
    fun `부정 행위자를 추가한다`() {
        given(cheaterRepository.existsByApplicantId(anyLong())).willReturn(false)
        given(cheaterRepository.save(any(Cheater::class.java))).willReturn(Cheater(1L))
        assertDoesNotThrow { cheaterService.save(1L) }
    }

    @Test
    fun `이미 등록된 부정 행위자를 추가하는 경우 예외를 던진다`() {
        given(cheaterRepository.existsByApplicantId(anyLong())).willReturn(true)
        assertThrows<IllegalArgumentException> { cheaterService.save(1L) }
    }
}
