package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantValidateException
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository,
    private val passwordGenerator: PasswordGenerator
) {
    fun findAllByRecruitmentIdAndSubmittedTrue(recruitmentId: Long): List<ApplicantResponse> {
        val applicationForms = applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .associateBy { it.applicantId }
        val cheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }

        return applicantRepository.findAllById(applicationForms.keys).map {
            ApplicantResponse(it, cheaterApplicantIds.contains(it.id), applicationForms.getValue(it.id))
        }
    }

    fun findByRecruitmentIdAndKeyword(recruitmentId: Long, keyword: String): List<ApplicantResponse> =
        findAllByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .filter { it.name.contains(keyword) || it.email.contains(keyword) }

    fun findByNameOrEmail(keyword: String): List<ApplicantBasicResponse> =
        applicantRepository.findByNameContainingOrEmailContaining(keyword, keyword).map {
            ApplicantBasicResponse(it)
        }

    fun getByEmail(email: String): Applicant =
        applicantRepository.findByEmail(email) ?: throw IllegalArgumentException("email=$email 인 유저가 존재하지 않습니다")

    fun resetPassword(request: ResetPasswordRequest): String {
        return applicantRepository.findByNameAndEmailAndBirthday(
            request.name,
            request.email,
            request.birthday
        )?.run {
            passwordGenerator.generate().also { password = Password(it) }
        } ?: throw ApplicantValidateException()
    }

    fun editPassword(applicant: Applicant, request: EditPasswordRequest) {
        applicant.takeIf { it.isSamePassword(request.password) }
            ?.run {
                applicant.password = request.newPassword
                applicantRepository.save(applicant)
            } ?: throw ApplicantValidateException()
    }

    @PostConstruct
    private fun populateDummy() {
        if (applicantRepository.count() != 0L) {
            return
        }
        val applicants = listOf(
            Applicant(
                name = "홍길동",
                email = "a@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 4, 17),
                password = Password("password")
            ),
            Applicant(
                name = "홍길동2",
                email = "b@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.FEMALE,
                birthday = createLocalDate(2020, 5, 5),
                password = Password("password")
            ),
            Applicant(
                name = "홍길동3",
                email = "c@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1),
                password = Password("password")
            ),
            Applicant(
                name = "홍길동4",
                email = "d@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1),
                password = Password("password")
            )
        )
        applicantRepository.saveAll(applicants)
    }
}
