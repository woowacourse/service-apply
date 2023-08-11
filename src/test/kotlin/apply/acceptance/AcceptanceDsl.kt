package apply.acceptance

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDateTime

private val now: LocalDateTime = LocalDateTime.now()

data class Recruitment(
    var title: String = "웹 백엔드 4기",
    var term: Term? = null,
    var termId: Long = 0L,
    var startDateTime: LocalDateTime = now.minusYears(1),
    var endDateTime: LocalDateTime = now.plusYears(1),
    var recruitable: Boolean = false,
    var hidden: Boolean = true,
    var recruitmentItems: List<RecruitmentItem> = emptyList(),
    var id: Long = 0L
)

data class RecruitmentItem(
    var title: String = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?",
    var position: Int = 1,
    var maximumLength: Int = 1000,
    var description: String = "현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요.",
    var id: Long = 0L
)

class RecruitmentItems {
    val items: MutableList<RecruitmentItem> = mutableListOf()

    fun recruitmentItem(block: RecruitmentItem.() -> Unit = {}) {
        items.add(RecruitmentItem().apply(block))
    }
}

fun recruitmentItems(block: RecruitmentItems.() -> Unit): List<RecruitmentItem> {
    return RecruitmentItems().apply(block).items
}

fun WebTestClient.recruitment(block: Recruitment.() -> Unit = {}): Recruitment {
    val recruitment = Recruitment().apply(block)
    if (recruitment.term == null) {
        recruitment.term = Term(id = recruitment.termId)
    }
    post().uri("/api/recruitments")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(recruitment)
        .exchange()
        .expectStatus().isCreated
        .expectHeader().value(HttpHeaders.LOCATION) { recruitment.id = it.extractId() }
    return recruitment
}

data class Term(
    var name: String = "4기",
    var id: Long = 0L
)

fun WebTestClient.term(block: Term.() -> Unit = {}): Term {
    val term = Term().apply(block)
    post().uri("/api/terms")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(term)
        .exchange()
        .expectStatus().isCreated
        .expectHeader().value(HttpHeaders.LOCATION) { term.id = it.extractId() }
    return term
}

private fun String.extractId(): Long = substringAfterLast("/").toLong()
