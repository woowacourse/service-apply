package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class ApplicationFormService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository
) {

    fun findAllByRecruitmentId(recruitmentId: Long): List<ApplicationForm> =
        applicationFormRepository.findByRecruitmentId(recruitmentId)

    fun getByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): ApplicationForm =
        applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
            ?: throw IllegalArgumentException("해당하는 지원서가 없습니다.")

    fun getAllByApplicantId(applicantId: Long): List<MyApplicationFormResponse> =
        applicationFormRepository.findAllByApplicantId(applicantId).map(::MyApplicationFormResponse)

    fun create(applicantId: Long, request: CreateApplicationFormRequest) {
        checkRecruitment(request.recruitmentId)
        require(!applicationFormRepository.existsByRecruitmentIdAndApplicantId(request.recruitmentId, applicantId)) {
            "이미 지원한 이력이 있습니다."
        }
        val applicationForm = ApplicationForm(applicantId, request.recruitmentId)
        applicationFormRepository.save(applicationForm)
    }

    fun update(applicantId: Long, request: UpdateApplicationFormRequest) {
        checkRecruitment(request.recruitmentId)
        val applicationForm = getByRecruitmentIdAndApplicantId(request.recruitmentId, applicantId)
        val answers = Answers(
            request.answers.map {
                Answer(
                    it.contents,
                    it.recruitmentItemId
                )
            }.toMutableList()
        )
        applicationForm.update(request.referenceUrl, answers)
        if (request.submitted) {
            require(!applicationFormRepository.existsByApplicantIdAndSubmittedTrue(applicantId)) {
                "이미 제출 완료한 지원서가 존재하여 제출할 수 없습니다."
            }
            applicationForm.submit()
        }
    }

    fun findForm(applicantId: Long, recruitmentId: Long): ApplicationFormResponse {
        val applicationForm = getByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
        return ApplicationFormResponse(applicationForm)
    }

    private fun checkRecruitment(recruitmentId: Long) {
        val recruitment = recruitmentRepository.findByIdOrNull(recruitmentId)
        requireNotNull(recruitment) {
            "지원하는 모집이 존재하지 않습니다."
        }
        check(recruitment.isRecruiting) {
            "지원 불가능한 모집입니다."
        }
    }
}
