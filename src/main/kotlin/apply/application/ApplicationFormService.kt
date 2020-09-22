package apply.application

import apply.domain.answer.Answer
import apply.domain.answer.AnswerRepository
import apply.domain.applicationForm.ApplicationForm
import apply.domain.applicationForm.ApplicationFormRepository
import org.springframework.stereotype.Service

@Service
class ApplicationFormService(
        private val applicationFormRepository: ApplicationFormRepository,
        private val answerRepository: AnswerRepository,
        private val applicantService: ApplicantService
) {
    fun save(applicantId: Long, recruitmentId: Long, applicationFormSaveRequest: ApplicationFormSaveRequest) {
        if (applicationFormRepository.findByApplicantIdAndRecruitmentId(applicantId, recruitmentId) != null) {
            throw IllegalAccessException("이미 저장된 지원서가 있습니다.")
        }
        val applicationForm = ApplicationForm(applicantId, recruitmentId, applicationFormSaveRequest.referenceUrl)
        if (applicationFormSaveRequest.isSubmit) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
        val answers: List<Answer> = applicationFormSaveRequest.answers.map { answer -> Answer(answer.contents, applicationForm.id, answer.recruitmentItemId) }
        answerRepository.saveAll(answers)
    }

    fun update(applicantId: Long, recruitmentId: Long, applicationFormUpdateRequest: ApplicationFormUpdateRequest) {
        val applicationForm: ApplicationForm = applicationFormRepository.findByApplicantIdAndRecruitmentId(applicantId, recruitmentId)
                ?: throw IllegalAccessException("저장된 지원서가 없습니다.")
        applicationForm.update(applicationFormUpdateRequest.referenceUrl)
        applicantService.changePassword(applicantId, applicationFormUpdateRequest.password);

        if (applicationFormUpdateRequest.isSubmit) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
        answerRepository.deleteAllByApplicationFormId(applicationForm.id)
        val answers: List<Answer> = applicationFormUpdateRequest.answers.map { answer -> Answer(answer.contents, applicationForm.id, answer.recruitmentItemId) }
        answerRepository.saveAll(answers)
    }

    fun getForm(applicantId: Long, recruitmentId: Long): ApplicationFormResponse {
        val form = applicationFormRepository.findByApplicantIdAndRecruitmentId(applicantId, recruitmentId)
                ?: throw NoSuchElementException("해당하는 지원서가 없습니다.")
        val answers = answerRepository.findAllByApplicationFormId(form.id).map { answer -> AnswerResponse(answer.contents, answer.applicationFormId) }
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