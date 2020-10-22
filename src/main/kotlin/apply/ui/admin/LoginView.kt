package apply.ui.admin

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(value = "admin/login")
@PageTitle("관리자 로그인")
class LoginView : VerticalLayout(), BeforeEnterObserver {
    val login = LoginForm()

    init {
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        login.action = "admin/login"
        add(H1("우아한테크코스"), login)
    }

    override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
        if (beforeEnterEvent.location
            .queryParameters
            .parameters
            .containsKey("error")
        ) {
            login.isError = true
        }
    }
}
