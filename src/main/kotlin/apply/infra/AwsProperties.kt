package apply.infra

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("aws")
@ConstructorBinding
data class AwsProperties(val accessKey: String, val secretKey: String) {
    val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
}

@ConfigurationProperties("aws.sqs")
@ConstructorBinding
data class AwsSqsProperties(val queueUrl: String)
