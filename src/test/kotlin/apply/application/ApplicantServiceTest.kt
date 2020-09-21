package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import apply.domain.cheater.CheaterRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import support.createLocalDate

@ExtendWith(MockitoExtension::class)
internal class ApplicantServiceTest {
    @Mock
    private lateinit var applicantRepository: ApplicantRepository

    @Mock
    private lateinit var cheaterRepository: CheaterRepository

    private lateinit var applicantService: ApplicantService

    private val applicants = listOf(
        Applicant(
            id = 1L,
            name = "홍길동1",
            email = "a@email.com",
            phoneNumber = "010-0000-0000",
            gender = Gender.MALE,
            birthday = createLocalDate(2020, 4, 17)
        )
    )

    @BeforeEach
    internal fun setUp() {
        applicantService = ApplicantService(applicantRepository, cheaterRepository)
    }

    @Test
    fun `지원자 정보와 부정 행위자 여부를 함께 제공한다`() {
        given(applicantRepository.findAll()).willReturn(applicants)
        given(cheaterRepository.existsByApplicantId(anyLong())).willReturn(true)

        val founds = applicantService.findAll()

        assertAll(
            { assertThat(founds).usingElementComparatorIgnoringFields("isCheater").isEqualTo(applicants) },
            { assertThat(founds[0].isCheater).isEqualTo(true) }
        )
    }
}
