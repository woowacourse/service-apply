package apply.ui.api

import apply.domain.user.UserAuthenticationException
import apply.domain.applicationform.DuplicateApplicationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(UserAuthenticationException::class)
    fun handleUnauthorizedException(exception: UserAuthenticationException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFoundException(exception: EntityNotFoundException): ResponseEntity<ApiResponse<Unit>> {
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

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(exception.message))
    }
}
