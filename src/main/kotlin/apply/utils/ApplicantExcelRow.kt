package apply.utils

import support.getMemberPropertiesByDeclaredOrder

data class ApplicantExcelRow(
    val applicantName: String,
    val applicantEmail: String,
    val applicantPhoneNumber: String,
    val applicantGender: String,
    val applicantBirthDay: String
) : ExcelRow {
    override fun getData(): List<Any?> {
        return getMemberPropertiesByDeclaredOrder(this)
    }
}
