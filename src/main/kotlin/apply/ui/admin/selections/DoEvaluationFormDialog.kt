package apply.ui.admin.selections

import apply.domain.dummy.DummyService
import apply.application.DoEvaluationRequest
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import support.views.BindingFormLayout
import support.views.createContrastButton
import support.views.createPrimaryButton

class DoEvaluationFormDialog(
    private val dummyService: DummyService,
    targetId: Long
) : VerticalLayout() {
    private val title: H2 = H2().apply { alignItems = FlexComponent.Alignment.CENTER }
    private val description: TextArea = TextArea().apply {
        setWidthFull()
        isReadOnly = true
    }
    private val doEvaluationForm: BindingFormLayout<DoEvaluationRequest>
    private val dialog: Dialog

    init {
        // TODO: 평가 정보 가져오기
        val evaluateResponse = dummyService.getEvaluationByTargetId(targetId)
        doEvaluationForm = DoEvaluationForm(evaluateResponse)
        title.text = evaluateResponse.evaluationTitle
        description.value = evaluateResponse.evaluationDescription
        dialog = createDialog()
    }

    private fun createDialog(): Dialog {
        return Dialog().apply {
            add(createHeader(), doEvaluationForm, createButtons())
            width = "800px"
            height = "90%"
            open()
        }
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title, description).apply {
            alignItems = FlexComponent.Alignment.CENTER
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createAddButton(), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAddButton(): Button {
        return createPrimaryButton("생성") {
            doEvaluationForm.bindOrNull()?.let {
                // TODO: 저장 요청
                dummyService.save(it)
                dialog.close()
            }
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            dialog.close()
        }
    }
}
