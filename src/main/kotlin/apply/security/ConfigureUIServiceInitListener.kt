package apply.security

import apply.security.SecurityUtils.isUserLoggedIn
import apply.ui.admin.LoginView
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.UIInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import org.springframework.stereotype.Component

@Component
class ConfigureUIServiceInitListener : VaadinServiceInitListener {
    override fun serviceInit(event: ServiceInitEvent) {
        event.source.addUIInitListener { uiEvent: UIInitEvent ->
            uiEvent.ui.addBeforeEnterListener { event: BeforeEnterEvent -> authenticateNavigation(event) }
        }
    }

    private fun authenticateNavigation(event: BeforeEnterEvent) {
        if (LoginView::class.java != event.navigationTarget && !isUserLoggedIn) {
            event.rerouteTo(LoginView::class.java)
        }
    }
}
