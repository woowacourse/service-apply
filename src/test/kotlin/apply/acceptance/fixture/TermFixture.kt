package apply.acceptance.fixture

import io.restassured.RestAssured
import io.restassured.http.ContentType

class Term(val name: String, val id: Long)

class TermBuilder {
    var name: String = "단독 모집"
    var id: Long = 0L

    fun build(): Term {
        val term = Term(name, id)
        postTerm(term)
        return term
    }

    private fun postTerm(term: Term) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(term)
            .`when`()
            .post("/api/terms")
    }
}

fun term(builder: TermBuilder.() -> Unit): Term {
    return TermBuilder().apply { id = 0L }.apply(builder).build()
}

fun term(): Term {
    return TermBuilder().build()
}
