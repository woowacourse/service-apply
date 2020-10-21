package apply.application

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("application")
@ConstructorBinding
data class ApplicationProperties(val url: String)
