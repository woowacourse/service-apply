package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.domain.applicationform.ApplicationForm
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
    fun getByEmail(email: String): Applicant {
        return applicantRepository.findByEmail(email) ?: throw IllegalArgumentException("지원자가 존재하지 않습니다. email: $email")
    }

    fun findAllByRecruitmentIdAndKeyword(recruitmentId: Long, keyword: String): List<ApplicantAndFormResponse> {
        return applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .joinOnApplicantId(applicantRepository.findAllByKeyword(keyword))
    }

    fun findAllByRecruitmentIdAndSubmittedTrue(recruitmentId: Long): List<ApplicantAndFormResponse> {
        return applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(recruitmentId)
            .run { joinOnApplicantId(applicantRepository.findAllById(applicantIds())) }
    }

    private fun List<ApplicationForm>.joinOnApplicantId(applicants: List<Applicant>): List<ApplicantAndFormResponse> {
        val applicantForms = associateBy { it.applicantId }
        val cheaters = cheaterRepository.findAll().map { it.applicantId }
        return applicants
            .filter { applicantForms.containsKey(it.id) }
            .map { ApplicantAndFormResponse(it, cheaters.contains(it.id), applicantForms.getValue(it.id)) }
    }

    private fun List<ApplicationForm>.applicantIds() = map { it.applicantId }

    fun findAllByKeyword(keyword: String): List<ApplicantResponse> {
        return applicantRepository.findAllByKeyword(keyword).map(::ApplicantResponse)
    }

    fun findAllByRecruitmentId(recruitmentId: Long): List<ApplicantAndFormResponse> {
        return applicationFormRepository.findByRecruitmentId(recruitmentId)
            .run { joinOnApplicantId(applicantRepository.findAllById(applicantIds())) }
    }

    fun resetPassword(request: ResetPasswordRequest): String {
        return passwordGenerator.generate().also {
            getByEmail(request.email).resetPassword(request.name, request.birthday, it)
        }
    }

    fun editPassword(id: Long, request: EditPasswordRequest) {
        applicantRepository.getOne(id).apply {
            changePassword(request.password, request.newPassword)
        }
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
        val applicantss = mutableListOf<Applicant>()
        for (i in 1..1000) {
            applicantss.add(
                Applicant(
                    name = "홍길동$i",
                    email = "ss$i@email.com",
                    phoneNumber = "010-0000-0000",
                    gender = listOf(Gender.MALE, Gender.FEMALE).shuffled().take(1)[0],
                    birthday = createLocalDate(2020, 1, 1).minusDays(i.toLong()),
                    password = Password("password")
                )
            )
        }
        applicantss.add(
            Applicant(
                name = "홍길동9999",
                email = "ssasd@email.com",
                phoneNumber = "010-0000-0000",
                gender = listOf(Gender.MALE, Gender.FEMALE).shuffled().take(1)[0],
                birthday = createLocalDate(2021, 1, 1),
                password = Password("password")
            )
        )
        applicantRepository.saveAll(applicants)
        applicantRepository.saveAll(applicantss)
    }
}
