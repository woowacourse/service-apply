package apply.acceptance

import apply.config.Database
import apply.createUser
import apply.security.LoginUserResolver
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotContainDuplicates
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import support.createLocalDateTime
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@SpykBean(LoginUserResolver::class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AcceptanceDslTest(
    private val client: WebTestClient,
    private val database: Database,
    private val loginUserResolver: LoginUserResolver
) : StringSpec({
    every { loginUserResolver.resolveArgument(any(), any(), any(), any()) } returns createUser()

    "기수 생성 예시" {
        val term1 = with(client) {
            term()
        }
        val term2 = with(client) {
            term {
                name = "5기"
            }
        }
        term1.id shouldNotBe term2.id
        term2.name shouldBe "5기"
    }

    "모집 생성 예시" {
        val recruitment1 = with(client) {
            recruitment()
        }
        val recruitment2 = with(client) {
            recruitment {
                termId = 0L
            }
        }
        val recruitment3 = with(client) {
            recruitment {
                title = "웹 백엔드 5기"
                term = term {
                    name = "5기"
                }
                startDateTime = createLocalDateTime(2022, 10, 17)
                endDateTime = createLocalDateTime(2022, 10, 24)
                recruitmentItems = recruitmentItems {
                    recruitmentItem {
                        title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
                        position = 1
                    }
                    recruitmentItem {
                        title = "프로그래머가 되려는 이유는 무엇인가요?"
                        position = 2
                    }
                }
            }
        }
        listOf(recruitment1.id, recruitment2.id, recruitment3.id).shouldNotContainDuplicates()
    }

    afterTest {
        database.clear(database.retrieveTables())
    }
})

private val now: LocalDateTime = now()

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
        .contentType(APPLICATION_JSON)
        .bodyValue(recruitment)
        .exchange()
        .expectStatus().isCreated
        .expectHeader().value(LOCATION) { recruitment.id = it.extractId() }
    return recruitment
}

data class Term(
    var name: String = "4기",
    var id: Long = 0L
)

fun WebTestClient.term(block: Term.() -> Unit = {}): Term {
    val term = Term().apply(block)
    post().uri("/api/terms")
        .contentType(APPLICATION_JSON)
        .bodyValue(term)
        .exchange()
        .expectStatus().isCreated
        .expectHeader().value(LOCATION) { term.id = it.extractId() }
    return term
}

private fun String.extractId(): Long = substringAfterLast("/").toLong()
