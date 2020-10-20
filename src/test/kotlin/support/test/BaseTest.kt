package support.test

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class BaseTest

@DataJpaTest
abstract class BaseDataJpaTest : BaseTest()

@SpringBootTest
abstract class BaseSpringBootTest : BaseTest()
