package apply.acceptance

import apply.config.Database
import apply.createUser
import apply.security.LoginUserResolver
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.mockk.every
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import support.test.TestEnvironment
import support.test.spec.afterRootTest
import support.test.spec.beforeRootTest

@SpykBean(LoginUserResolver::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestEnvironment
abstract class AcceptanceTest : BehaviorSpec() {
    @Autowired
    lateinit var client: WebTestClient

    @Autowired
    private lateinit var database: Database

    @Autowired
    private lateinit var loginUserResolver: LoginUserResolver

    init {
        extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

        beforeRootTest {
            every { loginUserResolver.resolveArgument(any(), any(), any(), any()) } returns createUser()
        }

        afterRootTest {
            database.clear(database.retrieveTables())
        }
    }

    fun recruitment(block: Recruitment.() -> Unit = {}): Recruitment {
        return client.recruitment(block)
    }

    fun term(block: Term.() -> Unit = {}): Term {
        return client.term(block)
    }
}
