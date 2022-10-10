package apply.infra.judgment

import apply.application.JudgmentAgency
import apply.application.JudgmentRequest
import apply.infra.AwsProperties
import apply.infra.AwsSqsProperties
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
    private val awsSqsProperties: AwsSqsProperties,
    private val objectMapper: ObjectMapper
) : JudgmentAgency {
    private val client: AmazonSQS = AmazonSQSClientBuilder
        .standard()
        .withCredentials(AWSStaticCredentialsProvider(awsProperties.awsCredentials))
        .withRegion(Regions.AP_NORTHEAST_2)
        .build()

    override fun requestJudge(request: JudgmentRequest) {
        val messageBody = objectMapper.writeValueAsString(SqsRequest(request))
        val message = SendMessageRequest()
            .withQueueUrl(awsSqsProperties.queueUrl)
            .withMessageBody(messageBody)
        runCatching { client.sendMessage(message) }
            .onSuccess { logger.info { "request: $messageBody, response: $it" } }
            .onFailure { e -> throw IllegalArgumentException(e) }
    }
}
