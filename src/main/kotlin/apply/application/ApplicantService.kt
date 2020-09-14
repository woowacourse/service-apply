package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import apply.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import java.time.LocalDate
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicantService(
    private val applicantRepository: ApplicantRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun findAll(): List<Applicant> {
        return applicantRepository.findAll()
    }

    fun findByValue(value: String): List<Applicant> =
        applicantRepository.findByNameContainingOrEmailContaining(value, value)

    fun validateOrCreateApplicantAndGenerateToken(
        name: String,
        email: String,
        phoneNumber: String,
        gender: Gender,
        birthDay: LocalDate,
        password: String
    ): String {
        applicantRepository.findByEmail(email)
            ?.validatePassword(password)
            ?: createApplicant(
                name = name,
                email = email,
                phoneNumber = phoneNumber,
                gender = gender,
                birthDay = birthDay,
                password = password
            )

        return jwtTokenProvider.createToken(email)
    }

    private fun createApplicant(
        name: String,
        email: String,
        phoneNumber: String,
        gender: Gender,
        birthDay: LocalDate,
        password: String
    ) {
        val newApplicant = Applicant(
            name = name,
            email = email,
            phoneNumber = phoneNumber,
            gender = gender,
            birthday = birthDay,
            password = password
        )
        applicantRepository.save(newApplicant)
    }

    @PostConstruct
    private fun populateDummy() {
        if (applicantRepository.count() != 0L) {
            return
        }
        val applicants = listOf(
            Applicant(
                "홍길동1",
                "a@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 4, 17),
                "password"
            ),
            Applicant(
                "홍길동2",
                "b@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5),
                "password"
            ),
            Applicant(
                "홍길동3",
                "c@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1),
                "password"
            )
        )
        applicantRepository.saveAll(applicants)
    }
}
