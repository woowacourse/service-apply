package support.views

import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ReadOnlyHasValue
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

const val NEW_VALUE: String = "new"
const val EDIT_VALUE: String = "edit"
val FORM_URL_PATTERN: Regex = Regex("^(\\d*)/?($NEW_VALUE|$EDIT_VALUE)$")

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
