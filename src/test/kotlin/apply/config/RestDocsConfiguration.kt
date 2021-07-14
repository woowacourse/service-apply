package apply.config

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.operation.preprocess.Preprocessors

@TestConfiguration
class RestDocsConfiguration {
    @Bean
    fun restDocsMockMvcConfigurationCustomizer(): RestDocsMockMvcConfigurationCustomizer {
        return RestDocsMockMvcConfigurationCustomizer {
            it.operationPreprocessors()
                .withRequestDefaults(Preprocessors.prettyPrint())
                .withResponseDefaults(Preprocessors.prettyPrint())
        }
    }
}
