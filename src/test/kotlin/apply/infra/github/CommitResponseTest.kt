package apply.infra.github

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.ZonedDateTime

class CommitResponseTest : StringSpec({
    val objectMapper = jacksonObjectMapper()

    "JSON 역직렬화" {
        val json = """
            {
              "sha": "6dcb09b5b57875f334f61aebed695e2e4193db5e",
              "commit": {
                "committer": {
                  "date": "2011-04-14T16:00:49Z"
                }
              }
            }
        """.trimIndent()
        val actual = objectMapper.readValue<CommitResponse>(json)
        actual.date shouldBe ZonedDateTime.parse("2011-04-15T01:00:49+09:00[Asia/Seoul]")
    }
})
