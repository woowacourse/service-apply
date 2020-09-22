package apply.application

import apply.domain.applicationForm.ApplicationForm
import apply.domain.applicationForm.ApplicationFormRepository
import org.springframework.stereotype.Service

@Service
class ApplicationFormService(
        private val applicationFormRepository: ApplicationFormRepository
) {
    fun save(applicantId: Long, recruitmentId: Long, applicationFormRequest: ApplicationFormRequest) {
        var applicationForm : ApplicationForm? = applicationFormRepository.findByApplicantIdAndRecruitmentId(applicantId, recruitmentId)
        if (applicationForm == null) {
            applicationForm = ApplicationForm(applicantId, recruitmentId, applicationFormRequest.referenceUrl, applicationFormRequest.answers)
        } else {
            applicationForm.update(applicationFormRequest.referenceUrl, applicationFormRequest.answers)
        }
        applicationFormRepository.save(applicationForm)
    }
}