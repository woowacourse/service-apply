package apply.security

import apply.security.SecurityUtils.isUserLoggedIn
import apply.ui.admin.LoginView
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import org.springframework.stereotype.Component

@Component
class ConfigureUiServiceInitListener : VaadinServiceInitListener {
    override fun serviceInit(event: ServiceInitEvent) {
        event.source.addUIInitListener { uiEvent ->
            uiEvent.ui.addBeforeEnterListener { event -> authenticateNavigation(event) }
        }
    }

    private fun authenticateNavigation(event: BeforeEnterEvent) {
        if (LoginView::class.java != event.navigationTarget && !isUserLoggedIn) {
            event.rerouteTo(LoginView::class.java)
        }
    }
}
