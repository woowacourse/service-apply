package apply

import apply.application.AdministratorResponse
import apply.application.CreateAdministratorFormData
import apply.application.UpdateAdministratorFormData
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
): CreateAdministratorFormData {
    return CreateAdministratorFormData(name, username, password, confirmPassword)
}

fun createAdministratorUpdateFormData(
    id: Long = 0L,
    name: String = "Admin",
    password: String = "abcd12345",
    passwordConfirmation: String = "abcd12345"
): UpdateAdministratorFormData {
    return UpdateAdministratorFormData(id, name, password, passwordConfirmation)
}

fun createAdministratorResponse(
    id: Long = 0L,
    name: String = ADMINISTRATOR_NAME,
    username: String = ADMINISTRATOR_USERNAME,
    password: String = ADMINISTRATOR_PASSWORD
): AdministratorResponse {
    return AdministratorResponse(id, name, username, password)
}
