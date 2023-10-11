package apply.config

import apply.domain.administrator.Administrator
import apply.domain.administrator.AdministratorRepository
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormAnswer
import apply.domain.applicationform.ApplicationFormAnswers
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationitem.EvaluationItem
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.judgmentitem.JudgmentItem
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.judgmentitem.ProgrammingLanguage
import apply.domain.mail.MailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.SubmissionMethod
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import apply.domain.user.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import support.createLocalDate
import support.createLocalDateTime

@Profile("local")
@Transactional
@Component
class DatabaseInitializer(
    private val administratorRepository: AdministratorRepository,
    private val termRepository: TermRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val evaluationRepository: EvaluationRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val userRepository: UserRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val mailHistoryRepository: MailHistoryRepository,
    private val database: Database
) : CommandLineRunner {
    override fun run(vararg args: String) {
        if (shouldSkip()) return
        cleanUp()
        populate()
    }

    private fun shouldSkip(): Boolean {
        return recruitmentRepository.count() != 0L
    }

    private fun cleanUp() {
        database.clear(database.retrieveTables())
    }

    private fun populate() {
        populateAdministrator()
        populateTerms()
        populateRecruitments()
        populateRecruitmentItems()
        populateEvaluations()
        populateEvaluationItems()
        populateUsers()
        populateApplicationForms()
        populateEvaluationTargets()
        populateMissions()
        populateJudgmentItems()
        populateAssignments()
        populateMailHistories()
    }

    private fun populateAdministrator() {
        val administrator = Administrator(
            id = 1L,
            name = "admin",
            username = "admin",
            password = "{noop}1234"
        )
        administratorRepository.save(administrator)
    }

    private fun populateTerms() {
        val terms = listOf(
            Term("1기"),
            Term("2기"),
            Term("3기"),
            Term("4기")
        )
        termRepository.saveAll(terms)
    }

    private fun populateRecruitments() {
        val recruitments = listOf(
            Recruitment(
                title = "지원할 제목",
                startDateTime = createLocalDateTime(2019, 10, 5, 10),
                endDateTime = createLocalDateTime(2120, 11, 5, 10),
                recruitable = true,
                hidden = false
            ),
            Recruitment(
                title = "웹 백엔드 2기",
                startDateTime = createLocalDateTime(2019, 10, 25, 10),
                endDateTime = createLocalDateTime(2019, 11, 5, 10),
                recruitable = true,
                hidden = false
            ),
            Recruitment(
                title = "웹 백엔드 3기",
                startDateTime = createLocalDateTime(2020, 10, 25, 15),
                endDateTime = createLocalDateTime(2020, 11, 5, 10),
                recruitable = true,
                hidden = true
            ),
            Recruitment(
                title = "웹 프론트엔드 3기",
                startDateTime = createLocalDateTime(2020, 10, 25, 15),
                endDateTime = createLocalDateTime(2020, 11, 5, 10),
                recruitable = false,
                hidden = false
            )
        )
        recruitmentRepository.saveAll(recruitments)
    }

    private fun populateRecruitmentItems() {
        val recruitmentItems = listOf(
            RecruitmentItem(
                title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은",
                description = "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요.",
                recruitmentId = 1L,
                maximumLength = 1000,
                position = 1
            ),
            RecruitmentItem(
                title = "프로그래머가 되려는 이유는 무엇인가요?",
                description = "어떤 계기로 프로그래머라는 직업을 꿈꾸게 되었나요? 프로그래밍을 배워 최종적으로 하고 싶은 일이 무엇인지, 프로그래밍을 통해 만들고 싶은 소프트웨어가 있다면 무엇인지에 대해 작성해 주세요.",
                recruitmentId = 1L,
                maximumLength = 1000,
                position = 2
            )
        )
        recruitmentItemRepository.saveAll(recruitmentItems)
    }

    private fun populateEvaluations() {
        val evaluations = listOf(
            Evaluation(
                title = "프리코스 대상자 선발",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                recruitmentId = 1L
            ),
            Evaluation(
                title = "1주차 - 숫자야구게임",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                recruitmentId = 1L,
                beforeEvaluationId = 1L
            ),
            Evaluation(
                title = "2주차 - 자동차경주게임 ",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                recruitmentId = 1L,
                beforeEvaluationId = 2L
            )
        )
        evaluationRepository.saveAll(evaluations)
    }

    private fun populateEvaluationItems() {
        val evaluationItems = listOf(
            EvaluationItem(
                title = "학습과정/역량",
                description = "학습 기간과 전공 유무도 고려한다.",
                evaluationId = 1L,
                maximumScore = 5,
                position = 1
            ),
            EvaluationItem(
                title = "README.md 파일에 기능 목록이 추가되어 있는가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 2L,
                maximumScore = 2,
                position = 1
            ),
            EvaluationItem(
                title = "하드코딩을 상수화 했는가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 2L,
                maximumScore = 2,
                position = 2
            ),
            EvaluationItem(
                title = "인덴트가 2 이하인가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 2L,
                maximumScore = 3,
                position = 3
            ),
            EvaluationItem(
                title = "기능 요구 사항을 만족하는가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 2L,
                maximumScore = 10,
                position = 4
            )
        )
        evaluationItemRepository.saveAll(evaluationItems)
    }

    private fun populateUsers() {
        val users = listOf(
            User(
                name = "홍길동",
                email = "a@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 4, 17),
                password = Password("password")
            ),
            User(
                name = "홍길동2",
                email = "b@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.FEMALE,
                birthday = createLocalDate(2020, 5, 5),
                password = Password("password")
            ),
            User(
                name = "홍길동3",
                email = "c@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1),
                password = Password("password")
            ),
            User(
                name = "홍길동4",
                email = "d@email.com",
                phoneNumber = "010-0000-0000",
                gender = Gender.MALE,
                birthday = createLocalDate(2020, 1, 1),
                password = Password("password")
            )
        )
        userRepository.saveAll(users)
    }

    private fun populateApplicationForms() {
        val applicationForms = listOf(
            ApplicationForm(
                referenceUrl = "",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10, 10, 10),
                recruitmentId = 1L,
                userId = 1L,
                answers = ApplicationFormAnswers(
                    mutableListOf(
                        ApplicationFormAnswer("도전, 끈기", 1L),
                        ApplicationFormAnswer("고객에게 가치를 전달하고 싶습니다.", 2L)
                    )
                )
            ),
            ApplicationForm(
                referenceUrl = "https://www.google.com",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10, 10, 10),
                recruitmentId = 1L,
                userId = 2L,
                answers = ApplicationFormAnswers(
                    mutableListOf(
                        ApplicationFormAnswer("책임감", 1L),
                        ApplicationFormAnswer("스타트업을 하고 싶습니다.", 2L)
                    )
                )
            ),
            ApplicationForm(
                referenceUrl = "https://www.google.com",
                submitted = false,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 6, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 6, 10, 10, 10),
                recruitmentId = 1L,
                userId = 3L,
                answers = ApplicationFormAnswers(
                    mutableListOf(
                        ApplicationFormAnswer("건강", 1L),
                        ApplicationFormAnswer("바딘을 배우고 싶습니다.", 2L)
                    )
                )
            ),
            ApplicationForm(
                referenceUrl = "https://www.google.com",
                submitted = false,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 6, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 6, 10, 10, 10),
                recruitmentId = 1L,
                userId = 4L,
                answers = ApplicationFormAnswers(
                    mutableListOf(
                        ApplicationFormAnswer("사랑", 1L),
                        ApplicationFormAnswer("코딩 교육을 하고 싶습니다.", 2L)
                    )
                )
            )
        )
        applicationFormRepository.saveAll(applicationForms)
    }

    private fun populateEvaluationTargets() {
        val evaluationTargets = listOf(
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 1L,
                userId = 1L,
                evaluationStatus = EvaluationStatus.PASS
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 1L,
                userId = 2L,
                evaluationStatus = EvaluationStatus.PASS
            ),
            EvaluationTarget(
                evaluationId = 2L,
                administratorId = 1L,
                userId = 1L,
                evaluationStatus = EvaluationStatus.WAITING
            ),
            EvaluationTarget(
                evaluationId = 2L,
                administratorId = 1L,
                userId = 2L,
                evaluationStatus = EvaluationStatus.PASS,
                evaluationAnswers = EvaluationAnswers(
                    listOf(
                        EvaluationAnswer(score = 2, evaluationItemId = 2L),
                        EvaluationAnswer(score = 1, evaluationItemId = 3L)
                    )
                )
            ),
            EvaluationTarget(
                evaluationId = 3L,
                userId = 2L
            )
        )
        evaluationTargetRepository.saveAll(evaluationTargets)
    }

    private fun populateMissions() {
        val missions = listOf(
            Mission(
                title = "1주 차 프리코스 - 숫자 야구 게임",
                evaluationId = 2L,
                startDateTime = createLocalDateTime(2020, 11, 24, 15),
                endDateTime = createLocalDateTime(2120, 12, 1, 0),
                description = "https://github.com/woowacourse/java-baseball-precourse",
                submittable = true,
                hidden = false,
                submissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST
            ),
            Mission(
                title = "2주 차 프리코스 - 자동차 경주 게임",
                evaluationId = 3L,
                startDateTime = createLocalDateTime(2020, 12, 1, 15),
                endDateTime = createLocalDateTime(2120, 12, 8, 0),
                description = "https://github.com/woowacourse/java-racingcar-precourse",
                submittable = true,
                hidden = false,
                submissionMethod = SubmissionMethod.PRIVATE_REPOSITORY
            )
        )
        missionRepository.saveAll(missions)
    }

    private fun populateJudgmentItems() {
        val judgmentItems = listOf(
            JudgmentItem(
                missionId = 1L,
                evaluationItemId = 5L,
                testName = "baseball",
                programmingLanguage = ProgrammingLanguage.JAVA
            )
        )
        judgmentItemRepository.saveAll(judgmentItems)
    }

    private fun populateAssignments() {
        val assignments = listOf(
            Assignment(
                userId = 2L,
                missionId = 1L,
                githubUsername = "javajigi",
                pullRequestUrl = "https://github.com/woowacourse/java-baseball-precourse/pull/1",
                note = "안녕하세요. 이번 미션 생각보다 쉽지 않네요."
            )
        )
        assignmentRepository.saveAll(assignments)
    }

    private fun populateMailHistories() {
        val mailHistories = listOf(
            MailHistory(
                subject = "[우아한테크코스] 프리코스를 진행하는 목적과 사전 준비",
                body = "안녕하세요.",
                sender = "woowa_course@woowahan.com",
                recipients = listOf("a@email.com", "b@email.com", "c@email.com", "d@email.com"),
                sentTime = createLocalDateTime(2020, 11, 5, 10)
            )
        )
        mailHistoryRepository.saveAll(mailHistories)
    }
}
