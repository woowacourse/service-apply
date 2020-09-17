package support.views

import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.data.binder.BeanValidationBinder
import com.vaadin.flow.data.binder.Binder
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

abstract class BindingFormLayout<TARGET : Any>(
    private val targetClass: KClass<TARGET>
) : FormLayout() {
    protected val binder: Binder<TARGET> = BeanValidationBinder(targetClass.java)

    protected fun drawRequired() {
        binder.bindInstanceFields(this)
    }

    abstract fun bindOrNull(): TARGET?

    protected fun bindDefaultOrNull(): TARGET? {
        return targetClass.createInstance()
            .takeIf { binder.writeBeanIfValid(it) }
    }
}
