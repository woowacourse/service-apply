package apply.infra.github

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.time.ZoneId
import java.time.ZonedDateTime

private class CommitDeserializer : JsonDeserializer<CommitResponse>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): CommitResponse {
        val jsonNode: JsonNode = parser.codec.readTree(parser)
        return CommitResponse(
            jsonNode["sha"].asText(),
            ZonedDateTime.parse(jsonNode["commit"]["committer"]["date"].asText())
                .withZoneSameInstant(ZoneId.systemDefault())
        )
    }
}

@JsonDeserialize(using = CommitDeserializer::class)
data class CommitResponse(val hash: String, val date: ZonedDateTime)
