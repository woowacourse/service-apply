package apply.security

import apply.application.MemberService
import apply.domain.member.Member
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val BEARER = "Bearer"

@Component
class LoginMemberResolver(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberService: MemberService
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Member {
        validateIfAdministrator(parameter)
        val token = extractBearerToken(webRequest)
        if (!jwtTokenProvider.isValidToken(token)) {
            throw LoginFailedException()
        }
        val userEmail = jwtTokenProvider.getSubject(token)
        return memberService.getByEmail(userEmail)
    }

    private fun validateIfAdministrator(parameter: MethodParameter) {
        val annotation = parameter.getParameterAnnotation(LoginMember::class.java)
        if (annotation?.administrator == true) {
            // TODO: 관리자가 HTTP API를 사용할 때 작업
            throw LoginFailedException()
        }
    }

    private fun extractBearerToken(request: NativeWebRequest): String {
        val authorization = request.getHeader(AUTHORIZATION) ?: throw LoginFailedException()
        val (tokenType, token) = splitToTokenFormat(authorization)
        if (tokenType != BEARER) {
            throw LoginFailedException()
        }
        return token
    }

    private fun splitToTokenFormat(authorization: String): Pair<String, String> {
        return try {
            val tokenFormat = authorization.split(" ")
            tokenFormat[0] to tokenFormat[1]
        } catch (e: IndexOutOfBoundsException) {
            throw LoginFailedException()
        }
    }
}
