package apply.security

import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val BEARER = "Bearer"

@Component
class AccessorResolver(
    private val accessorProperties: AccessorProperties
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(Accessor::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ) {
        val token = extractBearerToken(webRequest)
        if (token != parameter.getKey()) {
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
    private fun MethodParameter.getKey(): String? {
        val value = getParameterAnnotation(Accessor::class.java)?.value
        return accessorProperties.keys[value]
    }
}
