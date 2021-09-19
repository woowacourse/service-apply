package apply.application

import apply.EVALUATION_ID
import apply.EVALUATION_ITEM_ID
import apply.NOTE
import apply.SCORE
import apply.createApplicant
import apply.createEvaluation
import apply.createEvaluationAnswer
import apply.createEvaluationItem
import apply.createEvaluationTarget
import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormAnswer
import apply.domain.applicationform.ApplicationFormAnswers
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import apply.domain.evaluationtarget.EvaluationStatus.WAITING
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.user.Gender
import apply.domain.user.Password
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.data.repository.findByIdOrNull
import support.createLocalDate
import support.createLocalDateTime
import support.test.RepositoryTest

@RepositoryTest
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

    @MockK
    private lateinit var evaluationItemRepository: EvaluationItemRepository

    private lateinit var evaluationTargetService: EvaluationTargetService

    @BeforeEach
    fun setUp() {
        evaluationTargetService = EvaluationTargetService(
            evaluationRepository,
            evaluationTargetRepository,
            evaluationItemRepository,
            applicationFormRepository,
            applicantRepository,
            cheaterRepository
        )
    }

    @Test
    fun `이전 평가가 없고 저장 된 평가 대상자가 없을 경우 저장하고 불러온다`() {
        // given
        val firstEvaluation = createEvaluation()

        val applicationForms = listOf(
            createApplicationForm(id = 1L, recruitmentId = 1L, applicantId = 1L),
            createApplicationForm(id = 2L, recruitmentId = 1L, applicantId = 2L),
            createApplicationForm(id = 3L, recruitmentId = 1L, applicantId = 3L)
        )

        val firstApplicant = getApplicant(1L)
        val secondApplicant = getApplicant(2L)
        val thirdApplicant = getApplicant(3L)

        val newApplicants = listOf(firstApplicant, secondApplicant, thirdApplicant)

        val additionalApplicants = listOf(firstApplicant, secondApplicant)

        // when
        every { evaluationRepository.findByIdOrNull(any()) } returns firstEvaluation
        every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
        every { applicantRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(getApplicant(3L))
        every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns applicationForms
        every { applicantRepository.findAllById(listOf(1L, 2L, 3L)) } returns newApplicants
        every { applicantRepository.findAllById(setOf(1L, 2L)) } returns additionalApplicants

        evaluationTargetService.load(firstEvaluation.id)

        val actual = evaluationTargetRepository.findAllByEvaluationId(firstEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(2) },
            { assertThat(actual[0].applicantId).isEqualTo(1L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(WAITING) },
            { assertThat(actual[1].applicantId).isEqualTo(2L) },
            { assertThat(actual[1].evaluationStatus).isEqualTo(WAITING) }
        )
    }

    @Test
    fun `이전 평가가 있고 저장 된 평가 대상자가 없을 경우 저장하고 불러온다`() {
        // given
        val savedEvaluationTargets = listOf(
            createEvaluationTarget(1L, 1L, WAITING),
            createEvaluationTarget(1L, 2L, PASS),
            createEvaluationTarget(1L, 3L, PASS)
        )

        evaluationTargetRepository.saveAll(savedEvaluationTargets)

        // when
        val secondEvaluation = createEvaluation(id = 2L, beforeEvaluationId = 1L)

        val addingApplicants = listOf(getApplicant(2L))

        every { evaluationRepository.findByIdOrNull(any()) } returns secondEvaluation
        every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
        every { applicantRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(getApplicant(3L))
        every { applicantRepository.findAllById(any()) } returns addingApplicants

        evaluationTargetService.load(secondEvaluation.id)

        val actual = evaluationTargetRepository.findAllByEvaluationId(secondEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(1) },
            { assertThat(actual[0].applicantId).isEqualTo(2L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(WAITING) }
        )
    }

    @Test
    fun `이전 평가가 없고 저장 된 평가 대상자가 있을 경우 갱신하여 불러온다, 새로 등록된 지원자를 평가 대상자에 추가한다`() {
        // given
        val savedEvaluationTargets = listOf(
            createEvaluationTarget(1L, 1L, FAIL),
            createEvaluationTarget(1L, 2L, PASS),
            createEvaluationTarget(1L, 3L, PASS)
        )

        evaluationTargetRepository.saveAll(savedEvaluationTargets)

        // when
        val firstEvaluation = createEvaluation(id = 1L, beforeEvaluationId = 0L)

        val allApplicationForms = listOf(
            createApplicationForm(id = 1L, recruitmentId = 1L, applicantId = 1L),
            createApplicationForm(id = 2L, recruitmentId = 1L, applicantId = 2L),
            createApplicationForm(id = 3L, recruitmentId = 1L, applicantId = 3L),
            createApplicationForm(id = 4L, recruitmentId = 1L, applicantId = 4L)
        )

        val addingApplicant = getApplicant(id = 4L)

        val allApplicants = listOf(
            getApplicant(1L),
            getApplicant(2L),
            getApplicant(3L),
            addingApplicant
        )

        every { evaluationRepository.findByIdOrNull(any()) } returns firstEvaluation
        every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
        every { applicantRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(getApplicant(3L))
        every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns allApplicationForms
        every { applicantRepository.findAllById(listOf(1L, 2L, 3L, 4L)) } returns allApplicants
        every { applicantRepository.findAllById(setOf(4L)) } returns listOf(addingApplicant)

        evaluationTargetService.load(firstEvaluation.id)

        val actual = evaluationTargetRepository.findAllByEvaluationId(firstEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(3) },
            { assertThat(actual[0].applicantId).isEqualTo(1L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(FAIL) },
            { assertThat(actual[1].applicantId).isEqualTo(2L) },
            { assertThat(actual[1].evaluationStatus).isEqualTo(PASS) },
            { assertThat(actual[2].applicantId).isEqualTo(4L) },
            { assertThat(actual[2].evaluationStatus).isEqualTo(WAITING) }
        )
    }

    @Test
    fun `이전 평가가 있고 저장 된 평가 대상자가 있을 경우 갱신하고 불러온다, 이전 평가의 평가 대상자의 평가가 FAIL로 바뀌면 제거한다`() {
        // given
        val savedCurrentEvaluationTargets = listOf(
            createEvaluationTarget(2L, 1L, WAITING),
            createEvaluationTarget(2L, 2L, PASS),
            createEvaluationTarget(2L, 3L, PASS)
        )

        evaluationTargetRepository.saveAll(savedCurrentEvaluationTargets)

        val loadingEvaluationTargetsFromBeforeEvaluation = listOf(
            createEvaluationTarget(1L, 1L, WAITING),
            createEvaluationTarget(1L, 2L, PASS),
            createEvaluationTarget(1L, 3L, PASS),
            createEvaluationTarget(1L, 4L, PASS)
        )

        evaluationTargetRepository.saveAll(loadingEvaluationTargetsFromBeforeEvaluation)

        // when
        val secondEvaluation = createEvaluation(id = 2L, beforeEvaluationId = 1L)

        val addingApplicant = getApplicant(4L)

        every { evaluationRepository.findByIdOrNull(any()) } returns secondEvaluation
        every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
        every { applicantRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(getApplicant(3L))
        every { applicantRepository.findAllById(setOf(4L)) } returns listOf(addingApplicant)

        evaluationTargetService.load(secondEvaluation.id)

        val actual = evaluationTargetRepository.findAllByEvaluationId(secondEvaluation.id)

        // then
        assertAll(
            { assertThat(actual).hasSize(2) },
            { assertThat(actual[0].applicantId).isEqualTo(2L) },
            { assertThat(actual[0].evaluationStatus).isEqualTo(PASS) },
            { assertThat(actual[1].applicantId).isEqualTo(4L) },
            { assertThat(actual[1].evaluationStatus).isEqualTo(WAITING) }
        )
    }

    @Test
    fun `현재 평가를 불러올 때, 평가 대상자가 부정행위자로 지정되어 제거될 경우 현재 평가에만 영향이 가는지 확인한다`() {
        // given
        val beforeEvaluationTarget = EvaluationTarget(evaluationId = 1L, applicantId = 1L)
        val currentEvaluationTarget = EvaluationTarget(evaluationId = 2L, applicantId = 1L)
        val nextEvaluationTarget = EvaluationTarget(evaluationId = 3L, applicantId = 1L)
        evaluationTargetRepository.saveAll(
            listOf(
                beforeEvaluationTarget,
                currentEvaluationTarget,
                nextEvaluationTarget
            )
        )

        // when
        val currentEvaluation = createEvaluation(id = 2L, beforeEvaluationId = 1L)

        every { evaluationRepository.findByIdOrNull(2L) } returns currentEvaluation
        every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
        every { applicantRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(getApplicant(3L))
        every { applicantRepository.findAllById(listOf(1L)) } returns listOf(getApplicant(1L))
        every { applicantRepository.findAllById(emptySet()) } returns emptyList()

        evaluationTargetService.load(2L)

        // then
        assertAll(
            { assertThat(evaluationTargetRepository.findAllByEvaluationId(1L)).isNotEmpty() },
            { assertThat(evaluationTargetRepository.findAllByEvaluationId(2L)).isEmpty() },
            { assertThat(evaluationTargetRepository.findAllByEvaluationId(3L)).isNotEmpty() }
        )
    }

    @Test
    fun `평가 대상을 평가(채점)한 적이 없을 때, 채점 정보를 불러온다`() {
        val evaluation = createEvaluation(id = EVALUATION_ID, beforeEvaluationId = 1L)
        val evaluationItem = createEvaluationItem(id = 1L)
        val evaluationTarget =
            evaluationTargetRepository.save(EvaluationTarget(evaluationId = EVALUATION_ID, applicantId = 1L))

        every { evaluationRepository.findByIdOrNull(EVALUATION_ID) } returns evaluation
        every {
            evaluationItemRepository.findByEvaluationIdOrderByPosition(EVALUATION_ID)
        } returns listOf(evaluationItem)

        val result = evaluationTargetService.getGradeEvaluation(evaluationTarget.id)
        assertAll(
            { assertThat(result.title).isEqualTo(evaluation.title) },
            { assertThat(result.description).isEqualTo(evaluation.description) },
            { assertThat(result.evaluationItems).hasSize(1) },
            { assertThat(result.evaluationTarget.evaluationItemScores[0].score).isEqualTo(0) },
            { assertThat(result.evaluationTarget.evaluationStatus).isEqualTo(WAITING) },
            { assertThat(result.evaluationTarget.note).isBlank() }
        )
    }

    @Test
    fun `평가 대상을 평가(채점)한 적이 있을 때, 채점 정보를 불러온다`() {
        val evaluation = createEvaluation(id = EVALUATION_ID, beforeEvaluationId = 1L)
        val evaluationItem = createEvaluationItem(id = EVALUATION_ITEM_ID)
        val answers = EvaluationAnswers(mutableListOf(createEvaluationAnswer()))
        val evaluationTarget =
            evaluationTargetRepository.save(createEvaluationTarget(EVALUATION_ID, 1L, PASS, NOTE, answers))

        every { evaluationRepository.findByIdOrNull(EVALUATION_ID) } returns evaluation
        every {
            evaluationItemRepository.findByEvaluationIdOrderByPosition(EVALUATION_ID)
        } returns listOf(evaluationItem)

        val result = evaluationTargetService.getGradeEvaluation(evaluationTarget.id)
        assertAll(
            { assertThat(result.title).isEqualTo(evaluation.title) },
            { assertThat(result.description).isEqualTo(evaluation.description) },
            { assertThat(result.evaluationItems).hasSize(1) },
            { assertThat(result.evaluationTarget.evaluationItemScores[0].score).isEqualTo(SCORE) },
            { assertThat(result.evaluationTarget.evaluationStatus).isEqualTo(PASS) },
            { assertThat(result.evaluationTarget.note).isEqualTo(NOTE) }
        )
    }

    @Test
    fun `평가 대상 지원자를 평가하면 평가 상태, 특이사항, 평가 항목에 대한 점수들이 변경된다`() {
        val evaluationTarget = evaluationTargetRepository.save(createEvaluationTarget(1L, 2L, WAITING))

        val updatedScore = 5
        val updatedStatus = PASS
        val updatedNote = "특이 사항(수정)"
        val answers = listOf(EvaluationItemScoreData(score = updatedScore, id = 3L))
        val gradeEvaluationRequest = EvaluationTargetData(answers, updatedNote, updatedStatus)

        evaluationTargetService.grade(evaluationTarget.id, gradeEvaluationRequest)

        val updatedEvaluationTarget = evaluationTargetRepository.findByIdOrNull(evaluationTarget.id)!!
        val expectedAnswers = EvaluationAnswers(mutableListOf(EvaluationAnswer(updatedScore, 3L)))
        assertAll(
            { assertThat(updatedEvaluationTarget.evaluationStatus).isEqualTo(updatedStatus) },
            { assertThat(updatedEvaluationTarget.note).isEqualTo(updatedNote) },
            {
                assertThat(updatedEvaluationTarget.evaluationAnswers).usingRecursiveComparison()
                    .isEqualTo(expectedAnswers)
            }
        )
    }

    private fun createApplicationForm(id: Long, recruitmentId: Long = 1L, applicantId: Long): ApplicationForm {
        return ApplicationForm(
            applicantId = applicantId,
            recruitmentId = recruitmentId,
            referenceUrl = "",
            submitted = true,
            createdDateTime = createLocalDateTime(2019, 10, 25, 10),
            modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
            submittedDateTime = createLocalDateTime(2019, 11, 5, 10),
            answers = ApplicationFormAnswers(
                mutableListOf(
                    ApplicationFormAnswer("${id}의 1번 답", 1L),
                    ApplicationFormAnswer("${id}의 2번 답", 2L)
                )
            ),
            id = id
        )
    }

    private fun getApplicant(id: Long): Applicant {
        return createApplicant(
            id = id,
            name = "홍길동$id",
            email = "$id@email.com",
            phoneNumber = "010-0000-0000",
            gender = Gender.MALE,
            birthday = createLocalDate(2020, 4, 17),
            password = Password("password")
        )
    }
}
