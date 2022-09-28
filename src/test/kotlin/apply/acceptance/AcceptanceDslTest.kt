package apply.acceptance

import apply.config.Database
import apply.createUser
import apply.security.LoginUserResolver
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldNotBeZero
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
                title = "웹 백엔드 5기"
                term = term {
                    name = "5기"
                }
                startDateTime = createLocalDateTime(2022, 10, 17)
                endDateTime = createLocalDateTime(2022, 10, 24)
            }
        }
        val term = with(client) {
            term {
                name = "6기"
            }
        }
        val recruitment3 = with(client) {
            recruitment {
                title = "웹 백엔드 6기"
                termId = term.id
            }
        }
        recruitment1.id.shouldNotBeZero()
        recruitment2.title shouldBe "웹 백엔드 5기"
        recruitment3.title shouldBe "웹 백엔드 6기"
    }

    afterTest {
        database.clear(database.retrieveTables())
    }
})

private val now: LocalDateTime = now()

data class Recruitment(
    var title: String = "웹 백엔드 4기",
    var term: Term? = null,
    var termId: Long? = null,
    var startDateTime: LocalDateTime = now.minusYears(1),
    var endDateTime: LocalDateTime = now.plusYears(1),
    var recruitable: Boolean = false,
    var hidden: Boolean = true,
    var id: Long = 0L
)

fun WebTestClient.recruitment(block: Recruitment.() -> Unit = {}): Recruitment {
    val recruitment = Recruitment().apply(block)
    if (recruitment.term == null) {
        recruitment.term = Term(id = recruitment.termId ?: term().id)
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
