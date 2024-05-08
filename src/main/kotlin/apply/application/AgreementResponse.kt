package apply.application

import apply.domain.agreement.Agreement

data class AgreementResponse(val id: Long, val version: Int, val content: String) {
    constructor(agreement: Agreement) : this(agreement.id, agreement.version, agreement.content)
}
