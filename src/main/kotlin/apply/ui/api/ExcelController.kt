package apply.ui.api

import apply.application.EvaluationService
import apply.application.ExcelService
import apply.application.RecruitmentService
import apply.domain.member.Member
import apply.security.LoginMember
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/recruitments/{recruitmentId}")
@RestController
class ExcelController(
    private val excelService: ExcelService,
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService
) {
    @GetMapping("/applicants/excel")
    fun createApplicantExcel(
        @PathVariable recruitmentId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<InputStreamResource> {
        val excel = excelService.createApplicantExcel(recruitmentId)
        val recruitment = recruitmentService.getById(recruitmentId)
        val headers = HttpHeaders().apply {
            contentDisposition = ContentDisposition.builder("attachment")
                .filename("${recruitment.title}.xlsx")
                .build()
        }
        return ResponseEntity.ok()
            .headers(headers)
            .body(InputStreamResource((excel)))
    }

    @GetMapping("/evaluations/{evaluationId}/targets/excel")
    fun createTargetExcel(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<InputStreamResource> {
        val excel = excelService.createTargetExcel(evaluationId)
        val evaluation = evaluationService.getById(evaluationId)
        val headers = HttpHeaders().apply {
            contentDisposition = ContentDisposition.builder("attachment")
                .filename("${evaluation.title}.xlsx")
                .build()
        }
        return ResponseEntity.ok()
            .headers(headers)
            .body(InputStreamResource((excel)))
    }
}
