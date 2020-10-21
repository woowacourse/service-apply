package apply.infra.mail

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("aws")
@ConstructorBinding
data class AwsProperties(val accessKey: String, val secretKey: String)
