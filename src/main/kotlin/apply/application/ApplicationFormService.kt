package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import org.springframework.stereotype.Service
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

    fun save(applicantId: Long, applicationFormSaveRequest: ApplicationFormSaveRequest) {
        if (applicationFormRepository.existsByRecruitmentIdAndApplicantId(
            applicationFormSaveRequest.recruitmentId,
            applicantId
        )
        ) {
            throw IllegalArgumentException("이미 저장된 지원서가 있습니다.")
        }
        val answers = Answers(
            applicationFormSaveRequest.answers.map {
                Answer(
                    it.contents,
                    it.recruitmentItemId
                )
            }.toMutableList()
        )
        val applicationForm =
            ApplicationForm(
                applicantId,
                applicationFormSaveRequest.recruitmentId,
                applicationFormSaveRequest.referenceUrl,
                answers
            )

        if (applicationFormSaveRequest.isSubmitted) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
    }

    fun update(applicantId: Long, applicationFormUpdateRequest: ApplicationFormUpdateRequest) {
        val applicationForm: ApplicationForm =
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                applicationFormUpdateRequest.recruitmentId,
                applicantId
            )
                ?: throw IllegalArgumentException("저장된 지원서가 없습니다.")
        val answers = Answers(
            applicationFormUpdateRequest.answers.map {
                Answer(
                    it.contents,
                    it.recruitmentItemId
                )
            }.toMutableList()
        )
        applicationForm.update(applicationFormUpdateRequest.referenceUrl, answers)
        applicantService.changePassword(applicantId, applicationFormUpdateRequest.password)

        if (applicationFormUpdateRequest.isSubmitted) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
    }

    fun getForm(applicantId: Long, recruitmentId: Long): ApplicationFormResponse {
        val form = applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
            ?: throw IllegalArgumentException("해당하는 지원서가 없습니다.")
        val answers = form.answers.items.map { AnswerResponse(it.contents, it.recruitmentItemId) }
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
