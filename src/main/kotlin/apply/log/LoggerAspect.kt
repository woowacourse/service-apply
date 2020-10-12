package apply.log

import apply.utils.Log
import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.Arrays
import java.util.Date

@Component
@Aspect
class LoggerAspect(val objectMapper: ObjectMapper) {
    companion object : Log

    @Pointcut("execution(* apply..*Controller.*(..))")
    fun methodPointCut() {
    }

    @Before("methodPointCut()")
    fun methodLog(joinPoint: JoinPoint) {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        val controllerName = joinPoint.signature.declaringType.simpleName
        val methodName = joinPoint.signature.name

        val params = mutableMapOf<String, Any>()
        params["controller"] = controllerName
        params["method"] = methodName
        params["params"] = objectMapper.writeValueAsString(request.parameterMap)
        params["time"] = Date()
        params["request_uri"] = request.requestURI
        params["http_method"] = request.method

        logger.info("{}", params)
    }

    @Pointcut("execution(* apply..*Service.*(..))")
    fun errorPointCut() {
    }

    @AfterThrowing(pointcut = "errorPointCut()", throwing = "ex")
    fun errorLog(joinPoint: JoinPoint, ex: Throwable) {
        val signature = joinPoint.signature
        val methodName = signature.name
        val args = Arrays.toString(joinPoint.args)
        val params = mutableMapOf<String, String>()
        params["methodName"] = methodName
        params["arguments"] = args
        params["message"] = ex.message ?: ""

        logger.error("{}", params)
    }
}
