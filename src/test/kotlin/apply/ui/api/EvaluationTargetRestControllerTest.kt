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
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import support.test.web.servlet.bearer
import support.test.web.servlet.multipart
import kotlin.io.path.Path
import kotlin.io.path.inputStream

@WebMvcTest(EvaluationTargetRestController::class)
class EvaluationTargetRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var evaluationTargetService: EvaluationTargetService

    @MockkBean
    private lateinit var mailTargetService: MailTargetService

    @MockkBean
    private lateinit var evaluationTargetCsvService: EvaluationTargetCsvService

    @Test
    fun `평가 id와 키워드(이름, 이메일)로 평가 대상자를 조회한다`() {
        val responses = listOf(
            EvaluationTargetResponse(
                id = 1L,
                name = "아마찌",
                email = "wlgp2500@gmail.com",
                memberId = 1L,
                totalScore = 100,
                evaluationStatus = PASS,
                administratorId = 1L,
                note = EVALUATION_TARGET_NOTE,
                answers = EvaluationAnswers(mutableListOf())
            )
        )
        every { evaluationTargetService.findAllByEvaluationIdAndKeyword(any(), any()) } returns responses

        mockMvc.get("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets", 1L, 1L) {
            bearer("valid_token")
            param("keyword", "아마찌")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }

    @Test
    fun `이전 평가와 평가 대상자 존재 여부를 통해 저장하거나 갱신한다 `() {
        every { evaluationTargetService.load(any()) } just Runs

        mockMvc.put("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/renew", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `평가 대상 id로 채점 정보를 불러온다`() {
        val response = GradeEvaluationResponse(
            title = "평가 항목 제목",
            description = "평가 항목 설명",
            evaluationTarget = EvaluationTargetData(),
            evaluationItems = listOf(EvaluationItemResponse(createEvaluationItem()))
        )
        every { evaluationTargetService.getGradeEvaluation(any()) } returns response

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade", 1L, 1L, 1L
        ) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("patch/recruitments/evaluations/targets/grade"))
        }
    }

    @Test
    fun `평가 완료 후 점수를 매긴다`() {
        every { evaluationTargetService.grade(any(), any()) } just Runs

        mockMvc.patch(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade", 1L, 1L, 1L
        ) {
            jsonContent(EvaluationTargetData(listOf(EvaluationItemScoreData(score = 5, id = 3L)), "특이 사항(수정)", PASS))
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
        }
    }

    @EnumSource(names = ["PASS", "FAIL", "WAITING"])
    @ParameterizedTest
    fun `메일 발송 대상(합격자)들의 이메일 정보를 조회한다`(enumStatus: EvaluationStatus) {
        val responses = listOf(MailTargetResponse("roki@woowacourse.com", "김로키", 1L))
        every { mailTargetService.findMailTargets(any(), any()) } returns responses

        mockMvc.get("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/emails", 1L, 1L) {
            bearer("valid_token")
            param("status", enumStatus.name)
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }

    @Test
    fun `평가지를 기준으로 평가대상자들의 상태를 업데이트한다`() {
        val contentStream = Path("src/test/resources/another_evaluation.csv").inputStream()
        val file = MockMultipartFile("evaluation", "evaluation.csv", "text/csv", contentStream)

        every { evaluationTargetCsvService.updateTarget(any(), any()) } just Runs

        mockMvc.multipart(
            HttpMethod.PATCH,
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/grade",
            1L,
            1L
        ) {
            bearer("valid_token")
            file("file", file.bytes)
        }.andExpect(status().isOk)
    }
}
