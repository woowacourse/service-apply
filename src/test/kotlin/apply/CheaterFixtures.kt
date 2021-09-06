package apply

import apply.application.CheaterData

private const val CHEATER_EMAIL: String = "cheater@email.com"
private const val CHEATER_DESCRIPTION: String = "코딩테스트 부정행위자 입니다."

fun createCheaterData(
    email: String = CHEATER_EMAIL,
    description: String = CHEATER_DESCRIPTION
): CheaterData {
    return CheaterData(email, description)
}
