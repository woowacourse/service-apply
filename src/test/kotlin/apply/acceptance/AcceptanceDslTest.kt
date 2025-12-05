package apply.acceptance

import io.kotest.matchers.collections.shouldNotContainDuplicates
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import support.createLocalDateTime

class AcceptanceDslTest : AcceptanceTest() {
    init {
        Given("기수 생성 예시") {
            When("기수를 생성하면") {
                val term1 = term()
                val term2 = term {
                    name = "5기"
                }

                Then("기수가 생성된다") {
                    term1.id shouldNotBe term2.id
                    term2.name shouldBe "5기"
                }
            }
        }

        Given("모집 생성 예시") {
            When("모집을 생성하면") {
                val recruitment1 = recruitment()
                val recruitment2 = recruitment {
                    termId = 0L
                }
                val recruitment3 = recruitment {
                    title = "웹 백엔드 5기"
                    term = term {
                        name = "5기"
                    }
                    startDateTime = createLocalDateTime(2022, 10, 17)
                    endDateTime = createLocalDateTime(2022, 10, 24)
                    recruitmentItems = recruitmentItems {
                        recruitmentItem {
                            title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
                            position = 1
                        }
                        recruitmentItem {
                            title = "프로그래머가 되려는 이유는 무엇인가요?"
                            position = 2
                        }
                    }
                }

                Then("모집이 생성된다") {
                    listOf(recruitment1.id, recruitment2.id, recruitment3.id).shouldNotContainDuplicates()
                }
            }
        }
    }
}
