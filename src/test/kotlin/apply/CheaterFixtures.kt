package apply

import apply.application.CheaterData
import apply.domain.cheater.Cheater
import java.time.LocalDateTime

private const val CHEATER_EMAIL: String = "cheater@email.com"
private const val CHEATER_DESCRIPTION: String = "코딩테스트 부정행위자 입니다."

fun createCheater(
    email: String = CHEATER_EMAIL,
    description: String = CHEATER_DESCRIPTION,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
): Cheater {
    return Cheater(email, description, createdDateTime, id)
}

fun createCheaterData(
    email: String = CHEATER_EMAIL,
    description: String = CHEATER_DESCRIPTION
): CheaterData {
    return CheaterData(email, description)
}
