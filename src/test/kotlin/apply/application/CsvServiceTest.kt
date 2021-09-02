package apply.application

import apply.domain.evaluationItem.EvaluationItemRepository
import apply.utils.CsvGenerator
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CsvServiceTest {
    @MockK
    private lateinit var applicantService: ApplicantService

    @MockK
    private lateinit var evaluationTargetService: EvaluationTargetService

    @MockK
    private lateinit var evaluationItemRepository: EvaluationItemRepository

    @MockK
    private lateinit var csvGenerator: CsvGenerator

    private lateinit var csvService: CsvService

    @BeforeEach
    internal fun setUp() {
        csvService = CsvService(applicantService, evaluationTargetService, evaluationItemRepository, csvGenerator)
    }

    @Test
    fun `평가에 대한 csv 파일을 만든다`() {
    }
}
