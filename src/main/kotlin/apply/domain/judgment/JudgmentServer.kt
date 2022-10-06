package apply.domain.judgment

import apply.application.JudgmentRequest

interface JudgmentAgency {
    fun requestJudge(request: JudgmentRequest)
}
