package apply.ui.admin

import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import support.views.Title

@Route(value = "admin/login")
class LoginView : VerticalLayout(), BeforeEnterObserver {
    private val login = LoginForm()

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        login.action = "admin/login"
        add(Title("우아한테크코스"), login)
    }

    override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
        if (beforeEnterEvent.location.queryParameters.parameters.containsKey("error")) {
            login.isError = true
        }
    }
}
