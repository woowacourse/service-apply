package apply.domain.applicant

import apply.createApplicant
import org.junit.jupiter.api.BeforeEach

internal class ApplicantTest {
    private lateinit var applicant: Applicant

    @BeforeEach
    internal fun setUp() {
        applicant = createApplicant()
    }
}
