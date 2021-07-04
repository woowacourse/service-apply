package apply.ui.api

import apply.application.EvaluationData
import apply.application.EvaluationResponse
import apply.application.EvaluationSelectData
import apply.application.EvaluationService
import apply.application.RecruitmentSelectData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evaluations")
class EvaluationRestController(
    private val evaluationService: EvaluationService
) {

    @PostMapping
    fun createEvaluation(@RequestBody evaluationData: EvaluationData): ResponseEntity<Unit> {
        evaluationService.save(evaluationData)
        return ResponseEntity.ok().body(Unit)
    }

    @GetMapping("/recruitments")
    fun findAllRecruitmentSelectData(): ResponseEntity<ApiResponse<List<RecruitmentSelectData>>> {
        val recruitmentSelectDatas = evaluationService.findAllRecruitmentSelectData()
        return ResponseEntity.ok(ApiResponse.success(recruitmentSelectDatas))
    }

    @GetMapping("/{evaluationId}")
    fun getDataById(@PathVariable("evaluationId") evaluationId: Long): ResponseEntity<ApiResponse<EvaluationData>> {
        val evaluationData = evaluationService.getDataById(evaluationId)
        return ResponseEntity.ok(ApiResponse.success(evaluationData))
    }

    @GetMapping("/info")
    fun getAllSelectDataByRecruitmentId(@RequestParam("recruitmentId") recruitmentId: Long): ResponseEntity<ApiResponse<List<EvaluationSelectData>>> {
        val evaluationDatas = evaluationService.getAllSelectDataByRecruitmentId(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(evaluationDatas))
    }

    @GetMapping
    fun findAllWithRecruitment(): ResponseEntity<ApiResponse<List<EvaluationResponse>>> {
        val evaluationResponses = evaluationService.findAllWithRecruitment()
        return ResponseEntity.ok(ApiResponse.success(evaluationResponses))
    }

    @DeleteMapping("/{evaluationId}")
    fun deleteById(@PathVariable("evaluationId") evaluationId: Long): ResponseEntity<Unit> {
        evaluationService.deleteById(evaluationId)
        return ResponseEntity.ok().body(Unit)
    }
}
