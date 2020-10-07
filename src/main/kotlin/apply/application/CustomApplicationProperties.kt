package apply.application

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("application")
class CustomApplicationProperties {
    lateinit var url: String
}
