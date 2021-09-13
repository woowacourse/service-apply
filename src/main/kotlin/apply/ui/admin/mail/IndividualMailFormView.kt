package apply.ui.admin.mail

import apply.application.ApplicantService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.router.Route
import support.views.createSearchBar

@Route(value = "admin/emails/personal", layout = BaseLayout::class)
class IndividualMailFormView(
    private val applicantService: ApplicantService
) : MailFormView("개별 발송") {

    override fun createReceiverFilter() {
        createSearchBar {
            removeAll()
        }
    }
}
