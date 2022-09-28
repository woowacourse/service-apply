package apply.acceptance

import apply.config.Database
import apply.createUser
import apply.security.LoginUserResolver
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.LOCATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

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

    afterTest {
        database.clear(database.retrieveTables())
    }
})

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
