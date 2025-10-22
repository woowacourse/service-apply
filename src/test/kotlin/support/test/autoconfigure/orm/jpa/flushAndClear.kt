package support.test.autoconfigure.orm.jpa

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

fun TestEntityManager.flushAndClear() {
    entityManager.flush()
    entityManager.clear()
}
