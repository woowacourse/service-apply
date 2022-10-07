package apply.security

import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

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
        val authorization = webRequest.getHeader(AUTHORIZATION) ?: throw LoginFailedException()
        if (authorization != parameter.getKey()) {
            throw LoginFailedException()
        }
    }

    private fun MethodParameter.getKey(): String? {
        val value = getParameterAnnotation(Accessor::class.java)?.value
        return accessorProperties.keys[value]
    }
}
