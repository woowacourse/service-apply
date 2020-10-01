package apply.security

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KClass

private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER = "Bearer"

const val APPLICANT_EMAIL_ATTRIBUTE_NAME = "loginApplicantEmail"

private val SUPPORTED_PARAMETER_ANNOTATIONS: List<KClass<out Annotation>> = listOf(LoginApplicant::class)

@Component
class BearerAuthInterceptor(private val jwtTokenProvider: JwtTokenProvider) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as? HandlerMethod ?: return true

        if (!hasSupportedParameterAnnotations(handlerMethod.methodParameters)) {
            return true
        }

        val token = extractBearerToken(request)
        val applicantEmail = jwtTokenProvider.getSubject(token)
        request.setAttribute(APPLICANT_EMAIL_ATTRIBUTE_NAME, applicantEmail)

        return true
    }

    private fun hasSupportedParameterAnnotations(methodParameters: Array<MethodParameter>): Boolean {
        return methodParameters.any { methodParameter ->
            methodParameter.parameterAnnotations.any {
                SUPPORTED_PARAMETER_ANNOTATIONS.contains(it.annotationClass)
            }
        }
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
