package apply.security

import com.vaadin.flow.server.ServletHelper
import com.vaadin.flow.shared.ApplicationConstants
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.stream.Stream
import javax.servlet.http.HttpServletRequest

object SecurityUtils {
    fun isFrameworkInternalRequest(request: HttpServletRequest): Boolean {
        val parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER)
        return (
            parameterValue != null && Stream.of(*ServletHelper.RequestType.values())
                .anyMatch { r: ServletHelper.RequestType -> r.identifier == parameterValue }
            )
    }

    val isUserLoggedIn: Boolean
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return (
                authentication != null && authentication !is AnonymousAuthenticationToken && authentication.isAuthenticated
                )
        }
}
