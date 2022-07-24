package apply.acceptance.fixture

class RecruitmentItem(
    val title: String,
    val position: Int,
    val maximumLength: Int,
    val description: String
)

class RecruitmentItemBuilder {
    var title: String = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
    var position: Int = 1
    var maximumLength = 1000
    var description: String = "우아한테크코스는..."

    fun build(): RecruitmentItem {
        return RecruitmentItem(title, position, maximumLength, description)
    }
}

class RecruitmentItemsBuilder {
    private var recruitmentItems = mutableListOf<RecruitmentItem>()

    fun recruitmentItem(builder: RecruitmentItemBuilder.() -> Unit) {
        recruitmentItems.add(RecruitmentItemBuilder().apply(builder).build())
    }

    fun recruitmentItem() {
        recruitmentItems.add(RecruitmentItemBuilder().build())
    }

    fun build(): List<RecruitmentItem> {
        return recruitmentItems
    }
}

fun recruitmentItems(builder: RecruitmentItemsBuilder.() -> Unit): List<RecruitmentItem> {
    return RecruitmentItemsBuilder().apply(builder).build()
}
