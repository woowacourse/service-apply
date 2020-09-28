package support.views

import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
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

    fun fillIn(target: TARGET) {
        binder.readBean(target)
    }
}
