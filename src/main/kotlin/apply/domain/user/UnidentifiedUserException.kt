package apply.domain.user

class UnidentifiedUserException(
    message: String = "요청 정보가 기존 회원 정보와 일치하지 않습니다."
) : RuntimeException(message)
