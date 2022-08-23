package apply.ui.admin.administrators

import apply.application.AdministratorData
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingIdentityFormLayout

class AdministratorForm : BindingIdentityFormLayout<AdministratorData>(AdministratorData::class) {
    private val name: TextField = TextField("관리자명")
    private val username: TextField = TextField("관리자 ID")
    private val password: TextField = TextField("비밀번호")
    private val passwordConfirmation: TextField = TextField("비밀번호 확인")

    init {
        add(name, username, password, passwordConfirmation)
        setResponsiveSteps(ResponsiveStep("0", 1))
        drawRequired()
    }

    override fun bindOrNull(): AdministratorData? {
        return bindDefaultOrNull()
    }

    override fun fill(administrator: AdministratorData) {
        fillDefault(administrator)
    }
}
