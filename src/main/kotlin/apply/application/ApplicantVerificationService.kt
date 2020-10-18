package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantValidateException
import apply.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ApplicantVerificationService(
    private val applicantRepository: ApplicantRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun generateToken(request: RegisterApplicantRequest): String {
        val applicant = applicantRepository.findByEmail(request.email)
            ?.also { it.verify(request.password, request.information) }
            ?: applicantRepository.save(request.toEntity())

        return jwtTokenProvider.createToken(applicant.email)
    }

    fun generateTokenByLogin(request: VerifyApplicantRequest): String {
        val applicant = applicantRepository.findByEmail(request.email)!!
        if (!applicant.samePassword(request.password)) {
            throw ApplicantValidateException()
        }
        return jwtTokenProvider.createToken(request.email)
    }
}
