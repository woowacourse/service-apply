package apply.ui.api

import apply.application.EvaluationService
import apply.application.ExcelService
import apply.application.RecruitmentService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ExcelRestController(
    private val excelService: ExcelService,
    private val recruitmentService: RecruitmentService,
    private val evaluationService: EvaluationService,
) {
    @GetMapping("/recruitments/{recruitmentId}/excel")
    fun createApplicantExcel(@PathVariable("recruitmentId") recruitmentId: Long): ResponseEntity<InputStreamResource> {
        val excel = excelService.createApplicantExcel(recruitmentId) // return type: ByteArrayInputStream
        val recruitment = recruitmentService.getById(recruitmentId)
        val headers = HttpHeaders().apply {
            contentDisposition = ContentDisposition.builder("attatchment")
                .filename("${recruitment.title}.xlsx")
                .build()
        }

        return ResponseEntity.ok()
            .headers(headers)
            .body(InputStreamResource((excel)))
    }

    @GetMapping("/evaluations/{evaluationId}/excel")
    fun createTargetExcel(@PathVariable("evaluationId") evaluationId: Long): ResponseEntity<InputStreamResource> {
        val excel = excelService.createTargetExcel(evaluationId)
        val evaluation = evaluationService.findById(evaluationId)
        val headers = HttpHeaders().apply {
            contentDisposition = ContentDisposition.builder("attatchment")
                .filename("${evaluation?.title}.xlsx")
                .build()
        }

        return ResponseEntity.ok()
            .headers(headers)
            .body(InputStreamResource((excel)))
    }
}
