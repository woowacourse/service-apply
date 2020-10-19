package apply.ui.admin

import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route(value = "login")
class LoginView : VerticalLayout() {
    init {
        add(LoginForm())
    }
}
