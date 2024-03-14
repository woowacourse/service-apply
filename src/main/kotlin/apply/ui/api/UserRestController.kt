package apply.ui.api

import apply.application.AuthenticateUserRequest
import apply.application.EditInformationRequest
import apply.application.EditPasswordRequest
import apply.application.RegisterUserRequest
import apply.application.ResetPasswordRequest
import apply.application.MemberAuthenticationService
import apply.application.UserResponse
import apply.application.MemberService
import apply.application.mail.MailService
import apply.domain.member.Member
import apply.security.LoginMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/api/users")
@RestController
class UserRestController(
    private val memberService: MemberService,
    private val memberAuthenticationService: MemberAuthenticationService,
    private val mailService: MailService
) {
    @PostMapping("/register")
    fun generateToken(@RequestBody @Valid request: RegisterUserRequest): ResponseEntity<ApiResponse<String>> {
        val token = memberAuthenticationService.generateTokenByRegister(request)
        return ResponseEntity.ok(ApiResponse.success(token))
    }

    @PostMapping("/login")
    fun generateToken(@RequestBody @Valid request: AuthenticateUserRequest): ResponseEntity<ApiResponse<String>> {
        val token = memberAuthenticationService.generateTokenByLogin(request)
        return ResponseEntity.ok(ApiResponse.success(token))
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody @Valid request: ResetPasswordRequest): ResponseEntity<Unit> {
        memberService.resetPassword(request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/edit-password")
    fun editPassword(
        @RequestBody @Valid request: EditPasswordRequest,
        @LoginMember member: Member
    ): ResponseEntity<Unit> {
        memberService.editPassword(member.id, request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/authentication-code")
    fun generateAuthenticationCode(
        @RequestParam email: String
    ): ResponseEntity<Unit> {
        val authenticationCode = memberAuthenticationService
            .generateAuthenticationCode(email)
        mailService.sendAuthenticationCodeMail(email, authenticationCode)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/authenticate-email")
    fun authenticateEmail(
        @RequestParam email: String,
        @RequestParam authenticationCode: String
    ): ResponseEntity<Unit> {
        memberAuthenticationService.authenticateEmail(email, authenticationCode)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun findAllByKeyword(
        @RequestParam keyword: String,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<List<UserResponse>>> {
        val responses = memberService.findAllByKeyword(keyword)
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @GetMapping("/me")
    fun getMyInformation(
        @LoginMember member: Member
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val response = memberService.getInformation(member.id)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PatchMapping("/information")
    fun editInformation(
        @RequestBody @Valid request: EditInformationRequest,
        @LoginMember member: Member
    ): ResponseEntity<Unit> {
        memberService.editInformation(member.id, request)
        return ResponseEntity.noContent().build()
    }
}
