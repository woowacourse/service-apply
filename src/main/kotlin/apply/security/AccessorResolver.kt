package apply.security

import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Base64

private const val BASIC: String = "Basic"

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
        val token = extractBasicToken(webRequest)
        val decoded = token.decode(StandardCharsets.UTF_8)
        if (!isAuthenticated(decoded, parameter)) {
            throw LoginFailedException()
        }
    }

    private fun extractBasicToken(request: NativeWebRequest): String {
        val authorization = request.getHeader(AUTHORIZATION) ?: throw LoginFailedException()
        val (tokenType, token) = authorization.split(" ")
        if (tokenType != BASIC) {
            throw LoginFailedException()
        }
        return token
    }

    private fun String.decode(charset: Charset): String = Base64.getDecoder().decode(this).toString(charset)

    private fun isAuthenticated(decoded: String, parameter: MethodParameter): Boolean {
        return decoded.split(":") == parameter.getAuthentication().toList()
    }

    private fun MethodParameter.getAuthentication(): Pair<String?, String?> {
        val value = getParameterAnnotation(Accessor::class.java)?.value
        return value to accessorProperties.keys[value]
    }
}
