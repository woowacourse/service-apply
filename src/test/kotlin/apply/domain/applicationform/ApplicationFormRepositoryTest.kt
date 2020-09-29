package apply.domain.applicationform

import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
        Answers(
            mutableListOf(
                Answer("스타트업을 하고 싶습니다.", 1L),
                Answer("책임감", 2L)
            )
        )
    )

    @BeforeEach
    internal fun setUp() {
        applicationFormRepository.save(applicationForm)
    }

    @Test
    fun `저장한 Form을 찾아내는지 테스트한다`() {
        val form =
            applicationFormRepository.findByRecruitmentIdAndApplicantId(1L, 1L)!!
        assertThat(form.referenceUrl).isEqualTo("http://example.com")
        assertThat(form.id).isEqualTo(1L)
        assertThat(form.answers.items[0].contents).isEqualTo("스타트업을 하고 싶습니다.")
        assertThat(form.answers.items[1].contents).isEqualTo("책임감")
    }
}
