package apply.infra.judgment

import apply.application.JudgmentRequest
import apply.domain.judgment.JudgmentAgency
import apply.infra.mail.AwsProperties
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.services.sqs.model.InvalidMessageContentsException
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class SqsClient(
    awsProperties: AwsProperties
) : JudgmentAgency {

    private val client = AmazonSQSClientBuilder
        .standard()
        .withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    awsProperties.accessKey,
                    awsProperties.secretKey
                )
            )
        )
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

    companion object {
        private const val QUEUE_NAME: String = "queue-name"
        private val OBJECT_MAPPER: ObjectMapper = ObjectMapper()

        fun serialize(judgmentRequest: JudgmentRequest): String = OBJECT_MAPPER.writeValueAsString(judgmentRequest)
            ?: throw IllegalArgumentException("데이터 직렬화에 실패했습니다. : $judgmentRequest")
    }
}
