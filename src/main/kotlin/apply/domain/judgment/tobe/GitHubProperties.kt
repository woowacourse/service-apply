package apply.domain.judgment.tobe

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("github")
@ConstructorBinding
data class GitHubProperties(val accessKey: String)
