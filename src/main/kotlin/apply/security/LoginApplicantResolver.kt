package apply.security

import apply.application.ApplicantService
import apply.domain.applicant.Applicant
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginApplicantResolver(private val applicantService: ApplicantService) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.hasParameterAnnotation(LoginApplicant::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Applicant {
        val applicantEmail = webRequest.getAttribute(APPLICANT_EMAIL_ATTRIBUTE_NAME, SCOPE_REQUEST) as? String
            ?: throw AssertionError("요청에 loginApplicantEmail 속성이 반드시 존재해야 합니다")

        return applicantService.findByEmail(applicantEmail)
    }
}
