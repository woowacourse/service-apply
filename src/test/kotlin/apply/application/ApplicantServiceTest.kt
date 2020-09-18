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
        given(cheaterRepository.existsByApplicantId(1L)).willReturn(true)

        val founds = applicantService.findAll()

        assertAll(
            { assertThat(founds[0].id).isEqualTo(applicants[0].id) },
            { assertThat(founds[0].name).isEqualTo(applicants[0].name) },
            { assertThat(founds[0].email).isEqualTo(applicants[0].email) },
            { assertThat(founds[0].phoneNumber).isEqualTo(applicants[0].phoneNumber) },
            { assertThat(founds[0].birthday).isEqualTo(applicants[0].birthday) },
            { assertThat(founds[0].gender).isEqualTo(applicants[0].gender) },
            { assertThat(founds[0].isCheater).isEqualTo(true) }
        )
    }
}
