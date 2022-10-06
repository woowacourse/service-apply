package apply.ui.admin.administrator

import apply.application.CreateAdministratorFormData
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout

class AdministratorForm : BindingIdentityFormLayout<CreateAdministratorFormData>(CreateAdministratorFormData::class) {
    private val name: TextField = TextField("관리자명")
    private val username: TextField = TextField("관리자 사용자명")
    private val password: PasswordField = PasswordField("비밀번호")
    private val confirmPassword: PasswordField = PasswordField("비밀번호 확인")

    init {
        add(name, username, password, confirmPassword)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    override fun bindOrNull(): CreateAdministratorFormData? {
        return bindDefaultOrNull()
    }

    override fun fill(administrator: CreateAdministratorFormData) {
        fillDefault(administrator)
    }
}
