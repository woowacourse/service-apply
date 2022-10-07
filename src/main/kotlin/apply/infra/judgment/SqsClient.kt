package apply.infra.judgment

import apply.application.JudgmentAgency
import apply.application.JudgmentRequest
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.ProgrammingLanguage
import apply.infra.AwsProperties
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class SqsClient(
    awsProperties: AwsProperties,
    private val objectMapper: ObjectMapper
) : JudgmentAgency {
    private val client: AmazonSQS = AmazonSQSClientBuilder
        .standard()
        .withCredentials(AWSStaticCredentialsProvider(awsProperties.awsCredentials))
        .withRegion(Regions.AP_NORTHEAST_2)
        .build()

    override fun requestJudge(request: JudgmentRequest) {
        val message = SendMessageRequest()
            .withQueueUrl(client.getQueueUrl("queue-name").queueUrl)
            .withMessageBody(objectMapper.writeValueAsString(request))
        runCatching { client.sendMessage(message) }
            .onSuccess { logger.info { it.toString() } }
            .onFailure { e -> throw IllegalArgumentException(e) }
    }
}

data class SqsRequest(
    val judgmentId: Long,
    val language: ProgrammingLanguage,
    val testName: String,
    val testType: JudgmentType,
    val pullRequestUrl: String,
    val commitHash: String
) {
}
