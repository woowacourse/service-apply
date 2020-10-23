package apply.security

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    val isUserLoggedIn: Boolean
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return authentication != null &&
                authentication !is AnonymousAuthenticationToken &&
                authentication.isAuthenticated
        }
}
