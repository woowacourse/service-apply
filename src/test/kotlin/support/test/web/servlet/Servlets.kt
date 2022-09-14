package support.test.web.servlet

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

fun MockMvc.multipart(
    method: HttpMethod,
    urlTemplate: String,
    vararg vars: Any,
    block: MockMultipartHttpServletRequestBuilder.() -> Unit = {}
): ResultActions {
    val builder = MockMvcRequestBuilders.multipart(urlTemplate, *vars)
    builder.with {
        it.method = method.name
        it
    }
    builder.apply(block)
    return perform(builder)
}

fun MockHttpServletRequestDsl.bearer(token: String) {
    header(AUTHORIZATION, bearerToken(token))
}

fun MockMultipartHttpServletRequestBuilder.bearer(token: String) {
    header(AUTHORIZATION, bearerToken(token))
}

private fun bearerToken(token: String): String = "Bearer $token"
