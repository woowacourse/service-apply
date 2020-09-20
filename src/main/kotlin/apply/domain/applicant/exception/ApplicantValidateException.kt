package apply.domain.applicant.exception

import java.lang.RuntimeException

class ApplicantValidateException : RuntimeException("요청 정보가 기존 지원자 정보와 일치하지 않습니다")
