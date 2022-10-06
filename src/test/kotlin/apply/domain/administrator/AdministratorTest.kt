package apply.domain.administrator

import apply.createAdministrator
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AdministratorTest : StringSpec({
    "관리자명과 비밀번호를 수정한다" {
        val administrator = createAdministrator("케이", "kth990303", "1234").apply {
            update(
                name = "케이케이",
                password = "12345"
            )
        }
        assertSoftly(administrator) {
            administrator.name shouldBe "케이케이"
            administrator.username shouldBe "kth990303"
            administrator.password shouldBe "12345"
        }
    }
})
