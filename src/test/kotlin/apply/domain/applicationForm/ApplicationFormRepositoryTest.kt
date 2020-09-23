package apply.domain.applicationForm

import com.helger.commons.mock.CommonsAssert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
class ApplicationFormRepositoryTest(
        private val applicationFormRepository: ApplicationFormRepository
) {
    private val applicationForm = ApplicationForm(
            1L,
            1L,
            "http://example.com",
            1L
    )

    @BeforeEach
    internal fun setUp() {
        applicationFormRepository.save(applicationForm)
    }

    @Test
    @DisplayName("저장한 Form을 찾아내는지 테스트한다")
    internal fun formFindTest() {
        val form =
                applicationFormRepository.findByApplicantIdAndRecruitmentId(1L, 1L)!!
        assertEquals("http://example.com", form.referenceUrl)
        assertEquals(1L, form.id)
    }
}