package apply.security.accessor

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("lambda")
@ConstructorBinding
data class LambdaAccessorProperties(val key: String)
