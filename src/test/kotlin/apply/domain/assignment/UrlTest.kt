package apply.domain.assignment

import apply.domain.mission.SubmissionMethod.GENERIC_URL
import apply.domain.mission.SubmissionMethod.PRIVATE_REPOSITORY
import apply.domain.mission.SubmissionMethod.PUBLIC_PULL_REQUEST
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class UrlTest : StringSpec({
    fun createPullRequestUrl(value: String): Url = Url.of(value, PUBLIC_PULL_REQUEST)
    fun createRepositoryUrl(value: String): Url = Url.of(value, PRIVATE_REPOSITORY)
    fun createGenericUrl(value: String): Url = Url.of(value, GENERIC_URL)

    "풀 리퀘스트 URL 형식을 지원한다" {
        shouldNotThrowAny { createPullRequestUrl("https://github.com/woowacourse/service-apply/pull/734") }
        shouldThrow<IllegalArgumentException> { createPullRequestUrl("https://github.com/woowacourse/pull/734") }
        shouldThrow<IllegalArgumentException> { createPullRequestUrl("https://github.com/woowacourse/service-apply/pull") }
        shouldThrow<IllegalArgumentException> { createPullRequestUrl("https://github.com/woowacourse/service-apply/pull/") }
        shouldThrow<IllegalArgumentException> { createPullRequestUrl("https://github.com/woowacourse/service-apply/issues/722") }
        shouldThrow<IllegalArgumentException> { createPullRequestUrl("https://github.com/woowacourse/service-apply/pull/734/734") }
    }

    "저장소 URL 형식을 지원한다" {
        shouldNotThrowAny { createRepositoryUrl("https://github.com/woowacourse/service-apply") }
        shouldThrow<IllegalArgumentException> { createRepositoryUrl("https://github.com/woowacourse") }
        shouldThrow<IllegalArgumentException> { createRepositoryUrl("https://github.com/woowacourse/") }
    }

    "일반 URL 형식을 지원한다" {
        shouldNotThrowAny { createGenericUrl("http://example.com") }
        shouldNotThrowAny { createGenericUrl("https://example.com") }
        shouldNotThrowAny { createGenericUrl("https://apply.techcourse.co.kr") }
        shouldNotThrowAny { createGenericUrl("https://sub.domain.example.com/path/to/resource") }
        shouldNotThrowAny { createGenericUrl("https://example.com?query=param&foo=bar") }
        shouldNotThrowAny { createGenericUrl("https://example.com#section") }
        shouldNotThrowAny { createGenericUrl("https://example.com:8080/path") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("http//missing-colon.com") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("htp://typo.com") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("://missing-scheme.com") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("example.com") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("ftp://example.com") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("https://") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("   https://example.com") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("https://example.com  ") }
        shouldThrow<IllegalArgumentException> { createGenericUrl("https://exa mple.com") }
    }
})
