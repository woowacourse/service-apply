package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormAnswer
import apply.domain.applicationform.ApplicationFormAnswers
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class ApplicationFormService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository
) {
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
        validateRequest(request, applicantId)
        val applicationForm = findByRecruitmentIdAndApplicantId(request.recruitmentId, applicantId)
        val answers = ApplicationFormAnswers(
            request.answers.map {
                ApplicationFormAnswer(
                    it.contents,
                    it.recruitmentItemId
                )
            }.toMutableList()
        )
        applicationForm.update(request.referenceUrl, answers)
        if (request.submitted) {
            applicationForm.submit()
        }
    }

    fun getMyApplicationForms(applicantId: Long): List<MyApplicationFormResponse> =
        applicationFormRepository.findAllByApplicantId(applicantId).map(::MyApplicationFormResponse)

    fun getApplicationForm(applicantId: Long, recruitmentId: Long): ApplicationFormResponse {
        val applicationForm = findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
        check(!applicationForm.submitted) {
            "이미 제출한 지원서는 열람할 수 없습니다."
        }
        return ApplicationFormResponse(applicationForm)
    }

    private fun findByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): ApplicationForm =
        applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
            ?: throw IllegalArgumentException("해당하는 지원서가 없습니다.")

    private fun checkRecruitment(recruitmentId: Long) {
        val recruitment = recruitmentRepository.findByIdOrNull(recruitmentId)
        requireNotNull(recruitment) {
            "지원하는 모집이 존재하지 않습니다."
        }
        check(recruitment.isRecruiting) {
            "지원 불가능한 모집입니다."
        }
    }

    private fun validateRequest(request: UpdateApplicationFormRequest, applicantId: Long) {
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(request.recruitmentId)
        if (request.submitted) {
            require(!applicationFormRepository.existsByApplicantIdAndSubmittedTrue(applicantId)) {
                "이미 제출 완료한 지원서가 존재하여 제출할 수 없습니다."
            }
            require(request.answers.all { it.contents.isNotBlank() } && (recruitmentItems.size == request.answers.size)) {
                "작성하지 않은 문항이 존재합니다."
            }
        }
        require(
            request.answers.all { answer ->
                recruitmentItems.first { answer.recruitmentItemId == it.id }.maximumLength >= answer.contents.length
            }
        ) {
            "모집 문항의 최대 글자 수를 초과하였습니다."
        }
    }
}
