package support.test.spec

import io.kotest.core.spec.AfterTest
import io.kotest.core.spec.BeforeTest
import io.kotest.core.spec.Spec
import io.kotest.core.test.isRootTest

fun Spec.beforeRootTest(f: BeforeTest) {
    beforeTest {
        if (it.isRootTest()) f(it)
    }
}

fun Spec.afterRootTest(f: AfterTest) {
    afterTest {
        val (testcase) = it
        if (testcase.isRootTest()) f(it)
    }
}
