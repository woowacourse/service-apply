package support.test.context.event

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

/**
 * Spring Boot 2.4.2 이후 `@RecordApplicationEvents`로 대체
 */
@TestConfiguration
class RecordEventsConfiguration {
    @Bean
    fun applicationEvents(): Events {
        return Events()
    }
}
