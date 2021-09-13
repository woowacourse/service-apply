package apply.ui.admin.mail

import apply.application.EvaluationService
import apply.application.MailTargetService
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.router.Route

@Route(value = "admin/emails/group", layout = BaseLayout::class)
class GroupMailFormView(
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
    private val mailTargetService: MailTargetService
) : MailFormView("그룹 발송") {

    override fun createReceiverFilter() {
        TODO("Not yet implemented")
    }
}
