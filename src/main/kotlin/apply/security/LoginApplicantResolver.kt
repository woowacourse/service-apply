package apply.security

import apply.application.ApplicantService
import apply.domain.applicant.Applicant
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER = "Bearer"

@Component
class LoginApplicantResolver(
    private val jwtTokenProvider: JwtTokenProvider,
    private val applicantService: ApplicantService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.hasParameterAnnotation(LoginApplicant::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Applicant {
        val token = extractBearerToken(webRequest)
        if (!jwtTokenProvider.isValidToken(token)) {
            throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")
        }
        val applicantEmail = jwtTokenProvider.getSubject(token)

        return applicantService.getByEmail(applicantEmail)
    }

    private fun extractBearerToken(request: NativeWebRequest): String {
        val authorization = request.getHeader(AUTHORIZATION_HEADER)
            ?: throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")

        val (tokenType, token) = splitToTokenFormat(authorization)
        if (tokenType != BEARER) {
            throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")
        }
        return token
    }

    private fun splitToTokenFormat(authorization: String): Pair<String, String> {
        return try {
            val tokenFormat = authorization.split(" ")
            Pair(tokenFormat[0], tokenFormat[1])
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")
        }
    }
}
