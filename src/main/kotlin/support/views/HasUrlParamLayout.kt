package support.views

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HasUrlParameter

abstract class HasUrlParamLayout<T> : VerticalLayout(), HasUrlParameter<T>
