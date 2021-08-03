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

@IntegrationTest
class ApplicationFormIntegrationTest(
    private val applicationFormService: ApplicationFormService,
    private val applicantRepository: ApplicantRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository
) {
    @Test
    fun `아직 지원하지 않은 경우 단독 모집에 지원 가능하다`() {
        val recruitment = recruitmentRepository.save(createRecruitment(term = null, recruitable = true))
        val applicant = applicantRepository.save(createApplicant())
        assertDoesNotThrow { applicationFormService.create(applicant.id, CreateApplicationFormRequest(recruitment.id)) }
    }

    @Test
    fun `이미 지원한 지원에는 중복으로 지원할 수 없다`() {
        val recruitment = recruitmentRepository.save(createRecruitment(term = null, recruitable = true))
        val applicant = applicantRepository.save(createApplicant())
        applicationFormRepository.save(createApplicationForm(applicant.id, recruitment.id, submitted = true))
        assertThrows<DuplicateApplicationException> {
            applicationFormService.create(applicant.id, CreateApplicationFormRequest(recruitment.id))
        }
    }

    @Test
    fun `동일한 기수의 다른 모집에 지원할 수 없다`() {
        val applicant = applicantRepository.save(createApplicant())
        val appliedRecruitment = recruitmentRepository.save(createRecruitment(term = 1L))
        applicationFormRepository.save(createApplicationForm(applicant.id, appliedRecruitment.id, submitted = true))
        val recruitment = recruitmentRepository.save(createRecruitment(term = 1L, recruitable = true))
        assertThrows<DuplicateApplicationException> {
            applicationFormService.create(applicant.id, CreateApplicationFormRequest(recruitment.id))
        }
    }

    @AfterEach
    internal fun tearDown() {
        applicantRepository.deleteAll()
        applicationFormRepository.deleteAll()
        recruitmentRepository.deleteAll()
    }
}
