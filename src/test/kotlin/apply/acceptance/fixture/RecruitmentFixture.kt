package apply.acceptance.fixture

import apply.application.RecruitmentData
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef
import java.time.LocalDateTime

data class Recruitment(
    var title: String,
    var term: Term,
    var startDateTime: LocalDateTime,
    var endDateTime: LocalDateTime,
    var recruitable: Boolean,
    var hidden: Boolean,
    var recruitmentItems: List<RecruitmentItem>,
    var id: Long
) {
    constructor(recruitmentData: RecruitmentData) : this(
        recruitmentData.title,
        Term(recruitmentData.term),
        recruitmentData.startDateTime,
        recruitmentData.endDateTime,
        recruitmentData.recruitable,
        recruitmentData.hidden,
        recruitmentData.recruitmentItems
            .map(::RecruitmentItem),
        recruitmentData.id
    )
}

class RecruitmentBuilder {
    var title: String = "웹 백엔드 3기"
    var termId: Long = 0L
    lateinit var term: Term
    var startDateTime: LocalDateTime = LocalDateTime.now().minusYears(1)
    var endDateTime: LocalDateTime = LocalDateTime.now().plusYears(1)
    var recruitable: Boolean = false
    var hidden: Boolean = true
    var recruitmentItems: List<RecruitmentItem> = recruitmentItems {
        recruitmentItem()
        recruitmentItem {
            title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
            position = 2
            maximumLength = 1500
            description = "우아한테크코스는..."
        }
        recruitmentItem {
            title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
            position = 3
            maximumLength = 2000
            description = "우아한테크코스는..."
        }
    }
    var id: Long = 0L

    fun build(): Recruitment {
        if (!::term.isInitialized) {
            term = getTermById()
        }

        val recruitment = Recruitment(
            title,
            term,
            startDateTime,
            endDateTime,
            recruitable,
            hidden,
            recruitmentItems,
            id
        )

        return postRecruitment(recruitment)
    }

    private fun postRecruitment(recruitment: Recruitment): Recruitment {
        val savedRecruitmentData = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(recruitment)
            .`when`()
            .post("/api/recruitments")
            .`as`(object : TypeRef<ApiResponse<RecruitmentData>>() {})
            .body as RecruitmentData
        return Recruitment(savedRecruitmentData)
    }

    private fun getTermById(): Term {
        val term = RestAssured.given()
            .get("/api/terms/$termId")
            .then()
            .extract()
            .`as`(object : TypeRef<ApiResponse<apply.domain.term.Term>>() {})
            .body as apply.domain.term.Term
        return Term(term.name, term.id)
    }
}

fun recruitment(builder: RecruitmentBuilder.() -> Unit): Recruitment {
    return RecruitmentBuilder().apply(builder).build()
}

fun recruitment(): Recruitment {
    return RecruitmentBuilder().build()
}
