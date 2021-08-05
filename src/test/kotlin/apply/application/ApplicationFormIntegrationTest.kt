package apply.application

import apply.createApplicant
import apply.createApplicationForm
import apply.createRecruitment
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.applicationform.DuplicateApplicationException
import apply.domain.recruitment.RecruitmentRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import support.test.IntegrationTest
import java.time.LocalDateTime

@IntegrationTest
class ApplicationFormIntegrationTest(
    private val applicationFormService: ApplicationFormService,
    private val applicantRepository: ApplicantRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository
) {
    @Test
    fun `아직 지원하지 않은 경우 단독 모집에 지원 가능하다`() {
        val recruitment = recruitmentRepository.save(createRecruitment(termId = 0L, recruitable = true))
        val applicant = applicantRepository.save(createApplicant())
        assertDoesNotThrow { applicationFormService.create(applicant.id, CreateApplicationFormRequest(recruitment.id)) }
    }

    @Test
    fun `이미 지원한 지원에는 중복으로 지원할 수 없다`() {
        val recruitment = recruitmentRepository.save(createRecruitment(termId = 0L, recruitable = true))
        val applicant = applicantRepository.save(createApplicant())
        applicationFormRepository.save(
            createApplicationForm(
                applicant.id,
                recruitment.id,
                submitted = true,
                submittedDateTime = LocalDateTime.now()
            )
        )
        assertThrows<DuplicateApplicationException> {
            applicationFormService.create(applicant.id, CreateApplicationFormRequest(recruitment.id))
        }
    }

    @Test
    fun `동일한 기수의 다른 모집에 지원할 수 없다`() {
        val applicant = applicantRepository.save(createApplicant())
        val appliedRecruitment = recruitmentRepository.save(createRecruitment(termId = 1L))
        applicationFormRepository.save(
            createApplicationForm(
                applicant.id,
                appliedRecruitment.id,
                submitted = true,
                submittedDateTime = LocalDateTime.now()
            )
        )
        val recruitment = recruitmentRepository.save(createRecruitment(termId = 1L, recruitable = true))
        assertThrows<DuplicateApplicationException> {
            applicationFormService.create(applicant.id, CreateApplicationFormRequest(recruitment.id))
        }
    }

    @Test
    fun `동일한 기수의 다른 모집에 임시 저장되어 있어도 지원서를 제출할 수 있다`() {
        val applicant = applicantRepository.save(createApplicant())
        val appliedRecruitment = recruitmentRepository.save(createRecruitment(termId = 1L))
        applicationFormRepository.save(createApplicationForm(applicant.id, appliedRecruitment.id, submitted = false))
        val recruitment = recruitmentRepository.save(createRecruitment(termId = 1L, recruitable = true))
        applicationFormRepository.save(createApplicationForm(applicant.id, recruitment.id, submitted = false))
        assertDoesNotThrow {
            applicationFormService.update(
                applicant.id,
                UpdateApplicationFormRequest(recruitmentId = recruitment.id, submitted = true)
            )
        }
    }

    @Test
    fun `동일한 기수의 다른 모집에 이미 지원서를 제출한 경우에는 지원서를 제출할 수 없다`() {
        val applicant = applicantRepository.save(createApplicant())
        val appliedRecruitment = recruitmentRepository.save(createRecruitment(termId = 1L))
        applicationFormRepository.save(
            createApplicationForm(
                applicant.id,
                appliedRecruitment.id,
                submitted = true,
                submittedDateTime = LocalDateTime.now()
            )
        )
        val recruitment = recruitmentRepository.save(createRecruitment(termId = 1L, recruitable = true))
        applicationFormRepository.save(createApplicationForm(applicant.id, recruitment.id, submitted = false))
        assertThrows<DuplicateApplicationException> {
            applicationFormService.update(
                applicant.id,
                UpdateApplicationFormRequest(recruitmentId = recruitment.id, submitted = true)
            )
        }
    }

    @AfterEach
    internal fun tearDown() {
        applicantRepository.deleteAll()
        applicationFormRepository.deleteAll()
        recruitmentRepository.deleteAll()
    }
}
