package apply.ui.admin.selections

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.AssignmentService
import apply.application.EvaluationSelectData
import apply.application.EvaluationService
import apply.application.EvaluationTargetCsvService
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.ExcelService
import apply.application.JudgmentAllService
import apply.application.JudgmentService
import apply.application.MyMissionService
import apply.application.RecruitmentItemService
import apply.application.RecruitmentService
import apply.domain.applicationform.ApplicationForm
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.addSortableColumn
import support.views.addSortableDateColumn
import support.views.addSortableDateTimeColumn
import support.views.createContrastButtonWithDialog
import support.views.createCsvUpload
import support.views.createNormalButton
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.createSearchBox
import support.views.createSuccessButton
import support.views.downloadFile

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val recruitmentService: RecruitmentService,
    private val recruitmentItemService: RecruitmentItemService,
    private val applicantService: ApplicantService,
    private val evaluationService: EvaluationService,
    private val evaluationTargetService: EvaluationTargetService,
    private val assignmentService: AssignmentService,
    private val judgmentService: JudgmentService,
    private val judgmentAllService: JudgmentAllService,
    private val myMissionService: MyMissionService,
    private val excelService: ExcelService,
    private val evaluationTargetCsvService: EvaluationTargetCsvService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L
    private var evaluations: List<EvaluationSelectData> =
        evaluationService.getAllSelectDataByRecruitmentId(recruitmentId)
    private var tabs: Tabs = Tabs()
    private var selectedTabIndex: Int = 0
    private var evaluationFileButtons: HorizontalLayout = createEvaluationFileButtons()

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        add(createTitle(), createContent())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1(recruitmentService.getById(recruitmentId).title)).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createContent(keyword: String = ""): Component {
        val tabsToGrids: Map<Tab, Component> = mapTabAndGrid(keyword)
        val (tabs, grids) = createTabComponents(tabsToGrids)
        val menu = HorizontalLayout(
            createSearchBox {
                removeAll()
                add(
                    createTitle(),
                    createContent(keyword = it)
                )
                selectedTabIndex = tabs.selectedIndex
            },
            tabs,
            HorizontalLayout(
                createLoadButton(tabs),
                createResultDownloadButton(),
                createJudgeAllButton(tabs)
            )
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
        return VerticalLayout(menu, grids, evaluationFileButtons).apply { setWidthFull() }
    }

    private fun createEvaluationFileButtons(): HorizontalLayout {
        return HorizontalLayout(
            createEvaluationFileDownloadButton(),
            createEvaluationFileUpload()
        ).apply {
            setWidthFull()
            isVisible = false
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun mapTabAndGrid(keyword: String): Map<Tab, Component> {
        val tabsToGrids = LinkedHashMap<Tab, Component>()

        val userResponses = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword)
        tabsToGrids[Tab(TOTAL_APPLIED_USER_LABEL)] = createTotalUsersGrid(userResponses)

        evaluations = evaluationService.getAllSelectDataByRecruitmentId(recruitmentId)
        for (evaluation in evaluations) {
            val responses = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluation.id, keyword)
            tabsToGrids[Tab(evaluation.title)] = createEvaluationTargetsGrid(responses)
        }
        return tabsToGrids
    }

    private fun createTotalUsersGrid(users: List<ApplicantAndFormResponse>): Component {
        return Grid<ApplicantAndFormResponse>(10).apply {
            addSortableColumn("이름", ApplicantAndFormResponse::name)
            addSortableColumn("이메일", ApplicantAndFormResponse::email)
            addSortableColumn("전화번호", ApplicantAndFormResponse::phoneNumber)
            addSortableColumn("성별") { it.gender.label }
            addSortableDateColumn("생년월일", ApplicantAndFormResponse::birthday)
            addSortableDateTimeColumn("지원 일시") { it.applicationForm.submittedDateTime }
            addSortableColumn("부정행위자") { if (it.isCheater) "O" else "X" }
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(users)
        }
    }

    private fun createButtonRenderer(): Renderer<ApplicantAndFormResponse> {
        return ComponentRenderer { user ->
            createPrimarySmallButton("지원서") {
                val dialog = Dialog()
                dialog.add(*createRecruitmentItems(user.applicationForm))
                dialog.width = "800px"
                dialog.height = "90%"
                dialog.open()
            }
        }
    }

    private fun createEvaluationTargetsGrid(evaluationTargets: List<EvaluationTargetResponse>): Component {
        return Grid<EvaluationTargetResponse>(10).apply {
            addSortableColumn("이름", EvaluationTargetResponse::name)
            addSortableColumn("이메일", EvaluationTargetResponse::email)
            addSortableColumn("합계", EvaluationTargetResponse::totalScore)
            addSortableColumn("평가 상태", EvaluationTargetResponse::evaluationStatus)
            addSortableColumn("평가자", EvaluationTargetResponse::administratorId)
            addColumn(createEvaluationButtonRenderer()).apply { isAutoWidth = true }
            setItems(evaluationTargets)
        }
    }

    private fun createEvaluationButtonRenderer(): Renderer<EvaluationTargetResponse> {
        return ComponentRenderer { response ->
            createPrimarySmallButton("평가하기") {
                EvaluationTargetFormDialog(
                    evaluationTargetService,
                    assignmentService,
                    myMissionService,
                    judgmentService,
                    response.id
                ) {
                    selectedTabIndex = tabs.selectedIndex
                    removeAll()
                    add(createTitle(), createContent())
                }
            }
        }
    }

    private fun createTabComponents(tabsToGrids: Map<Tab, Component>): Pair<Tabs, Div> {
        val tabs = Tabs().apply {
            add(*(tabsToGrids.keys).toTypedArray())
            addSelectedChangeListener {
                evaluationFileButtons.isVisible = !isTotalUserTab(it.selectedTab)
                tabsToGrids.forEach { (tab, grid) ->
                    grid.isVisible = (tab == selectedTab)
                }
            }
            setWidthFull()
            tabsToGrids.forEach { (tab, grid) -> grid.isVisible = (tab == selectedTab) }
            selectedIndex = selectedTabIndex
            tabs = this
        }

        val grids = Div(*tabsToGrids.values.toTypedArray()).apply { setWidthFull() }

        return tabs to grids
    }

    private fun createLoadButton(tabs: Tabs): Button {
        return createPrimaryButton("평가 대상자 불러오기") {
            val evaluation = tabs.findEvaluation() ?: return@createPrimaryButton
            evaluationTargetService.load(evaluation.id)
            selectedTabIndex = tabs.selectedIndex
            removeAll()
            add(
                createTitle(),
                createContent()
            )
        }
    }

    private fun createEvaluationFileDownloadButton(): Button {
        return createSuccessButton("평가지 다운로드") {
            val evaluation = evaluations[tabs.selectedIndex - 1]
            downloadFile("${evaluation.title}.csv", evaluationTargetCsvService.createTargetCsv(evaluation.id))
        }
    }

    private fun createJudgeAllButton(tabs: Tabs): Button {
        return createContrastButtonWithDialog("전체 자동 채점하기", "자동 채점을 실행하시겠습니까?") {
            val evaluation = tabs.findEvaluation() ?: return@createContrastButtonWithDialog
            judgmentAllService.judgeAll(evaluation.id)
        }
    }

    private fun Tabs.findEvaluation(): EvaluationSelectData? {
        return evaluations.find { it.title == selectedTab.label }
    }

    private fun createEvaluationFileUpload(): Upload {
        return createCsvUpload("평가지 업로드", MemoryBuffer()) {
            val evaluation = evaluations[tabs.selectedIndex - 1]
            evaluationTargetCsvService.updateTarget(it.inputStream, evaluation.id)
            val alertDialog = Dialog()
            val text = Text("평가지가 업로드가 완료 되었습니다. 페이지가 새로고침 됩니다.")
            val reloadButton = Button("확인") { UI.getCurrent().page.reload() }
            alertDialog.add(VerticalLayout(text, reloadButton).apply { alignItems = FlexComponent.Alignment.CENTER })
            alertDialog.open()
        }
    }

    private fun createResultDownloadButton(): Button {
        return createSuccessButton("평가결과 다운로드") {
            if (isTotalUserTab(tabs.selectedTab)) {
                val excel = excelService.createApplicantExcel(recruitmentId)
                downloadFile("${recruitmentService.getById(recruitmentId).title}.xlsx", excel)
            } else {
                val evaluation = evaluations[tabs.selectedIndex - 1]
                downloadFile("${evaluation.title}.xlsx", excelService.createTargetExcel(evaluation.id))
            }
        }
    }

    private fun createRecruitmentItems(applicationForm: ApplicationForm): Array<Component> {
        val answers = applicationForm.answers.items.associate { it.recruitmentItemId to it.contents }
        val items = recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId)
            .map { createItem(it.title, createAnswer(answers.getOrDefault(it.id, ""))) }
            .toTypedArray()
        return addIfExist(items, applicationForm.referenceUrl)
    }

    private fun addIfExist(items: Array<Component>, referenceUrl: String): Array<Component> {
        return when {
            referenceUrl.isNotEmpty() -> {
                val referenceItem = createItem(
                    "포트폴리오",
                    createNormalButton(referenceUrl) {
                        UI.getCurrent().page.open(referenceUrl)
                    }
                )
                items.plusElement(referenceItem)
            }

            else -> items
        }
    }

    private fun createItem(title: String, component: Component): Component {
        return Div(H4(title), component).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun createAnswer(answer: String): Component {
        return TextArea().apply {
            setWidthFull()
            isReadOnly = true
            value = answer
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun isTotalUserTab(tab: Tab): Boolean {
        return tab.label == TOTAL_APPLIED_USER_LABEL
    }

    companion object {
        private const val TOTAL_APPLIED_USER_LABEL: String = "전체 지원자"
    }
}
