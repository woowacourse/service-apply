package apply.infra.github

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
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

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class InvitationResponse(
    val id: Long,
    val repository: RepositoryResponse,
    val createdAt: ZonedDateTime,
    val expired: Boolean,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RepositoryResponse(
    val id: Long,
    val name: String,
    val fullName: String,
    val owner: OwnerResponse,
    val private: Boolean,
    val fork: Boolean,
)

data class OwnerResponse(
    val login: String,
)
