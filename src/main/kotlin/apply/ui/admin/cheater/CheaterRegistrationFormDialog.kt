package apply.ui.admin.cheater

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.CheaterData
import apply.application.CheaterService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import support.views.createContrastButton
import support.views.createPrimaryButton
import support.views.createSearchBar

class CheaterRegistrationFormDialog(
    private val applicantService: ApplicantService,
    private val cheaterService: CheaterService,
    reloadComponents: () -> Unit
) : Dialog() {
    private val title: H2 = H2("부정 행위자 등록")
    private val description: TextArea = TextArea("등록 사유").apply {
        placeholder = "사유를 입력하세요"
        setWidthFull()
    }
    private var cheater: ApplicantResponse? = null

    init {
        add(createHeader(), createRegisterForm(), createButtons(reloadComponents))
        width = "800px"
        height = "60%"
        open()
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(title).apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
            element.style.set("margin-bottom", "50px")
        }
    }

    private fun createRegisterForm(): VerticalLayout {
        return VerticalLayout(
            createSearchBar(), description
        ).apply {
            element.style.set("margin-bottom", "30px")
        }
    }

    private fun createSearchBar(): Component {
        val container = HorizontalLayout()
        return HorizontalLayout(
            createSearchBar("지원자 검색") {
                container.removeAll()
                val founds = applicantService.findAllByKeyword(it)
                if (founds.isNotEmpty()) {
                    val select = createSelectApplicant(founds)
                    container.add(select)
                }
            },
            container
        ).apply {
            defaultVerticalComponentAlignment = FlexComponent.Alignment.END
        }
    }

    private fun createSelectApplicant(applicants: List<ApplicantResponse>): Select<ApplicantResponse> {
        return Select<ApplicantResponse>().apply {
            setTextRenderer { "${it.name}/${it.email}" }
            setItems(applicants)
        }.apply {
            addValueChangeListener {
                cheater = it.value
            }
        }
    }

    private fun createButtons(reloadComponent: () -> Unit): Component {
        return HorizontalLayout(getCreateAddButton(reloadComponent), createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "20px")
        }
    }

    private fun getCreateAddButton(reloadComponent: () -> Unit): Button {
        return createPrimaryButton("저장") {
            try {
                cheaterService.save(CheaterData(cheater!!.email, description.value))
                reloadComponent()
            } catch (e: IllegalArgumentException) {
                createNotification("이미 등록된 부정 행위자입니다.").open()
            } catch (e: NullPointerException) {
                createNotification("대상을 선택해야 합니다.").open()
            }
            close()
        }
    }

    private fun createNotification(content: String, durationValue: Int = 1000): Notification {
        return Notification(content).apply {
            duration = durationValue
            position = Notification.Position.MIDDLE
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
