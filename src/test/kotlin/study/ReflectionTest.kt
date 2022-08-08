package study

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.staticFunctions

class Person(var firstName: String, val lastName: String, private var age: Int) {
    fun greeting() {}
    private fun fullName() {}
    private fun Int.isAdult() {}

    companion object {
        fun noname(age: Int): Person = Person("", "", age)
    }
}

class ReflectionTest : StringSpec({
    "변경 가능한 공개 프로퍼티 값 변경" {
        val person = Person("Jason", "Park", 20)
        Person::firstName.set(person, "Jaesung")
        person.firstName shouldBe "Jaesung"
    }

    "읽기 전용 공개 프로퍼티 값 변경" {
        val person = Person("Jason", "Park", 20)
        val lastNameField = Person::class.java.getDeclaredField("lastName")
        lastNameField.apply {
            isAccessible = true
            set(person, "Mraz")
        }
        person.lastName shouldBe "Mraz"
    }

    "클래스 내에서 선언된 프로퍼티" {
        val declaredMemberProperties = Person::class.declaredMemberProperties
        declaredMemberProperties shouldHaveSize 3
    }

    "클래스 내에서 선언된 변경 가능한 프로퍼티" {
        val mutableProperties = Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        mutableProperties shouldHaveSize 2
    }

    "변경 가능한 비공개 프로퍼티 변경" {
        val person = Person("Jason", "Park", 20)
        val firstNameProperty = Person::class.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .first { it.name == "firstName" }
        firstNameProperty.setter.call(person, "Jaesung")
        person.firstName shouldBe "Jaesung"
    }

    "클래스 및 부모 클래스 내에서 선언된 함수" {
        assertSoftly(Person::class) {
            functions shouldHaveSize 6 // fullName, greeting, isAdult, equals, hashCode, toString
            memberFunctions shouldHaveSize 5 // fullName, greeting, equals, hashCode, toString
            memberExtensionFunctions shouldHaveSize 1 // isAdult
        }
    }

    "클래스 내에서 선언된 함수" {
        assertSoftly(Person::class) {
            declaredFunctions shouldHaveSize 3 // fullName, greeting, isAdult
            declaredMemberFunctions shouldHaveSize 2 // greeting, isAdult
            declaredMemberExtensionFunctions shouldHaveSize 1 // isAdult
        }
    }

    "멤버 함수 + 확장 함수 + 클래스 내에서 선언된 정적 함수" {
        assertSoftly(Person::class) {
            functions shouldHaveSize 6 // fullName, greeting, isAdult, equals, hashCode, toString
            declaredFunctions shouldHaveSize 3 // fullName, greeting, isAdult
        }
    }

    "클래스 내에서 선언된 정적 함수" {
        Person::class.staticFunctions shouldHaveSize 0
    }
})
