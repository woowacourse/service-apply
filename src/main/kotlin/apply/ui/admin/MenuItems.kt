package apply.ui.admin

import apply.application.RecruitmentResponse
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.router.RouterLink
import support.views.HasUrlParamLayout
import support.views.createBorder
import support.views.createHiddenTab
import support.views.createRouteLink

infix fun String.of(navigationTarget: Class<out Component>): MenuItem {
    return SingleMenuItem(this, navigationTarget)
}

infix fun String.of(navigationTarget: Class<out HasUrlParamLayout<Long>>): ParamMenuItem {
    return ParamMenuItem(this, navigationTarget)
}

fun String.createComboBoxTab(recruitments: List<RecruitmentResponse>, innerItems: List<ParamMenuItem>): MenuItem {
    return ComboBoxMenuItem(this, recruitments.reversed(), innerItems)
}

sealed class MenuItem(protected val title: String = "") {
    abstract fun toComponents(): List<Component>
}

fun createBorderItem(): MenuItem {
    return BorderMenuItem()
}

class BorderMenuItem : MenuItem() {
    override fun toComponents(): List<Component> {
        return listOf(
            Tab(createBorder()).apply {
                isEnabled = false
            }
        )
    }
}

class SingleMenuItem(
    title: String,
    private val navigationTarget: Class<out Component>
) : MenuItem(title) {
    override fun toComponents(): List<Component> {
        return listOf(
            Tab(
                createRouteLink(title).apply {
                    setRoute(navigationTarget)
                }
            )
        )
    }
}

class ParamMenuItem(
    title: String,
    val navigationTarget: Class<out HasUrlParamLayout<Long>>
) : MenuItem(title) {
    val link: RouterLink = createRouteLink(title)
    val tab: Tab = createHiddenTab(link)
    override fun toComponents(): List<Component> {
        return listOf(tab)
    }
}

class ComboBoxMenuItem(
    title: String,
    private val contents: List<RecruitmentResponse>,
    private val innerItems: List<ParamMenuItem>
) : MenuItem(title) {
    override fun toComponents(): List<Component> {
        val comboBox = ComboBox<RecruitmentResponse>("모집을 선택해 선발을 진행하세요.").apply {
            placeholder = title
            setItems(contents)
            setItemLabelGenerator { it.title }

            addValueChangeListener {
                for (innerItem in innerItems) {
                    routeEvent(innerItem)
                }
            }
        }

        return mutableListOf<Component>(Tab(comboBox)).apply {
            addAll(innerItems.flatMap { it.toComponents() })
        }
    }

    private fun ComboBox<RecruitmentResponse>.routeEvent(paramMenuItem: ParamMenuItem) {
        with(paramMenuItem) {
            tab.isVisible = true
            tab.isSelected = false
            link.setRoute(navigationTarget, value.id)
        }
    }
}
