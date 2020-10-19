package apply.domain.administrator

import support.domain.BaseEntity

class Administrator(
    private val name: String,
    id: Long
) : BaseEntity(id)
