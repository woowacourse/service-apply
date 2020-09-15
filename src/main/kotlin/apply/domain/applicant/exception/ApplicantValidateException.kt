package apply.domain.applicant.exception

import java.lang.RuntimeException

class ApplicantValidateException(errorField: String) : RuntimeException("$errorField 값이 기존 정보와 일치하지 않습니다")
