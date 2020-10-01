package support.views

import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ReadOnlyHasValue
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

abstract class BindingFormLayout<TARGET : Any>(
    private val targetClass: KClass<TARGET>
) : FormLayout() {
    private val binder: Binder<TARGET> by lazy {
        BeanValidationBinder(targetClass.java).also {
            it.bindInstanceFields(this)
        }
    }

    protected fun drawRequired() {
        run { binder }
    }

    abstract fun bindOrNull(): TARGET?

    protected fun bindDefaultOrNull(): TARGET? {
        return targetClass.createInstance()
            .takeIf { binder.writeBeanIfValid(it) }
    }

    fun fill(target: TARGET) {
        binder.readBean(target)
    }
}

abstract class BindingIdentityFormLayout<TARGET : Any>(
    targetClass: KClass<TARGET>
) : BindingFormLayout<TARGET>(targetClass) {
    private val id: IdField = IdField()
}

class IdField : ReadOnlyHasValue<Long>({}) {
    init {
        value = 0L
    }
}
