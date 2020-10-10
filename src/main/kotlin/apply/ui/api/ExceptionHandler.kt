package apply.ui.api

import apply.domain.applicant.exception.ApplicantValidateException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [ApplicantValidateException::class])
    fun handleApplicantValidateException(exception: ApplicantValidateException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(value = [(IllegalArgumentException::class), (IllegalStateException::class)])
    fun handleRuntimeException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleGlobalException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(exception.message))
    }
}
