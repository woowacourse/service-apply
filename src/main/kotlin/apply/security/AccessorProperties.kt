package apply.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("accessor")
@ConstructorBinding
data class AccessorProperties(val keys: Map<String, String>)
