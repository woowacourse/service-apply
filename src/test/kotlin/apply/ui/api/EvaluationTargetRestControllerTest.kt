package apply.ui.api

import apply.EVALUATION_TARGET_NOTE
import apply.application.EvaluationItemResponse
import apply.application.EvaluationItemScoreData
import apply.application.EvaluationTargetCsvService
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.GradeEvaluationResponse
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.createEvaluationItem
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.io.path.Path
import kotlin.io.path.inputStream

@WebMvcTest(
    controllers = [EvaluationTargetRestController::class]
)
class EvaluationTargetRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var evaluationTargetService: EvaluationTargetService

    @MockkBean
    private lateinit var mailTargetService: MailTargetService

    @MockkBean
    private lateinit var evaluationTargetCsvService: EvaluationTargetCsvService

    private val recruitmentId = 1L
    private val evaluationId = 1L
    private val targetId = 1L
    private val keyword = "아마찌"
    private val updatedScore = 5
    private val updatedStatus = EvaluationStatus.PASS
    private val updatedNote = "특이 사항(수정)"
    private val answers = listOf(EvaluationItemScoreData(score = updatedScore, id = 3L))
    private val gradeEvaluationRequest = EvaluationTargetData(answers, updatedNote, updatedStatus)

    private val evaluationTargetResponse: EvaluationTargetResponse = EvaluationTargetResponse(
        id = 1L,
        name = "아마찌",
        email = "wlgp2500@gmail.com",
        userId = 1L,
        totalScore = 100,
        evaluationStatus = EvaluationStatus.PASS,
        administratorId = 1L,
        note = EVALUATION_TARGET_NOTE,
        answers = EvaluationAnswers(mutableListOf())
    )

    private val gradeEvaluationResponse: GradeEvaluationResponse = GradeEvaluationResponse(
        title = "평가 항목 제목",
        description = "평가 항목 설명",
        evaluationTarget = EvaluationTargetData(),
        evaluationItems = listOf(EvaluationItemResponse(createEvaluationItem()))
    )

    @Test
    fun `평가 id와 키워드(이름, 이메일)로 평가 대상자를 조회한다`() {
        val responses = listOf(evaluationTargetResponse)
        every { evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId, keyword) } returns responses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets",
            recruitmentId,
            evaluationId
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
            param("keyword", keyword)
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(responses))) }
        }
    }

    @Test
    fun `이전 평가와 평가 대상자 존재 여부를 통해 저장하거나 갱신한다 `() {
        every { evaluationTargetService.load(evaluationId) } just Runs

        mockMvc.put(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/renew",
            recruitmentId,
            evaluationId
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `평가 대상 id로 채점 정보를 불러온다`() {
        every { evaluationTargetService.getGradeEvaluation(targetId) } returns gradeEvaluationResponse

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade",
            recruitmentId,
            evaluationId,
            targetId
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(gradeEvaluationResponse))) }
        }
    }

    @Test
    fun `평가 완료 후 점수를 매긴다`() {
        every { evaluationTargetService.grade(targetId, gradeEvaluationRequest) } just Runs

        mockMvc.patch(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade",
            recruitmentId,
            evaluationId,
            targetId
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(gradeEvaluationRequest)
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }

    @EnumSource(names = ["PASS", "FAIL", "WAITING"])
    @ParameterizedTest
    fun `메일 발송 대상(합격자)들의 이메일 정보를 조회한다`(enumStatus: EvaluationStatus) {
        val responses = listOf(MailTargetResponse("roki@woowacourse.com", "김로키"))
        every { mailTargetService.findMailTargets(evaluationId, enumStatus) } returns responses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/emails?status={status}",
            recruitmentId,
            evaluationId,
            enumStatus.toString()
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
            param("keyword", keyword)
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(responses))) }
        }
    }

    @Test
    fun `평가지를 기준으로 평가대상자들의 상태를 업데이트한다`() {
        val contentStream = Path("src/test/resources/another_evaluation.csv").inputStream()
        val file = MockMultipartFile("evaluation", "evaluation.csv", "text/csv", contentStream)

        every { evaluationTargetCsvService.updateTarget(any(), evaluationId) } just Runs

        mockMvc.multipart(
            HttpMethod.PATCH,
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/grade",
            recruitmentId,
            evaluationId
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
            file("file", file.bytes)
        }.andExpect(status().isOk)
    }

    private fun MockMvc.multipart(
        method: HttpMethod,
        urlTemplate: String,
        vararg vars: Any,
        block: MockMultipartHttpServletRequestBuilder.() -> Unit = {}
    ): ResultActions {
        val builder = multipart(urlTemplate, *vars)
        MockHttpServletRequestBuilder::class.java.getDeclaredField("method").apply {
            isAccessible = true
            set(builder, method.name)
        }
        builder.apply(block)
        return perform(builder)
    }
}
