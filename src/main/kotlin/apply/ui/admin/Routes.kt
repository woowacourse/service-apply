package apply.ui.admin

import com.vaadin.flow.component.Component

sealed class Route
class ClassRoute(val route: Class<out Component>) : Route()
class StringRoute(val route: String) : Route()
