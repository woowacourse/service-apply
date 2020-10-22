package apply.domain.applicationform

import apply.application.ApplicantAndFormResponse
import apply.createApplicant
import apply.createApplicationForm
import apply.domain.applicant.ApplicantRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.jdbc.Sql
import support.createLocalDateTime
import support.test.RepositoryTest
import support.toSort

@Sql("/truncate.sql")
@RepositoryTest
class ApplicantAndFormResponseTest(
    private val applicationFormRepository: ApplicationFormRepository,
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    private lateinit var applicationForms: List<ApplicationForm>

    @BeforeEach
    internal fun setUp() {
        applicationForms = listOf(
            createApplicationForm(applicantId = 1L, recruitmentId = 2L).apply {
                submit(createLocalDateTime(year = 2020, month = 10, dayOfMonth = 2))
            },
            createApplicationForm(applicantId = 2L, recruitmentId = 2L).apply {
                submit(createLocalDateTime(year = 2020, month = 11, dayOfMonth = 23))
            },
            createApplicationForm(applicantId = 3L, recruitmentId = 2L).apply {
                submit(createLocalDateTime(year = 2020, month = 9, dayOfMonth = 22))
            },
            createApplicationForm(applicantId = 4L, recruitmentId = 2L).apply {
                submit(createLocalDateTime(year = 2020, month = 10, dayOfMonth = 30))
            },
            createApplicationForm(applicantId = 5L, recruitmentId = 2L),
            createApplicationForm(applicantId = 5L, recruitmentId = 1L).apply { submit() }
        )
        applicationFormRepository.saveAll(applicationForms)
    }

    @Test
    fun `지원자, 지원서 정보를 모집ID로 검색어 없이 함께 조회한다`() {
        val applicants = listOf(
            createApplicant(id = 1L, name = "이름1", email = "AAA@AAa.com"),
            createApplicant(id = 2L, name = "이름2", email = "BBB@BBB.com"),
            createApplicant(id = 3L, name = "이름3", email = "CCC@CCC.com"),
            createApplicant(id = 4L, name = "이름4", email = "DDD@DDD.com"),
            createApplicant(id = 5L, name = "이름 제출 안함", email = "EEE@EEE.com")
        )

        applicantRepository.saveAll(applicants)
        val result = applicationFormRepository.findByRecruitmentIdAndKeyword(
            2L,
            "",
            PageRequest.of(0, 10)
        ).content

        assertThat(result).hasSize(4)
    }

    @Test
    fun `지원자, 지원서, 부정행위자 정보를 모집ID로 검색어로 함께 조회한다`() {
        val applicants = listOf(
            createApplicant(id = 1L, name = "이름1", email = "AAA@AAa.com"),
            createApplicant(id = 2L, name = "이름2", email = "BBB@BBB.com"),
            createApplicant(id = 3L, name = "검색어 미포함", email = "CCC@CCC.com"),
            createApplicant(id = 4L, name = "검색어 미포함2", email = "DDD@DDD.com"),
            createApplicant(id = 5L, name = "이름 제출 안함", email = "EEE@EEE.com")
        )
        applicantRepository.saveAll(applicants)
        cheaterRepository.save(Cheater(2L))

        val result = applicationFormRepository.findByRecruitmentIdAndKeyword(
            2L,
            "이름",
            PageRequest.of(0, 2)
        ).content
        assertThat(result).usingFieldByFieldElementComparator()
            .isEqualTo(
                listOf(
                    ApplicantAndFormResponse(applicants[0], false, applicationForms[0]),
                    ApplicantAndFormResponse(applicants[1], true, applicationForms[1])
                )
            )
    }

    @Test
    fun `지원자, 지원서, 부정행위자 정보를 함께 페이징하여 조회한다`() {
        val applicants = listOf(
            createApplicant(id = 1L, name = "이름1", email = "AAA@AAa.com"),
            createApplicant(id = 2L, name = "이름2", email = "BBB@BBB.com"),
            createApplicant(id = 3L, name = "이름3", email = "CCC@CCC.com"),
            createApplicant(id = 4L, name = "이름4", email = "DDD@DDD.com")
        )
        applicantRepository.saveAll(applicants)

        val result = applicationFormRepository.findByRecruitmentIdAndKeyword(
            2L,
            "이름",
            PageRequest.of(1, 3)
        ).content

        assertThat(result).hasSize(1)
        assertThat(result).usingFieldByFieldElementComparator()
            .isEqualTo(
                listOf(
                    ApplicantAndFormResponse(applicants[3], false, applicationForms[3])
                )
            )
    }

    @Test
    fun `지원자, 지원서, 부정행위자 정보를 함께 Email로 정렬하여 조회한다`() {
        val applicants = listOf(
            createApplicant(id = 1L, name = "이름1", email = "AAA@AAa.com"),
            createApplicant(id = 2L, name = "이름2", email = "DDD@DDD.com"),
            createApplicant(id = 3L, name = "이름3", email = "BBB@BBB.com"),
            createApplicant(id = 4L, name = "이름4", email = "CCC@CCC.com")
        )
        applicantRepository.saveAll(applicants)

        val result = applicationFormRepository.findByRecruitmentIdAndKeyword(
            2L,
            "이름",
            PageRequest.of(
                0,
                4,
                LinkedHashMap<String, String>().apply { put("information.email", "ASCENDING") }.toSort()
            )
        ).content

        assertThat(result).usingFieldByFieldElementComparator()
            .isEqualTo(
                listOf(
                    ApplicantAndFormResponse(applicants[0], false, applicationForms[0]),
                    ApplicantAndFormResponse(applicants[2], false, applicationForms[2]),
                    ApplicantAndFormResponse(applicants[3], false, applicationForms[3]),
                    ApplicantAndFormResponse(applicants[1], false, applicationForms[1])
                )
            )
    }

    @Test
    fun `지원자, 지원서, 부정행위자 정보를 함께 지원 일시로 정렬하여 조회한다`() {
        val applicants = listOf(
            createApplicant(id = 1L, name = "이름1", email = "AAA@AAa.com"),
            createApplicant(id = 2L, name = "이름2", email = "BBB@BBB.com"),
            createApplicant(id = 3L, name = "이름3", email = "CCC@CCC.com"),
            createApplicant(id = 4L, name = "이름4", email = "DDD@DDD.com")
        )
        applicantRepository.saveAll(applicants)

        val result = applicationFormRepository.findByRecruitmentIdAndKeyword(
            2L,
            "이름",
            PageRequest.of(
                0,
                4,
                LinkedHashMap<String, String>().apply { put("f.submittedDateTime", "ASCENDING") }.toSort()
            )
        ).content

        assertThat(result).usingFieldByFieldElementComparator()
            .isEqualTo(
                listOf(
                    ApplicantAndFormResponse(applicants[2], false, applicationForms[2]),
                    ApplicantAndFormResponse(applicants[0], false, applicationForms[0]),
                    ApplicantAndFormResponse(applicants[3], false, applicationForms[3]),
                    ApplicantAndFormResponse(applicants[1], false, applicationForms[1])
                )
            )
    }

    @Test
    fun `지원자, 지원서, 부정행위자 정보를 함께 부정행위자 여부로 정렬하여 조회한다`() {
        val applicants = listOf(
            createApplicant(id = 1L, name = "이름1", email = "AAA@AAa.com"),
            createApplicant(id = 2L, name = "이름2", email = "BBB@BBB.com"),
            createApplicant(id = 3L, name = "이름3", email = "CCC@CCC.com"),
            createApplicant(id = 4L, name = "이름4", email = "DDD@DDD.com")
        )
        applicantRepository.saveAll(applicants)
        cheaterRepository.save(Cheater(3L))

        val result = applicationFormRepository.findByRecruitmentIdAndKeyword(
            2L,
            "이름",
            PageRequest.of(0, 4, LinkedHashMap<String, String>().apply { put("c.id", "ASCENDING") }.toSort())
        ).content

        assertThat(result[3]).usingRecursiveComparison()
            .isEqualTo(ApplicantAndFormResponse(applicants[2], true, applicationForms[2]))
    }
}
