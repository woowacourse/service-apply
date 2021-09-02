package apply.ui.admin.selections

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.EvaluationService
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.ExcelService
import apply.application.RecruitmentItemService
import apply.application.RecruitmentService
import apply.domain.applicationform.ApplicationForm
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
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
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import support.views.addSortableColumn
import support.views.addSortableDateColumn
import support.views.addSortableDateTimeColumn
import support.views.createCsvUploadButton
import support.views.createNormalButton
import support.views.createPrimaryButton
import support.views.createPrimarySmallButton
import support.views.createSearchBar
import support.views.createSuccessButton
import support.views.downloadFile
import java.io.BufferedReader

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val applicantService: ApplicantService,
    private val recruitmentService: RecruitmentService,
    private val recruitmentItemService: RecruitmentItemService,
    private val evaluationService: EvaluationService,
    private val evaluationTargetService: EvaluationTargetService,
    private val excelService: ExcelService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId = 0L
    private var evaluations = evaluationService.findAllByRecruitmentId(recruitmentId)
    private var tabs = Tabs()
    private var selectedTabIndex = 0

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
            createSearchBar {
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
                createResultDownloadButton()
            )
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }

        val csvButtons = HorizontalLayout(
            createCsvDownloadButton(),
            createCsvUploadButton()
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        }

        return VerticalLayout(menu, grids, csvButtons).apply { setWidthFull() }
    }

    private fun mapTabAndGrid(keyword: String): Map<Tab, Component> {
        val tabsToGrids = LinkedHashMap<Tab, Component>()

        val applicantResponses = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword)
        tabsToGrids[Tab("전체 지원자")] = createTotalApplicantsGrid(applicantResponses)

        evaluations = evaluationService.findAllByRecruitmentId(recruitmentId)
        for (evaluation in evaluations) {
            val evaluationTargetResponses =
                evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluation.id, keyword)
            tabsToGrids[Tab(evaluation.title)] = createEvaluationTargetsGrid(evaluationTargetResponses)
        }
        return tabsToGrids
    }

    private fun createTotalApplicantsGrid(applicants: List<ApplicantAndFormResponse>): Component {
        return Grid<ApplicantAndFormResponse>(10).apply {
            addSortableColumn("이름", ApplicantAndFormResponse::name)
            addSortableColumn("이메일", ApplicantAndFormResponse::email)
            addSortableColumn("전화번호", ApplicantAndFormResponse::phoneNumber)
            addSortableColumn("성별") { it.gender.title }
            addSortableDateColumn("생년월일", ApplicantAndFormResponse::birthday)
            addSortableDateTimeColumn("지원 일시") { it.applicationForm.submittedDateTime }
            addSortableColumn("부정 행위자") { if (it.isCheater) "O" else "X" }
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(applicants)
        }
    }

    private fun createButtonRenderer(): Renderer<ApplicantAndFormResponse> {
        return ComponentRenderer<Component, ApplicantAndFormResponse> { applicant ->
            createPrimarySmallButton("지원서") {
                val dialog = Dialog()
                dialog.add(*createRecruitmentItems(applicant.applicationForm))
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
        return ComponentRenderer<Component, EvaluationTargetResponse> { response ->
            createPrimarySmallButton("평가하기") {
                EvaluationTargetFormDialog(evaluationTargetService, response.id) {
                    selectedTabIndex = tabs.selectedIndex
                    removeAll()
                    add(
                        createTitle(),
                        createContent()
                    )
                }
            }
        }
    }

    private fun createTabComponents(tabsToGrids: Map<Tab, Component>): Pair<Tabs, Div> {
        val tabs = Tabs().apply {
            add(*(tabsToGrids.keys).toTypedArray())
            addSelectedChangeListener {
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
        return createPrimaryButton("평가자 불러오기") {
            val evaluation = evaluations.first { it.title == tabs.selectedTab.label }
            evaluationTargetService.load(evaluation.id)
            selectedTabIndex = tabs.selectedIndex
            removeAll()
            add(
                createTitle(),
                createContent()
            )
        }
    }

    private fun createCsvDownloadButton(): Button {
        return createSuccessButton("평가지 다운로드") {
            if (tabs.selectedIndex != 0) {
                val evaluation = evaluations[tabs.selectedIndex - 1]
                // TODO: 평가지 양식에 맞게 만들어진 CSV로 변경
                val excel = excelService.createTargetExcel(evaluation.id)
                downloadFile("${evaluation.title}.csv", excel)
            }
        }
    }

    private fun createCsvUploadButton(): Upload {
        return createCsvUploadButton("평가지 업로드", MemoryBuffer()) {
            if (tabs.selectedIndex != 0) {
                // TODO: 평가지 양식에 맞는 CSV 읽는 서비스 메서드로 분리 및 연결
                val reader = BufferedReader(it.inputStream.reader())
                val csvParser = CSVParser(reader, CSVFormat.DEFAULT)
                for (csvRecord in csvParser) {
                    csvRecord[0]
                }
            }
        }
    }

    private fun createResultDownloadButton(): Button {
        return createSuccessButton("평가결과 다운로드") {
            if (tabs.selectedIndex == 0) {
                val excel = excelService.createApplicantExcel(recruitmentId)
                downloadFile("${recruitmentService.getById(recruitmentId).title}.xlsx", excel)
            } else {
                val evaluation = evaluations[tabs.selectedIndex - 1]
                val excel = excelService.createTargetExcel(evaluation.id)
                downloadFile("${evaluation.title}.xlsx", excel)
            }
        }
    }

    private fun createRecruitmentItems(applicationForm: ApplicationForm): Array<Component> {
        val answers = applicationForm.answers
            .items
            .map { it.recruitmentItemId to it.contents }
            .toMap()
        val items = recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId)
            .map {
                createItem(it.title, createAnswer(answers.getOrDefault(it.id, "")))
            }.toTypedArray()
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

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        add(
            createTitle(),
            createContent()
        )
    }
}
