package apply.application

import apply.EVALUATION_ANSWER_SCORE
import apply.EVALUATION_ID
import apply.EVALUATION_ITEM_ID
import apply.EVALUATION_TARGET_NOTE
import apply.createEvaluation
import apply.createEvaluationAnswer
import apply.createEvaluationItem
import apply.createEvaluationTarget
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
import apply.domain.user.User
import apply.domain.user.UserRepository
import apply.domain.user.findAllByEmailIn
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeBlank
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import support.createLocalDate
import support.createLocalDateTime
import support.test.RepositoryTest

@RepositoryTest
class EvaluationTargetServiceTest(
    private val evaluationTargetRepository: EvaluationTargetRepository
) : DescribeSpec({
    @MockK
    val evaluationRepository: EvaluationRepository = mockk()

    @MockK
    val applicationFormRepository: ApplicationFormRepository = mockk()

    @MockK
    val userRepository: UserRepository = mockk()

    @MockK
    val cheaterRepository: CheaterRepository = mockk()

    @MockK
    val evaluationItemRepository: EvaluationItemRepository = mockk()

    val evaluationTargetService: EvaluationTargetService = EvaluationTargetService(
        evaluationRepository,
        evaluationTargetRepository,
        evaluationItemRepository,
        applicationFormRepository,
        userRepository,
        cheaterRepository
    )

    fun createApplicationForm(id: Long, recruitmentId: Long = 1L, userId: Long): ApplicationForm {
        return ApplicationForm(
            userId = userId,
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

    fun createUser(id: Long): User {
        return User(
            id = id,
            name = "홍길동$id",
            email = "$id@email.com",
            phoneNumber = "010-0000-0000",
            gender = Gender.MALE,
            birthday = createLocalDate(2020, 4, 17),
            password = Password("password")
        )
    }



    describe("EvaluationTargetService") {
        context("이전 평가가 없고 저장 된 평가 대상자가 없을 경우") {
            it("저장하고 불러온다") {
                // given
                val firstEvaluation = createEvaluation()

                val applicationForms = listOf(
                    createApplicationForm(id = 1L, recruitmentId = 1L, userId = 1L),
                    createApplicationForm(id = 2L, recruitmentId = 1L, userId = 2L),
                    createApplicationForm(id = 3L, recruitmentId = 1L, userId = 3L)
                )

                val firstUser = createUser(1L)
                val secondUser = createUser(2L)
                val thirdUser = createUser(3L)

                val newUsers = listOf(firstUser, secondUser, thirdUser)

                val additionalUsers = listOf(firstUser, secondUser)

                // when
                every { evaluationRepository.findByIdOrNull(any()) } returns firstEvaluation
                every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
                every { userRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(createUser(3L))
                every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns applicationForms
                every { userRepository.findAllById(listOf(1L, 2L, 3L)) } returns newUsers
                every { userRepository.findAllById(setOf(3L)) } returns listOf(createUser(3L))
                every { userRepository.findAllById(setOf(1L, 2L)) } returns additionalUsers

                evaluationTargetService.load(firstEvaluation.id)

                val actual = evaluationTargetRepository.findAllByEvaluationId(firstEvaluation.id)

                // then
                assertSoftly(actual) {
                    actual shouldHaveSize 3
                    actual[0].userId shouldBe 1L
                    actual[0].evaluationStatus shouldBe WAITING
                    actual[1].userId shouldBe 2L
                    actual[1].evaluationStatus shouldBe WAITING
                    actual[2].userId shouldBe 3L
                    actual[2].evaluationStatus shouldBe FAIL
                }
            }
        }

        context("이전 평가가 있고 저장 된 평가 대상자가 없을 경우") {
            it("저장하고 불러온다") {
                // given
                val savedEvaluationTargets = listOf(
                    createEvaluationTarget(1L, 1L, WAITING),
                    createEvaluationTarget(1L, 2L, PASS),
                    createEvaluationTarget(1L, 3L, PASS)
                )

                evaluationTargetRepository.saveAll(savedEvaluationTargets)

                // when
                val secondEvaluation = createEvaluation(id = 2L, beforeEvaluationId = 1L)

                every { evaluationRepository.findByIdOrNull(any()) } returns secondEvaluation
                every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
                every { userRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(createUser(3L))
                every { userRepository.findAllById(setOf(3L)) } returns listOf(createUser(3L))
                every { userRepository.findAllById(setOf(2L)) } returns listOf(createUser(2L))

                evaluationTargetService.load(secondEvaluation.id)

                val actual = evaluationTargetRepository.findAllByEvaluationId(secondEvaluation.id)

                // then
                assertSoftly(actual) {
                    actual shouldHaveSize 2
                    actual[0].userId shouldBe 2L
                    actual[0].evaluationStatus shouldBe WAITING
                    actual[1].userId shouldBe 3L
                    actual[1].evaluationStatus shouldBe FAIL
                }
            }
        }

        context("이전 평가가 없고 저장 된 평가 대상자가 있을 경우") {
            it("갱신하여 불러온다, 새로 등록된 지원자를 평가 대상자에 추가한다") {
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
                    createApplicationForm(id = 1L, recruitmentId = 1L, userId = 1L),
                    createApplicationForm(id = 2L, recruitmentId = 1L, userId = 2L),
                    createApplicationForm(id = 3L, recruitmentId = 1L, userId = 3L),
                    createApplicationForm(id = 4L, recruitmentId = 1L, userId = 4L)
                )

                val addingUser = createUser(id = 4L)

                val allUsers = listOf(
                    createUser(1L),
                    createUser(2L),
                    createUser(3L),
                    addingUser
                )

                every { evaluationRepository.findByIdOrNull(any()) } returns firstEvaluation
                every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
                every { userRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(createUser(3L))
                every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns allApplicationForms
                every { userRepository.findAllById(listOf(1L, 2L, 3L, 4L)) } returns allUsers
                every { userRepository.findAllById(setOf(4L)) } returns listOf(addingUser)
                every { userRepository.findAllById(setOf()) } returns listOf()

                evaluationTargetService.load(firstEvaluation.id)

                val actual = evaluationTargetRepository.findAllByEvaluationId(firstEvaluation.id)

                // then
                assertSoftly(actual) {
                    actual shouldHaveSize 4
                    actual[0].userId shouldBe 1L
                    actual[0].evaluationStatus shouldBe FAIL
                    actual[1].userId shouldBe 2L
                    actual[1].evaluationStatus shouldBe PASS
                    actual[2].userId shouldBe 3L
                    actual[2].evaluationStatus shouldBe FAIL
                    actual[3].userId shouldBe 4L
                    actual[3].evaluationStatus shouldBe WAITING
                }
            }
        }

        context("이전 평가가 있고 저장 된 평가 대상자가 있을 경우") {
            it("갱신하고 불러온다, 이전 평가의 평가 대상자의 평가가 FAIL로 바뀌면 제거한다") {
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

                val addingUser = createUser(4L)

                every { evaluationRepository.findByIdOrNull(any()) } returns secondEvaluation
                every { cheaterRepository.findAll() } returns listOf(Cheater("3@email.com"))
                every { userRepository.findAllByEmailIn(listOf("3@email.com")) } returns listOf(createUser(3L))
                every { userRepository.findAllById(setOf(4L)) } returns listOf(addingUser)
                every { userRepository.findAllById(setOf()) } returns listOf()

                evaluationTargetService.load(secondEvaluation.id)

                val actual = evaluationTargetRepository.findAllByEvaluationId(secondEvaluation.id)

                // then
                assertSoftly(actual) {
                    actual shouldHaveSize 3
                    actual[0].userId shouldBe 2L
                    actual[0].evaluationStatus shouldBe PASS
                    actual[1].userId shouldBe 3L
                    actual[1].evaluationStatus shouldBe FAIL
                    actual[2].userId shouldBe 4L
                    actual[2].evaluationStatus shouldBe WAITING
                }
            }
        }

        context("평가자 불러오기 시") {
            it("부정행위자는 불합격 상태로 불러온다") {
                // given
                val savedEvaluationTargets = listOf(
                    createEvaluationTarget(1L, 1L, FAIL),
                    createEvaluationTarget(1L, 2L, PASS),
                    createEvaluationTarget(1L, 3L, PASS),
                    createEvaluationTarget(1L, 4L, WAITING)
                )

                evaluationTargetRepository.saveAll(savedEvaluationTargets)

                // when
                val evaluation = createEvaluation(id = 1L, beforeEvaluationId = 0L)

                val allApplicationForms = listOf(
                    createApplicationForm(id = 1L, recruitmentId = 1L, userId = 1L),
                    createApplicationForm(id = 2L, recruitmentId = 1L, userId = 2L),
                    createApplicationForm(id = 3L, recruitmentId = 1L, userId = 3L),
                    createApplicationForm(id = 4L, recruitmentId = 1L, userId = 4L)
                )

                val cheater = createUser(3L)

                val allUsers = listOf(
                    createUser(1L),
                    createUser(2L),
                    cheater,
                    createUser(4L)
                )

                every { evaluationRepository.findByIdOrNull(any()) } returns evaluation
                every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns allApplicationForms
                every { cheaterRepository.findAll() } returns listOf(Cheater(cheater.email))
                every { userRepository.findAllByEmailIn(listOf(cheater.email)) } returns listOf(cheater)
                every { userRepository.findAllById(listOf(1L, 2L, 3L, 4L)) } returns allUsers
                every { userRepository.findAllById(setOf(3L)) } returns listOf(cheater)
                every { userRepository.findAllById(setOf()) } returns listOf()

                evaluationTargetService.load(evaluation.id)

                val actual = evaluationTargetRepository.findAllByEvaluationId(evaluation.id)
                val cheaterEvaluationTargets = actual.filter { it.userId == cheater.id }

                // then
                assertSoftly {
                    actual shouldHaveSize 4
                    cheaterEvaluationTargets.all { it.evaluationStatus == FAIL } shouldBe true
                }
            }
        }

        context("현재 평가를 불러올 때, 평가 대상자가 부정행위자로 지정되어 탈락 처리되는 경우") {
            it("현재 평가에만 영향이 가는지 확인한다") {
                // given
                val beforeEvaluationTarget = createEvaluationTarget(1L, 1L, PASS)
                val currentEvaluationTarget = createEvaluationTarget(2L, 1L, PASS)
                val nextEvaluationTarget = createEvaluationTarget(3L, 1L, PASS)
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
                every { cheaterRepository.findAll() } returns listOf(Cheater("1@email.com"))
                every { userRepository.findAllByEmailIn(listOf("1@email.com")) } returns listOf(createUser(1L))
                every { userRepository.findAllById(setOf(1L)) } returns listOf(createUser(1L))
                every { userRepository.findAllById(emptySet()) } returns emptyList()

                evaluationTargetService.load(2L)

                // then
                assertSoftly(evaluationTargetRepository) {
                    evaluationTargetRepository.findAllByEvaluationId(1L)[0].evaluationStatus shouldBe PASS
                    evaluationTargetRepository.findAllByEvaluationId(2L)[0].evaluationStatus shouldBe FAIL
                    evaluationTargetRepository.findAllByEvaluationId(3L)[0].evaluationStatus shouldBe PASS
                }
            }
        }

        context("평가 대상을 평가(채점)한 적이 없을 때") {
            it("채점 정보를 불러온다") {
                val evaluation = createEvaluation(id = EVALUATION_ID, beforeEvaluationId = 1L)
                val evaluationItem = createEvaluationItem(id = 1L)
                val evaluationTarget =
                    evaluationTargetRepository.save(EvaluationTarget(evaluationId = EVALUATION_ID, userId = 1L))

                every { evaluationRepository.findByIdOrNull(EVALUATION_ID) } returns evaluation
                every {
                    evaluationItemRepository.findByEvaluationIdOrderByPosition(EVALUATION_ID)
                } returns listOf(evaluationItem)

                val result = evaluationTargetService.getGradeEvaluation(evaluationTarget.id)
                assertSoftly(result) {
                    result.title shouldBe evaluation.title
                    result.description shouldBe evaluation.description
                    result.evaluationItems shouldHaveSize 1
                    result.evaluationTarget.evaluationItemScores[0].score shouldBe 0
                    result.evaluationTarget.evaluationStatus shouldBe WAITING
                    result.evaluationTarget.note.shouldBeBlank()
                }
            }
        }

        context("평가 대상을 평가(채점)한 적이 있을 때") {
            it("채점 정보를 불러온다") {
                val evaluation = createEvaluation(id = EVALUATION_ID, beforeEvaluationId = 1L)
                val evaluationItem = createEvaluationItem(id = EVALUATION_ITEM_ID)
                val answers = EvaluationAnswers(mutableListOf(createEvaluationAnswer()))
                val evaluationTarget = evaluationTargetRepository.save(
                    createEvaluationTarget(EVALUATION_ID, 1L, PASS, EVALUATION_TARGET_NOTE, answers)
                )

                every { evaluationRepository.findByIdOrNull(EVALUATION_ID) } returns evaluation
                every {
                    evaluationItemRepository.findByEvaluationIdOrderByPosition(EVALUATION_ID)
                } returns listOf(evaluationItem)

                val result = evaluationTargetService.getGradeEvaluation(evaluationTarget.id)
                assertSoftly(result) {
                    result.title shouldBe evaluation.title
                    result.description shouldBe evaluation.description
                    result.evaluationItems shouldHaveSize 1
                    result.evaluationTarget.evaluationItemScores[0].score shouldBe EVALUATION_ANSWER_SCORE
                    result.evaluationTarget.evaluationStatus shouldBe PASS
                    result.evaluationTarget.note shouldBe EVALUATION_TARGET_NOTE
                }
            }
        }

        context("평가 대상 지원자를 평가하면") {
            it("평가 상태, 특이사항, 평가 항목에 대한 점수들이 변경된다") {
                val evaluationTarget = evaluationTargetRepository.save(createEvaluationTarget(1L, 2L, WAITING))

                val updatedScore = 5
                val updatedStatus = PASS
                val updatedNote = "특이 사항(수정)"
                val answers = listOf(EvaluationItemScoreData(score = updatedScore, id = 3L))
                val gradeEvaluationRequest = EvaluationTargetData(answers, updatedNote, updatedStatus)

                evaluationTargetService.grade(evaluationTarget.id, gradeEvaluationRequest)

                val updatedEvaluationTarget = evaluationTargetRepository.findByIdOrNull(evaluationTarget.id)!!
                val expectedAnswers = EvaluationAnswers(mutableListOf(EvaluationAnswer(updatedScore, 3L)))
                assertSoftly(updatedEvaluationTarget) {
                    updatedEvaluationTarget.evaluationStatus shouldBe updatedStatus
                    updatedEvaluationTarget.note shouldBe updatedNote
                    updatedEvaluationTarget.evaluationAnswers shouldBeEqualToComparingFields expectedAnswers
                }
            }
        }
    }
})
