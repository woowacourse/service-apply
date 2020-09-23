package apply.domain.answer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
internal class applicantApplicantAnswerRepositoryTest (private val applicantAnswerRepository: ApplicantAnswerRepository){
    @BeforeEach
    internal fun setUp() {
        val answers = listOf(
                ApplicantAnswer(
                        "내용1",
                        1,
                        1
                ),
                ApplicantAnswer(
                        "내용2",
                        1,
                        2
                )
        )
        applicantAnswerRepository.saveAll(answers)
    }

    @Test
    @DisplayName("지원서 ID로 모든 지원서 질문의 답을 찾는다")
    internal fun findAllByApplicationFormIdTest() {
        assertThat(applicantAnswerRepository.findAllByApplicationFormId(1)).hasSize(2)
    }

    @Test
    @DisplayName("지원서 ID로 모든 지원서 질문의 답을 삭제한다")
    internal fun deleteAllByApplicationFormIdTest() {
        applicantAnswerRepository.deleteAllByApplicationFormId(1)
        assertThat(applicantAnswerRepository.findAllByApplicationFormId(1)).hasSize(0)
    }
}