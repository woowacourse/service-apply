package apply

import apply.application.AdministratorData
import apply.application.AdministratorResponse
import apply.domain.administrator.Administrator

private const val ADMINISTRATOR_NAME: String = "관리자"
const val ADMINISTRATOR_USERNAME: String = "admin"
const val ADMINISTRATOR_PASSWORD: String = "1234"

fun createAdministrator(
    name: String = ADMINISTRATOR_NAME,
    username: String = ADMINISTRATOR_USERNAME,
    password: String = ADMINISTRATOR_PASSWORD,
    id: Long = 0L
): Administrator {
    return Administrator(name, username, password, id)
}

fun createAdministratorData(
    name: String = ADMINISTRATOR_NAME,
    username: String = ADMINISTRATOR_USERNAME,
    password: String = ADMINISTRATOR_PASSWORD,
    confirmPassword: String = ADMINISTRATOR_PASSWORD
): AdministratorData {
    return AdministratorData(name, username, password, confirmPassword)
}

fun createAdministratorResponse(
    id: Long = 0L,
    name: String = ADMINISTRATOR_NAME,
    username: String = ADMINISTRATOR_USERNAME,
    password: String = ADMINISTRATOR_PASSWORD
): AdministratorResponse {
    return AdministratorResponse(id, name, username, password)
}
