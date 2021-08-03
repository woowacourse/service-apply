package apply

import apply.application.RecruitmentData
import apply.application.RecruitmentItemData
import apply.domain.recruitment.Recruitment
import apply.domain.recruitmentitem.RecruitmentItem
import apply.ui.admin.recruitment.RecruitmentForm
import apply.ui.admin.recruitment.RecruitmentItemForm
import java.time.LocalDateTime

private const val RECRUITMENT_TITLE: String = "웹 백엔드 3기"
private val START_DATE_TIME: LocalDateTime = LocalDateTime.now().minusYears(1)
private val END_DATE_TIME: LocalDateTime = LocalDateTime.now().plusYears(1)

private const val RECRUITMENT_ITEM_TITLE: String = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
private const val POSITION: Int = 1
const val MAXIMUM_LENGTH: Int = 1000
private const val DESCRIPTION: String =
    "우아한테크코스는 프로그래밍에 대한 기본 지식과 경험을 가진 교육생을 선발하기 때문에 프로그래밍 경험이 있는 상태에서 지원하게 됩니다. 프로그래밍 학습을 어떤 계기로 시작했으며, 어떻게 학습해왔는지, 이를 통해 현재 어느 정도의 역량을 보유한 상태인지를 구체적으로 작성해 주세요."

fun createRecruitment(
    title: String = RECRUITMENT_TITLE,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    term: Long? = null,
    recruitable: Boolean = true,
    hidden: Boolean = true,
    id: Long = 0L
): Recruitment {
    return Recruitment(title, startDateTime, endDateTime, term, recruitable, hidden, id)
}

fun createRecruitmentItem(
    recruitmentId: Long = 1L,
    title: String = RECRUITMENT_ITEM_TITLE,
    position: Int = POSITION,
    maximumLength: Int = MAXIMUM_LENGTH,
    description: String = DESCRIPTION,
    id: Long = 0L
): RecruitmentItem {
    return RecruitmentItem(recruitmentId, title, position, maximumLength, description, id)
}

fun createRecruitmentData(
    title: String = RECRUITMENT_TITLE,
    term: Long? = null,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    recruitable: Boolean = false,
    hidden: Boolean = true,
    recruitmentItems: List<RecruitmentItemData> = emptyList(),
    id: Long = 0L
): RecruitmentData {
    return RecruitmentData(title, term, startDateTime, endDateTime, recruitable, hidden, recruitmentItems, id)
}

fun createRecruitmentItemData(
    title: String = RECRUITMENT_ITEM_TITLE,
    position: Int = POSITION,
    maximumLength: Int = MAXIMUM_LENGTH,
    description: String = DESCRIPTION,
    id: Long = 0L
): RecruitmentItemData {
    return RecruitmentItemData(title, position, maximumLength, description, id)
}

fun createRecruitmentForm(
    title: String = RECRUITMENT_TITLE,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    recruitable: Boolean = false,
    hidden: Boolean = true
): RecruitmentForm {
    return RecruitmentForm(title, startDateTime, endDateTime, recruitable, hidden)
}

fun createRecruitmentItemForm(
    title: String = RECRUITMENT_ITEM_TITLE,
    position: Int = POSITION,
    maximumLength: Int = MAXIMUM_LENGTH,
    description: String = DESCRIPTION
): RecruitmentItemForm {
    return RecruitmentItemForm(title, position, maximumLength, description)
}
