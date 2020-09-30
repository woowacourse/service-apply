package apply.security

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER = "Bearer"

const val APPLICANT_EMAIL_ATTRIBUTE_NAME = "loginApplicantEmail"

@Component
class BearerAuthInterceptor(private val jwtTokenProvider: JwtTokenProvider) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as? HandlerMethod ?: return true

        if (handlerMethod.methodParameters.none { it.hasParameterAnnotation(LoginApplicant::class.java) }) {
            return true
        }

        val token = extractBearerToken(request)
        val applicantEmail = jwtTokenProvider.getSubject(token)
        request.setAttribute(APPLICANT_EMAIL_ATTRIBUTE_NAME, applicantEmail)

        return true
    }

    private fun extractBearerToken(request: HttpServletRequest): String {
        val authorization = request.getHeader(AUTHORIZATION_HEADER)
            ?: throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")

        val tokenType = authorization.substringBefore(" ")
        val token = authorization.substringAfter(" ")
        if (tokenType != BEARER) {
            throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")
        }
        return token
    }
}
