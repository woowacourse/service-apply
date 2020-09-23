package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import support.createLocalDate
import support.createLocalDateTime

@ExtendWith(MockKExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
class EvaluationTargetServiceTest(
    private val evaluationTargetRepository: EvaluationTargetRepository
) {
    @MockK
    private lateinit var evaluationRepository: EvaluationRepository

    @MockK
    private lateinit var applicationFormRepository: ApplicationFormRepository

    @MockK
    private lateinit var applicantRepository: ApplicantRepository

    @MockK
    private lateinit var cheaterRepository: CheaterRepository

    private lateinit var evaluationTargetService: EvaluationTargetService

    @BeforeEach
    fun setUp() {
        evaluationTargetService = EvaluationTargetService(
            evaluationTargetRepository,
            evaluationRepository,
            applicationFormRepository,
            applicantRepository,
            cheaterRepository
        )
    }

    @Test
    fun `이전 평가가 없고 저장 된 평가 대상자가 없을 경우 저장하고 불러온다`() {
        // given
        val firstEvaluation = Evaluation(
            id = 1L,
            title = "평가1",
            description = "평가1에 대한 설명",
            recruitmentId = 1L,
            beforeEvaluationId = 0L
        )

        val applicationForms = listOf(
            ApplicationForm(
                id = 1L,
                referenceUrl = "",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitmentId = 1L,
                applicantId = 1L,
                answers = Answers(
                    mutableListOf(
                        Answer("고객에게 가치를 전달하고 싶습니다.", 1L),
                        Answer("도전, 끈기", 2L)
                    )
                )
            ),
            ApplicationForm(
                id = 2L,
                referenceUrl = "https://www.google.com",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitmentId = 1L,
                applicantId = 2L,
                answers = Answers(
                    mutableListOf(
                        Answer("스타트업을 하고 싶습니다.", 1L),
                        Answer("책임감", 2L)
                    )
                )
            ),
            ApplicationForm(
                id = 3L,
                referenceUrl = "https://www.google.com",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitmentId = 1L,
                applicantId = 3L,
                answers = Answers(
                    mutableListOf(
                        Answer("양자 컴퓨터를 배우고 싶습니다.", 1L),
                        Answer("노력", 2L)
                    )
                )
            )
        )

        val applicants = listOf(
            Applicant(
                id = 1L,
                name = "홍길동1",
                email = "a@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 4, 17),
                password = "password"
            ),
            Applicant(
                id = 2L,
                name = "홍길동2",
                email = "b@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.FEMALE,
                birthday = createLocalDate(2020, 5, 5),
                password = "password"
            ),
            Applicant(
                id = 3L,
                name = "홍길동3",
                email = "c@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1),
                password = "password"
            )
        )

        // when
        every { evaluationRepository.findByIdOrNull(any()) } returns firstEvaluation
        every { applicationFormRepository.findByRecruitmentId(any()) } returns applicationForms
        every { applicantRepository.findAllById(any()) } returns applicants
        every { cheaterRepository.existsByApplicantId(1L) } returns false
        every { cheaterRepository.existsByApplicantId(2L) } returns false
        every { cheaterRepository.existsByApplicantId(3L) } returns true

        val actual: List<EvaluationTarget> = evaluationTargetService.load(firstEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(2) },
            { assertThat(actual[0].applicantId).isEqualTo(1L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(EvaluationStatus.WAITING) },
            { assertThat(actual[1].applicantId).isEqualTo(2L) },
            { assertThat(actual[1].evaluationStatus).isEqualTo(EvaluationStatus.WAITING) }
        )
    }

    @Test
    fun `이전 평가가 있고 저장 된 평가 대상자가 없을 경우 저장하고 불러온다`() {
        // given
        val savedEvaluationTargets = listOf(
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 1L,
                evaluationStatus = EvaluationStatus.WAITING
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 2L,
                evaluationStatus = EvaluationStatus.PASS
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 3L,
                evaluationStatus = EvaluationStatus.PASS
            )
        )

        evaluationTargetRepository.saveAll(savedEvaluationTargets)

        // when
        val secondEvaluation = Evaluation(
            id = 2L,
            title = "평가2",
            description = "평가2에 대한 설명",
            recruitmentId = 1L,
            beforeEvaluationId = 1L
        )

        every { evaluationRepository.findByIdOrNull(any()) } returns secondEvaluation
        every { cheaterRepository.existsByApplicantId(1L) } returns false
        every { cheaterRepository.existsByApplicantId(2L) } returns false
        every { cheaterRepository.existsByApplicantId(3L) } returns true

        val actual: List<EvaluationTarget> = evaluationTargetService.load(secondEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(1) },
            { assertThat(actual[0].applicantId).isEqualTo(2L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(EvaluationStatus.WAITING) }
        )
    }

    @Test
    fun `이전 평가가 없고 저장 된 평가 대상자가 있을 경우 갱신하여 불러온다`() {
        // given
        val savedEvaluationTargets = listOf(
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 1L,
                evaluationStatus = EvaluationStatus.FAIL
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 2L,
                evaluationStatus = EvaluationStatus.PASS
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 3L,
                evaluationStatus = EvaluationStatus.PASS
            )
        )

        evaluationTargetRepository.saveAll(savedEvaluationTargets)

        // when
        val firstEvaluation = Evaluation(
            id = 1L,
            title = "평가1",
            description = "평가1에 대한 설명",
            recruitmentId = 1L,
            beforeEvaluationId = 0L
        )

        val allApplicationForms = listOf(
            ApplicationForm(
                id = 1L,
                referenceUrl = "",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitmentId = 1L,
                applicantId = 1L,
                answers = Answers(
                    mutableListOf(
                        Answer("고객에게 가치를 전달하고 싶습니다.", 1L),
                        Answer("도전, 끈기", 2L)
                    )
                )
            ),
            ApplicationForm(
                id = 2L,
                referenceUrl = "https://www.google.com",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitmentId = 1L,
                applicantId = 2L,
                answers = Answers(
                    mutableListOf(
                        Answer("스타트업을 하고 싶습니다.", 1L),
                        Answer("책임감", 2L)
                    )
                )
            ),
            ApplicationForm(
                id = 3L,
                referenceUrl = "https://www.google.com",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitmentId = 1L,
                applicantId = 3L,
                answers = Answers(
                    mutableListOf(
                        Answer("양자 컴퓨터를 배우고 싶습니다.", 1L),
                        Answer("노력", 2L)
                    )
                )
            ),
            ApplicationForm(
                id = 4L,
                referenceUrl = "",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitmentId = 1L,
                applicantId = 4L,
                answers = Answers(
                    mutableListOf(
                        Answer("협업하는 개발자가 되고 싶습니다.", 1L),
                        Answer("협업", 2L)
                    )
                )
            )
        )

        val addingApplicant = Applicant(
            id = 4L,
            name = "홍길동4",
            email = "d@email.com",
            phoneNumber = "010-0000-0000",
            gender = Gender.MALE,
            birthday = createLocalDate(2020, 11, 11),
            password = "password"
        )

        val allApplicants = listOf(
            Applicant(
                id = 1L,
                name = "홍길동1",
                email = "a@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 4, 17),
                password = "password"
            ),
            Applicant(
                id = 2L,
                name = "홍길동2",
                email = "b@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.FEMALE,
                birthday = createLocalDate(2020, 5, 5),
                password = "password"
            ),
            Applicant(
                id = 3L,
                name = "홍길동3",
                email = "c@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1),
                password = "password"
            ),
            addingApplicant
        )

        every { evaluationRepository.findByIdOrNull(any()) } returns firstEvaluation
        every { applicationFormRepository.findByRecruitmentId(any()) } returns allApplicationForms
        every { applicantRepository.findAllById(listOf(1L, 2L, 3L, 4L)) } returns allApplicants
        every { applicantRepository.findAllById(setOf(4L)) } returns listOf(addingApplicant)
        every { cheaterRepository.existsByApplicantId(1L) } returns false
        every { cheaterRepository.existsByApplicantId(2L) } returns false
        every { cheaterRepository.existsByApplicantId(3L) } returns true
        every { cheaterRepository.existsByApplicantId(4L) } returns false

        val actual: List<EvaluationTarget> = evaluationTargetService.load(firstEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(3) },
            { assertThat(actual[0].applicantId).isEqualTo(1L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(EvaluationStatus.FAIL) },
            { assertThat(actual[1].applicantId).isEqualTo(2L) },
            { assertThat(actual[1].evaluationStatus).isEqualTo(EvaluationStatus.PASS) },
            { assertThat(actual[2].applicantId).isEqualTo(4L) },
            { assertThat(actual[2].evaluationStatus).isEqualTo(EvaluationStatus.WAITING) }
        )
    }

    @Test
    fun `이전 평가가 있고 저장 된 평가 대상자가 있을 경우 갱신하고 불러온다`() {
        // given
        val savedEvaluationTargets = listOf(
            EvaluationTarget(
                evaluationId = 2L,
                administratorId = 0L,
                applicantId = 1L,
                evaluationStatus = EvaluationStatus.WAITING
            ),
            EvaluationTarget(
                evaluationId = 2L,
                administratorId = 0L,
                applicantId = 2L,
                evaluationStatus = EvaluationStatus.PASS
            ),
            EvaluationTarget(
                evaluationId = 2L,
                administratorId = 0L,
                applicantId = 3L,
                evaluationStatus = EvaluationStatus.PASS
            )
        )

        evaluationTargetRepository.saveAll(savedEvaluationTargets)

        val loadingEvaluationTargetsFromBeforeEvaluation = listOf(
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 1L,
                evaluationStatus = EvaluationStatus.FAIL
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 2L,
                evaluationStatus = EvaluationStatus.PASS
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 3L,
                evaluationStatus = EvaluationStatus.PASS
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 0L,
                applicantId = 4L,
                evaluationStatus = EvaluationStatus.PASS
            )
        )

        evaluationTargetRepository.saveAll(loadingEvaluationTargetsFromBeforeEvaluation)

        // when
        val secondEvaluation = Evaluation(
            id = 2L,
            title = "평가2",
            description = "평가2에 대한 설명",
            recruitmentId = 1L,
            beforeEvaluationId = 1L
        )

        val addingApplicant = Applicant(
            id = 4L,
            name = "홍길동4",
            email = "d@email.com",
            phoneNumber = "010-0000-0000",
            gender = Gender.MALE,
            birthday = createLocalDate(2020, 11, 11),
            password = "password"
        )

        every { evaluationRepository.findByIdOrNull(any()) } returns secondEvaluation
        every { applicantRepository.findAllById(setOf(4L)) } returns listOf(addingApplicant)
        every { cheaterRepository.existsByApplicantId(1L) } returns false
        every { cheaterRepository.existsByApplicantId(2L) } returns false
        every { cheaterRepository.existsByApplicantId(3L) } returns true
        every { cheaterRepository.existsByApplicantId(4L) } returns false

        val actual: List<EvaluationTarget> = evaluationTargetService.load(secondEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(2) },
            { assertThat(actual[0].applicantId).isEqualTo(2L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(EvaluationStatus.PASS) },
            { assertThat(actual[1].applicantId).isEqualTo(4L) },
            { assertThat(actual[1].evaluationStatus).isEqualTo(EvaluationStatus.WAITING) }
        )
    }
}
