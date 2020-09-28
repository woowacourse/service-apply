package apply.domain.dummy

import apply.application.DoEvaluationItemResponse
import apply.application.DoEvaluationRequest
import apply.application.DoEvaluationResponse
import apply.domain.evaluationItem.EvaluationItem
import org.springframework.stereotype.Service

@Service
class DummyService {
    fun getEvaluationByTargetId(targetId: Long): DoEvaluationResponse {
        val evaluationItems = listOf(
            EvaluationItem(
                title = "README.md 파일에 기능 목록이 추가되어 있는가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 1L,
                maximumScore = 2,
                position = 0
            ),
            EvaluationItem(
                title = "인덴트가 2 이하인가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 1L,
                maximumScore = 3,
                position = 2
            ),
            EvaluationItem(
                title = "하드코딩을 상수화 했는가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 1L,
                maximumScore = 2,
                position = 1
            )
        )

        return DoEvaluationResponse(
            evaluationItems = evaluationItems.map {
                DoEvaluationItemResponse(
                    title = it.title,
                    description = it.description,
                    maximumScore = it.maximumScore,
                    id = it.id
                )
            },
            note = "평가를 하면서 느껴지는 특이한 점",
            evaluationStatus = EvaluationStatus.WAITING,
            evaluationDescription = "리뷰 절차는 이렇게 됩니다",
            evaluationTitle = "프리코스 대상자 선발"
        )
    }

    fun save(doEvaluationRequest: DoEvaluationRequest) {
    }
}
