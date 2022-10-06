package apply.ui.admin.administrator

import apply.application.UpdateAdministratorFormData
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout

class AdministratorUpdateForm :
    BindingIdentityFormLayout<UpdateAdministratorFormData>(UpdateAdministratorFormData::class) {
    private val name: TextField = TextField("새 관리자명")
    private val password: PasswordField = PasswordField("새 비밀번호")
    private val confirmPassword: PasswordField = PasswordField("새 비밀번호 확인")

    init {
        add(name, password, confirmPassword)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    override fun bindOrNull(): UpdateAdministratorFormData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: UpdateAdministratorFormData) {
        fillDefault(data)
    }
}
