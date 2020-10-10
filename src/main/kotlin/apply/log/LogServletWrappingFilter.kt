package apply.log

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogServletWrappingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val wrappingRequest = ContentCachingRequestWrapper(request)
        val wrappingResponse = ContentCachingResponseWrapper(response)
        filterChain.doFilter(wrappingRequest, wrappingResponse)
        wrappingResponse.copyBodyToResponse()
    }
}
