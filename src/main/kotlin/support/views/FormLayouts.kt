package support.views

import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ReadOnlyHasValue
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

const val NEW_VALUE: String = "new"
const val EDIT_VALUE: String = "edit"
const val SEND_VALUE: String = "send"
const val DETAIL_VALUE: String = "detail"
val FORM_URL_PATTERN: Regex = Regex("^(\\d*)/?($NEW_VALUE|$EDIT_VALUE|$DETAIL_VALUE|$SEND_VALUE)$")

fun String.toDisplayName(): String {
    return when (this) {
        NEW_VALUE -> "생성"
        EDIT_VALUE -> "수정"
        SEND_VALUE -> "보내기"
        DETAIL_VALUE -> "확인"
        else -> throw IllegalArgumentException()
    }
}

abstract class BindingFormLayout<DATA : Any>(
    private val dataClass: KClass<DATA>
) : FormLayout() {
    private val binder: Binder<DATA> by lazy {
        BeanValidationBinder(dataClass.java).also {
            it.bindInstanceFields(this)
        }
    }

    protected fun drawRequired() {
        run { binder }
    }

    abstract fun bindOrNull(): DATA?

    protected fun bindDefaultOrNull(): DATA? {
        return dataClass.createInstance()
            .takeIf { binder.writeBeanIfValid(it) }
    }

    abstract fun fill(data: DATA)

    protected fun fillDefault(data: DATA) {
        binder.readBean(data)
    }
}

/**
 * If you want to bind a form to [DATA] with an `id` of type [Long], use this [BindingIdentityFormLayout].
 * Note that the field `id` should not have any of [NotNull], [NotEmpty], [Size] annotations.
 *
 * @see BindingFormLayout
 * @see IdField
 */
abstract class BindingIdentityFormLayout<DATA : Any>(
    dataClass: KClass<DATA>
) : BindingFormLayout<DATA>(dataClass) {
    private val id: IdField = IdField()
}

class IdField : ReadOnlyHasValue<Long>({}) {
    init {
        value = 0L
    }
}
