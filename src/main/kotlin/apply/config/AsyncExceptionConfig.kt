package apply.config

import apply.ui.api.AsyncExceptionHandler
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurerSupport

@Configuration
class AsyncExceptionConfig(
    private val asyncExceptionHandler: AsyncExceptionHandler
) : AsyncConfigurerSupport() {
    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return asyncExceptionHandler
    }
}
