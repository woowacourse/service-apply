package apply.ui.api

import apply.application.EvaluationItemService
import apply.application.EvaluationService
import apply.domain.evaluation.Evaluation
import apply.domain.evaluationItem.EvaluationItem
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evaluations")
class EvaluationRestController(
    private val evaluationService: EvaluationService,
    private val evaluationItemService: EvaluationItemService
) {
    @GetMapping
    fun findAll(): ResponseEntity<List<Evaluation>> {
        return ResponseEntity.ok(evaluationService.findAll())
    }

    @GetMapping("/{id}/items")
    fun findItemsById(@PathVariable("id") evaluationId: Long): ResponseEntity<List<EvaluationItem>> {
        return ResponseEntity.ok(evaluationItemService.findByEvaluationId(evaluationId))
    }
}
