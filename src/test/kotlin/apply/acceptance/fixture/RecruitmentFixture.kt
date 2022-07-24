package apply.acceptance.fixture

import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef
import java.time.LocalDateTime

class Recruitment(
    val title: String,
    val term: Term,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val recruitable: Boolean,
    val hidden: Boolean,
    val recruitmentItems: List<RecruitmentItem>
)

class RecruitmentBuilder {
    var title: String = "웹 백엔드 3기"
    var termId: Long = 0L
    lateinit var term: Term
    var startDateTime: LocalDateTime = LocalDateTime.now().minusYears(1)
    var endDateTime: LocalDateTime = LocalDateTime.now().plusYears(1)
    var recruitable = false
    var hidden = true
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
            recruitmentItems
        )

        postRecruitment(recruitment)
        return recruitment
    }

    private fun postRecruitment(recruitment: Recruitment) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(recruitment)
            .`when`()
            .post("/api/recruitments")
    }

    private fun getTermById(): Term {
        return RestAssured.given()
            .get("/api/terms/$termId")
            .then()
            .extract()
            .`as`(object : TypeRef<ApiResponse<Term>>() {})
            .body as Term
    }
}

fun recruitment(builder: RecruitmentBuilder.() -> Unit): Recruitment {
    return RecruitmentBuilder().apply(builder).build()
}

fun recruitment(): Recruitment {
    return RecruitmentBuilder().build()
}
