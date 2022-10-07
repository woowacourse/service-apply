package apply.infra.judgment

import apply.application.JudgmentRequest
import apply.domain.judgment.JudgmentAgency
import apply.domain.judgment.ProgrammingLanguage
import apply.infra.AwsProperties
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.InvalidMessageContentsException
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

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

    private val queueUrl = client.getQueueUrl(QUEUE_NAME).queueUrl

    override fun requestJudge(request: JudgmentRequest) {
        val message = SendMessageRequest().withQueueUrl(queueUrl)
            .withMessageBody(serialize(request))

        try {
            val messageResult = client.sendMessage(message)
        } catch (e: InvalidMessageContentsException) {
            throw IllegalArgumentException("요청 메세지에 허용되지 않은 문자가 있습니다.", e)
        } catch (e: UnsupportedOperationException) {
            throw IllegalArgumentException("지원하지 않는 동작 요청입니다", e)
        }
    }

data class SqsRequest(
    val judgmentId: Long,
    val language: ProgrammingLanguage,
    val testName: String,
    val testType: JudgmentType,
    val pullRequestUrl: String,
    val commitHash: String
}
