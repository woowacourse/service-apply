package apply.log

import apply.utils.Log
import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.Date

@Component
@Aspect
class LoggerAspect(val objectMapper: ObjectMapper) {
    companion object : Log

    @Pointcut("execution(* apply..*Controller.*(..))")
    fun methodPointCut() {
    }

    @Before("methodPointCut()")
    fun methodLog(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

        val controllerName = proceedingJoinPoint.signature.declaringType.simpleName
        val methodName = proceedingJoinPoint.signature.name

        val params = mutableMapOf<String, Any>()
        params["controller"] = controllerName
        params["method"] = methodName
        params["params"] = objectMapper.writeValueAsString(request.parameterMap)
        params["time"] = Date()
        params["request_uri"] = request.requestURI
        params["http_method"] = request.method

        logger.info("{}", params)
        return proceedingJoinPoint.proceed()
    }

    @Pointcut("execution(* apply..*Handler.*(..))")
    fun errorPointCut() {
    }

    @Before("errorPointCut()")
    fun errorLog(proceedingJoinPoint: ProceedingJoinPoint): Any {
        val methodSignature = proceedingJoinPoint.signature as MethodSignature
        val parameters = methodSignature.method.parameters
        parameters.forEach { logger.error("message", it) }

        return proceedingJoinPoint.proceed()
    }
}
