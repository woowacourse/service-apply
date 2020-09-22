package apply.domain.answer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
internal class AnswerRepositoryTest (private val answerRepository: AnswerRepository){
    @BeforeEach
    internal fun setUp() {
        val answers = listOf(
                Answer(
                        "내용1",
                        1,
                        1
                ),
                Answer(
                        "내용2",
                        1,
                        2
                )
        )
        answerRepository.saveAll(answers)
    }

    @Test
    @DisplayName("지원서 ID로 모든 지원서 질문의 답을 찾는다")
    internal fun findAllByApplicationFormIdTest() {
        assertThat(answerRepository.findAllByApplicationFormId(1)).hasSize(2)
    }

    @Test
    @DisplayName("지원서 ID로 모든 지원서 질문의 답을 삭제한다")
    internal fun deleteAllByApplicationFormIdTest() {
        answerRepository.deleteAllByApplicationFormId(1)
        assertThat(answerRepository.findAllByApplicationFormId(1)).hasSize(0)
    }
}