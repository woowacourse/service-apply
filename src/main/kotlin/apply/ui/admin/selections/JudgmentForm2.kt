package apply.ui.admin.selections

import apply.application.JudgmentData
import apply.application.JudgmentService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextArea
import support.views.createContrastButton
import support.views.createNotification

class JudgmentForm2(
    private val judgmentData: JudgmentData,
    private val judgmentService: JudgmentService
) : FormLayout() {
    init {
        add(
            FormLayout(H3("자동 채점"), createJudgmentRequestButton()).apply {
                setResponsiveSteps(ResponsiveStep("0", 6))
            },
            FormLayout(
                IntegerField("통과 개수").apply {
                    value = judgmentData.passCount
                    isReadOnly = true
                },
                IntegerField("총 개수").apply {
                    value = judgmentData.totalCount
                    isReadOnly = true
                }
            ),
            createTextArea()
        )
        setResponsiveSteps(ResponsiveStep("0", 1))
    }

    private fun createJudgmentRequestButton(): Button {
        return createContrastButton("실행") {
            try {
                judgmentService.judgeReal(judgmentData.assignmentId)
                createNotification("자동 채점이 실행되었습니다.")
            } catch (e: Exception) {
                createNotification(e.localizedMessage)
            }
        }
    }

    private fun createTextArea(): Component {
        val result = """
            |status: ${judgmentData.status}
            |commitHash: ${judgmentData.commitHash}
            |startedDateTime: ${judgmentData.startedDateTime}
            |message: ${judgmentData.message}
        """.trimMargin()
        return TextArea("마지막 자동 채점 기록").apply {
            value = result
            minHeight = "135px"
            isReadOnly = true
        }
    }
}
