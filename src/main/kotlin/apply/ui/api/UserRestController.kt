package apply.ui.api

import apply.application.UserAuthenticationService
import apply.application.AuthenticateUserRequest
import apply.application.EditPasswordRequest
import apply.application.RegisterUserRequest
import apply.application.ResetPasswordRequest
import apply.application.UserService
import apply.application.mail.MailService
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserRestController(
    private val userService: UserService,
    private val userAuthenticationService: UserAuthenticationService,
    private val mailService: MailService,
) {
    @PostMapping("/register")
    fun generateToken(@RequestBody @Valid request: RegisterUserRequest): ResponseEntity<ApiResponse<String>> {
        val token = userAuthenticationService.generateToken(request)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/login")
    fun generateToken(@RequestBody @Valid request: AuthenticateUserRequest): ResponseEntity<ApiResponse<String>> {
        val token = userAuthenticationService.generateTokenByLogin(request)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody @Valid request: ResetPasswordRequest): ResponseEntity<Unit> {
        val newPassword = userService.resetPassword(request)
        mailService.sendPasswordResetMail(request, newPassword)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/edit-password")
    fun editPassword(
        @RequestBody @Valid request: EditPasswordRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        userService.editPassword(applicant.id, request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/authenticate-email")
    fun authenticateEmail(
        @RequestParam email: String,
        @RequestParam authenticateCode: String
    ): ResponseEntity<Unit> {
        userAuthenticationService.authenticateEmail(email, authenticateCode)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
