package apply.infra.github

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("github")
@ConstructorBinding
data class GitHubProperties(
    val uri: String,
    val accessKey: String
)
