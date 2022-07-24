package apply.acceptance.fixture

import apply.application.TermData
import apply.ui.api.ApiResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.mapper.TypeRef

class Term(var name: String, var id: Long) {
    constructor(termData: TermData) : this(termData.name, termData.id)
}

class TermBuilder {
    var name: String = "단독 모집"
    var id: Long = 0L

    fun build(): Term {
        val term = Term(name, id)
        return postTerm(term)
    }

    private fun postTerm(term: Term): Term {
        val savedTermData = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(term)
            .`when`()
            .post("/api/terms")
            .`as`(object : TypeRef<ApiResponse<TermData>>() {})
            .body as TermData
        return Term(savedTermData)
    }
}

fun term(builder: TermBuilder.() -> Unit): Term {
    return TermBuilder().apply(builder).build()
}

fun term(): Term {
    return TermBuilder().build()
}
