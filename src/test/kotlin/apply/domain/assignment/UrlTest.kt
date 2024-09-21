package apply.domain.assignment

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class UrlTest : StringSpec({
    "풀 리퀘스트 URL 형식을 지원한다" {
        shouldNotThrowAny { Url("https://github.com/woowacourse/service-apply/pull/734") }
        shouldThrow<IllegalArgumentException> { Url("https://github.com/woowacourse/pull/734") }
        shouldThrow<IllegalArgumentException> { Url("https://github.com/woowacourse/service-apply/pull") }
        shouldThrow<IllegalArgumentException> { Url("https://github.com/woowacourse/service-apply/pull/") }
        shouldThrow<IllegalArgumentException> { Url("https://github.com/woowacourse/service-apply/issues/722") }
        shouldThrow<IllegalArgumentException> { Url("https://github.com/woowacourse/service-apply/pull/734/734") }
    }

    "저장소 URL 형식을 지원한다" {
        shouldNotThrowAny { Url("https://github.com/woowacourse/service-apply") }
        shouldThrow<IllegalArgumentException> { Url("https://github.com/woowacourse") }
        shouldThrow<IllegalArgumentException> { Url("https://github.com/woowacourse/") }
    }
})
