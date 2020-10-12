package apply.ui.api

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {
    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
    }
}
