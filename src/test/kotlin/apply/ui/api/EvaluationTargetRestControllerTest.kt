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
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.io.File
import java.io.FileInputStream

@WebMvcTest(
    controllers = [EvaluationTargetRestController::class]
)
internal class EvaluationTargetRestControllerTest : RestControllerTest() {
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
        every { evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId, keyword) }.answers {
            listOf(evaluationTargetResponse)
        }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets",
                recruitmentId,
                evaluationId,
            ).param("keyword", keyword)
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(listOf(evaluationTargetResponse))
                    )
                )
            )
            .andDo(
                document(
                    "evaluation-target-findAllByEvaluationIdAndKeyword",
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID")
                    ),
                    requestParameters(
                        parameterWithName("keyword").description("키워드(이름, 이메일)")
                    ),
                    responseFields(
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("body.[]").description("평가 대상자 목록")
                    ).andWithPrefix("body.[].", EVALUATION_TARGET_FIELD_DESCRIPTORS)
                )
            )
    }

    @Test
    fun `이전 평가와 평가 대상자 존재 여부를 통해 저장하거나 갱신한다 `() {
        every { evaluationTargetService.load(evaluationId) } just Runs

        mockMvc.perform(
            RestDocumentationRequestBuilders.put(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/renew",
                recruitmentId, evaluationId,
            )
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "evaluation-target-load",
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID")
                    )
                )
            )
    }

    @Test
    fun `평가 대상 id로 채점 정보를 불러온다`() {
        every { evaluationTargetService.getGradeEvaluation(targetId) }.answers { gradeEvaluationResponse }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade",
                recruitmentId,
                evaluationId,
                targetId
            )
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(gradeEvaluationResponse)
                    )
                )
            )
            .andDo(
                document(
                    "evaluation-target-getGradeEvaluation",
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID"),
                        parameterWithName("targetId").description("평가 대상자 ID")
                    ),
                    responseFields(
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("body.title").type(JsonFieldType.STRING).description("평가 항목 제목"),
                        fieldWithPath("body.description").type(JsonFieldType.STRING).description("평가 항목 설명"),
                        fieldWithPath("body.evaluationTarget").type(JsonFieldType.OBJECT).description("평가 대상자 정보"),
                        fieldWithPath("body.evaluationTarget.evaluationItemScores").type(JsonFieldType.ARRAY)
                            .description("평가 항목 점수 목록"),
                        fieldWithPath("body.evaluationTarget.note").type(JsonFieldType.STRING).description("평가 특이 사항"),
                        fieldWithPath("body.evaluationTarget.evaluationStatus").type(JsonFieldType.STRING)
                            .description("평가 상태"),
                        fieldWithPath("body.evaluationItems").type(JsonFieldType.ARRAY).description("평가 항목 목록"),
                        fieldWithPath("body.evaluationItems.[].title").type(JsonFieldType.STRING)
                            .description("평가 항목 제목"),
                        fieldWithPath("body.evaluationItems.[].description").type(JsonFieldType.STRING)
                            .description("평가 항목 설명"),
                        fieldWithPath("body.evaluationItems.[].maximumScore").type(JsonFieldType.NUMBER)
                            .description("평가 항목 최대 점수"),
                        fieldWithPath("body.evaluationItems.[].position").type(JsonFieldType.NUMBER)
                            .description("평가 항목 POSITION"),
                        fieldWithPath("body.evaluationItems.[].evaluationId").type(JsonFieldType.NUMBER)
                            .description("평가 ID"),
                        fieldWithPath("body.evaluationItems.[].id").type(JsonFieldType.NUMBER).description("평가 항목 ID")
                    )
                )
            )
    }

    @Test
    fun `평가 완료 후 점수를 매긴다`() {
        every { evaluationTargetService.grade(targetId, gradeEvaluationRequest) } just Runs

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade",
                recruitmentId,
                evaluationId,
                targetId
            ).header("Content-Type", "application/json")
                .content(
                    objectMapper.writeValueAsString(
                        gradeEvaluationRequest
                    )
                )
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "evaluationtarget-grade",
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID"),
                        parameterWithName("targetId").description("평가 대상자 ID")
                    ),
                    requestFields(
                        fieldWithPath("evaluationItemScores").type(JsonFieldType.ARRAY)
                            .description("평가 항목 점수 목록"),
                        fieldWithPath("evaluationItemScores.[].score").type(JsonFieldType.NUMBER)
                            .description("평가 항목 점수"),
                        fieldWithPath("evaluationItemScores.[].id").type(JsonFieldType.NUMBER)
                            .description("평가 항목 점수 ID"),
                        fieldWithPath("note").type(JsonFieldType.STRING)
                            .description("평가 특이 사항"),
                        fieldWithPath("evaluationStatus").type(JsonFieldType.STRING)
                            .description("평가 상태")
                    )
                )
            )
    }

    @EnumSource(names = ["PASS", "FAIL", "WAITING"])
    @ParameterizedTest
    fun `메일 발송 대상(합격자)들의 이메일 정보를 조회한다`(enumStatus: EvaluationStatus?) {
        every {
            mailTargetService.findMailTargets(
                evaluationId,
                enumStatus
            )
        } returns listOf(MailTargetResponse("roki@woowacourse.com", "김로키"))

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/emails?status={status}",
                recruitmentId,
                evaluationId,
                enumStatus.toString()
            ).header("Content-Type", "application/json")
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "evaluationtarget-email-sending-target-emails",
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID")
                    ),
                    requestParameters(
                        parameterWithName("status").description("조회할 평가 상태")
                    ),
                    responseFields(
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("body.[].name").type(JsonFieldType.STRING).description("대상 이름"),
                        fieldWithPath("body.[].email").type(JsonFieldType.STRING).description("대상 E-MAIL")
                    )
                )
            )
    }

    @Test
    fun `평가지를 기준으로 평가대상자들의 상태를 업데이트한다`() {
        val pathname = javaClass.classLoader.getResource("another_evaluation.csv")!!.file
        val inputStream = FileInputStream(File(pathname))
        val file = MockMultipartFile("evaluation", "evaluation.csv", "text/csv", inputStream)

        every { evaluationTargetCsvService.updateTarget(any(), evaluationId) } just Runs

        val request = RestDocumentationRequestBuilders.fileUpload(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/grade",
            recruitmentId,
            evaluationId
        ).file("file", file.bytes)
            .with { request ->
                request.method = "PATCH"
                request
            }

        mockMvc.perform(request)
            .andExpect(status().isOk)
            .andDo(
                document(
                    "evaluationtarget-csv-grade",
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID"),
                    ),
                    requestParts(
                        partWithName("file").description("업데이트 기준이 될 평가지")
                    ),
                )
            )
    }

    companion object {
        val EVALUATION_TARGET_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("userId").type(JsonFieldType.NUMBER).description("지원자 ID"),
            fieldWithPath("totalScore").type(JsonFieldType.NUMBER).description("점수"),
            fieldWithPath("evaluationStatus").type(JsonFieldType.STRING).description("평가 상태"),
            fieldWithPath("administratorId").type(JsonFieldType.NUMBER).description("평가자 ID"),
            fieldWithPath("note").type(JsonFieldType.STRING).description("평가 특이 사항"),
            fieldWithPath("answers").type(JsonFieldType.ARRAY).description("평가 대답")
        )
    }
}
