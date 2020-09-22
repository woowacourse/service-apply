package apply.application

import apply.domain.answer.Answer
import apply.domain.answer.AnswerRepository
import apply.domain.applicationForm.ApplicationForm
import apply.domain.applicationForm.ApplicationFormRepository
import org.springframework.stereotype.Service

@Service
class ApplicationFormService(
        private val applicationFormRepository: ApplicationFormRepository,
        private val answerRepository: AnswerRepository
) {
    fun saveOrUpdate(applicantId: Long, recruitmentId: Long, applicationFormRequest: ApplicationFormRequest) {
        var applicationForm: ApplicationForm? = applicationFormRepository.findByApplicantIdAndRecruitmentId(applicantId, recruitmentId)
        if (applicationForm == null) {
            applicationForm = ApplicationForm(applicantId, recruitmentId, applicationFormRequest.referenceUrl)
        } else {
            applicationForm.update(applicationFormRequest.referenceUrl)
        }
        if (applicationFormRequest.isSubmit) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
        answerRepository.deleteAllByApplicationFormId(applicationForm.id)
        val answers: List<Answer> = applicationFormRequest.answers.map { answer -> Answer(answer.contents, applicationForm.id, answer.recruitmentItemId) }
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