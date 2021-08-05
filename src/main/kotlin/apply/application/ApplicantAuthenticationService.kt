package apply.application

import apply.domain.applicant.ApplicantAuthenticationException
import apply.domain.applicant.ApplicantRepository
import apply.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicantAuthenticationService(
    private val applicantRepository: ApplicantRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun generateToken(request: RegisterApplicantRequest): String {
        val applicant = applicantRepository.findByEmail(request.email)
            ?.also { it.authenticate(request.toEntity()) }
            ?: applicantRepository.save(request.toEntity())
        return jwtTokenProvider.createToken(applicant.email)
    }

    fun generateTokenByLogin(request: AuthenticateApplicantRequest): String {
        val applicant = applicantRepository.findByEmail(request.email)
            ?.also { it.authenticate(request.password) }
            ?: throw ApplicantAuthenticationException()
        return jwtTokenProvider.createToken(applicant.email)
    }

    fun authenticateEmail(email: String, authenticateCode: String) {
        val applicant = applicantRepository.findByEmail(email) ?: throw IllegalArgumentException("지원자가 존재하지 않습니다. email: $email")
        applicant.authenticateEmail(authenticateCode)
    }
}
