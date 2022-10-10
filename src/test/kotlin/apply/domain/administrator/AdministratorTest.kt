package apply.domain.administrator

import apply.createAdministrator
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AdministratorTest : StringSpec({
    "관리자명과 비밀번호를 수정한다" {
        val administrator = createAdministrator("케이", "kth990303", "1234")
        administrator.update("조조그린", "5678")
        assertSoftly(administrator) {
            administrator.name shouldBe "조조그린"
            administrator.password shouldBe "5678"
        }
    }
})
