package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormAnswer
import apply.domain.applicationform.ApplicationFormAnswers
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.applicationform.ApplicationValidator
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitment.getOrThrow
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicationFormService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val applicationValidator: ApplicationValidator
) {
    fun create(memberId: Long, request: CreateApplicationFormRequest): ApplicationFormResponse {
        val recruitment = findApplicableRecruitment(request.recruitmentId)
        check(!applicationFormRepository.existsByRecruitmentIdAndMemberId(recruitment.id, memberId)) {
            "이미 작성한 지원서가 있습니다."
        }
        return applicationFormRepository
            .save(ApplicationForm(memberId, recruitment.id, applicationValidator))
            .let(::ApplicationFormResponse)
    }

    fun update(memberId: Long, request: UpdateApplicationFormRequest) {
        val recruitment = findApplicableRecruitment(request.recruitmentId)
        validateRequest(request)
        val applicationForm = findByRecruitmentIdAndMemberId(recruitment.id, memberId)
        val answers = ApplicationFormAnswers(
            request.answers.map {
                ApplicationFormAnswer(it.contents, it.recruitmentItemId)
            }.toMutableList()
        )
        applicationForm.update(request.referenceUrl, answers)
        if (request.submitted) {
            applicationForm.submit(applicationValidator)
        }
        applicationFormRepository.save(applicationForm)
    }

    fun getMyApplicationForms(memberId: Long): List<MyApplicationFormResponse> =
        applicationFormRepository.findAllByMemberId(memberId).map(::MyApplicationFormResponse)

    fun getApplicationForm(memberId: Long, recruitmentId: Long): ApplicationFormResponse {
        val applicationForm = findByRecruitmentIdAndMemberId(recruitmentId, memberId)
        check(!applicationForm.submitted) {
            "이미 제출한 지원서는 열람할 수 없습니다."
        }
        return ApplicationFormResponse(applicationForm)
    }

    private fun findByRecruitmentIdAndMemberId(recruitmentId: Long, memberId: Long): ApplicationForm =
        applicationFormRepository.findByRecruitmentIdAndMemberId(recruitmentId, memberId)
            ?: throw NoSuchElementException("해당하는 지원서가 없습니다.")

    private fun findApplicableRecruitment(recruitmentId: Long): Recruitment {
        val recruitment = recruitmentRepository.getOrThrow(recruitmentId)
        check(recruitment.isRecruiting) {
            "지원 불가능한 모집입니다."
        }
        return recruitment
    }

    private fun validateRequest(request: UpdateApplicationFormRequest) {
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(request.recruitmentId)
        if (request.submitted) {
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
