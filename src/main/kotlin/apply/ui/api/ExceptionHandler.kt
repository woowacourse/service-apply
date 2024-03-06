package apply.ui.api

import apply.domain.applicationform.DuplicateApplicationException
import apply.domain.member.UnidentifiedMemberException
import apply.security.LoginFailedException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import support.infra.ExceededRequestException
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        val message = when (val exception = ex.cause) {
            is MissingKotlinParameterException -> "${exception.parameter.name.orEmpty()}: 널이어서는 안됩니다"
            is InvalidFormatException -> "${exception.path.last().fieldName.orEmpty()}: 올바른 형식이어야 합니다"
            else -> exception?.message.orEmpty()
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(message))
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(ex.messages()))
    }

    private fun MethodArgumentNotValidException.messages(): String {
        return bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage.orEmpty()}" }
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(LoginFailedException::class)
    fun handleUnauthorizedException(exception: LoginFailedException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(UnidentifiedMemberException::class)
    fun handleForbiddenException(exception: UnidentifiedMemberException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(NoSuchElementException::class, EntityNotFoundException::class)
    fun handleNotFoundException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(DuplicateApplicationException::class)
    fun handleConflictException(exception: DuplicateApplicationException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(ExceededRequestException::class)
    fun handleExceedRateLimitException(exception: ExceededRequestException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .header(HttpHeaders.RETRY_AFTER, "1")
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(exception.message))
    }
}
