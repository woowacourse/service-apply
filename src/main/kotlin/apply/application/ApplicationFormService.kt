package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import org.springframework.stereotype.Service
import support.createLocalDateTime
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Transactional
@Service
class ApplicationFormService(
        private val applicationFormRepository: ApplicationFormRepository,
        private val applicantService: ApplicantService
) {
    fun findAllByRecruitmentId(recruitmentId: Long): List<ApplicationForm> =
            applicationFormRepository.findByRecruitmentId(recruitmentId)

    fun getByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): ApplicationForm =
            applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
                    ?: throw IllegalArgumentException()

    @PostConstruct
    private fun populateDummy() {
        if (applicationFormRepository.count() != 0L) {
            return
        }
        val applicationForms = listOf(
                ApplicationForm(
                        referenceUrl = "",
                        submitted = true,
                        createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                        modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                        submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                        recruitmentId = 1L,
                        applicantId = 1L,
                        answers = Answers(
                                mutableListOf(
                                        Answer("고객에게 가치를 전달하고 싶습니다.", 1L),
                                        Answer("도전, 끈기", 2L)
                                )
                        )
                ),
                ApplicationForm(
                        referenceUrl = "https://www.google.com",
                        submitted = true,
                        createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                        modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                        submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                        recruitmentId = 1L,
                        applicantId = 2L,
                        answers = Answers(
                                mutableListOf(
                                        Answer("스타트업을 하고 싶습니다.", 1L),
                                        Answer("책임감", 2L)
                                )
                        )
                )
        )
        applicationFormRepository.saveAll(applicationForms)
    }

    fun save(applicantId: Long, recruitmentId: Long, applicationFormSaveRequest: ApplicationFormSaveRequest) {
        if (applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId) != null) {
            throw IllegalAccessException("이미 저장된 지원서가 있습니다.")
        }
        val answers = Answers(applicationFormSaveRequest.answers.map { answer -> Answer(answer.contents, answer.recruitmentItemId) }.toMutableList())
        val applicationForm = ApplicationForm(applicantId, recruitmentId, applicationFormSaveRequest.referenceUrl, answers)

        if (applicationFormSaveRequest.isSubmit) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
    }

    fun update(applicantId: Long, recruitmentId: Long, applicationFormUpdateRequest: ApplicationFormUpdateRequest) {
        val applicationForm: ApplicationForm = applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
                ?: throw IllegalAccessException("저장된 지원서가 없습니다.")
        val answers = Answers(applicationFormUpdateRequest.answers.map { answer -> Answer(answer.contents, answer.recruitmentItemId) }.toMutableList())
        applicationForm.update(applicationFormUpdateRequest.referenceUrl, answers)
        applicantService.changePassword(applicantId, applicationFormUpdateRequest.password)

        if (applicationFormUpdateRequest.isSubmit) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
    }

    fun getForm(applicantId: Long, recruitmentId: Long): ApplicationFormResponse {
        val form = applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
                ?: throw NoSuchElementException("해당하는 지원서가 없습니다.")
        val answers = form.answers.items.map{answer -> AnswerResponse(answer.contents, answer.recruitmentItemId)}
        return ApplicationFormResponse(
                form.id,
                form.recruitmentId,
                form.referenceUrl,
                form.submitted,
                answers,
                form.createdDateTime,
                form.modifiedDateTime,
                form.submittedDateTime
        )
    }
}
