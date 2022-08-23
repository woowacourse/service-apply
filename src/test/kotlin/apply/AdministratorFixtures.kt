package apply

import apply.application.AdministratorData
import apply.application.AdministratorResponse
import apply.domain.administrator.Administrator

const val ADMINISTRATOR_NAME = "admin"
const val ADMINISTRATOR_USERNAME = "master"
const val ADMINISTRATOR_PASSWORD = "abcd1234"

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
    passwordConfirmation: String = ADMINISTRATOR_PASSWORD
): AdministratorData {
    return AdministratorData(name, username, password, passwordConfirmation)
}

fun createAdministratorResponse(
    id: Long = 0L,
    name: String = ADMINISTRATOR_NAME,
    username: String = ADMINISTRATOR_USERNAME,
    password: String = ADMINISTRATOR_PASSWORD
): AdministratorResponse {
    return AdministratorResponse(id, name, username, password)
}
