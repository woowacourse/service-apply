package apply.security

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private const val AUTHORIZATION_HEADER = "Authorization"
private const val BEARER = "Bearer"
private const val TOKEN_TYPE_INDEX = 0
private const val TOKEN_INDEX = 1
private const val VALID_CHUNK_SIZE = 2

@Component
class BearerAuthInterceptor(private val jwtTokenProvider: JwtTokenProvider) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as? HandlerMethod ?: return true

        if (handlerMethod.methodParameters.none { it.hasParameterAnnotation(LoginApplicant::class.java) }) {
            return true
        }

        val token = extractBearerToken(request)
        val applicantEmail = jwtTokenProvider.getPayload(token)
        request.setAttribute("loginApplicantEmail", applicantEmail)

        return true
    }

    private fun extractBearerToken(request: HttpServletRequest): String {
        val authorization = request.getHeader(AUTHORIZATION_HEADER)
            ?: throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")

        val splitToken = authorization.split(" ")
        if (splitToken.size != VALID_CHUNK_SIZE || splitToken[TOKEN_TYPE_INDEX] != BEARER) {
            throw IllegalArgumentException("로그인 정보가 정확하지 않습니다")
        }
        return splitToken[TOKEN_INDEX]
    }
}
