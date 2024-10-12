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

fun createBorderItem(): MenuItem {
    return BorderMenuItem()
}

sealed class MenuItem(protected val title: String = "") {
    abstract fun toComponents(): List<Component>
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
        val link = createRouteLink(title).apply {
            setRoute(navigationTarget)
        }
        return listOf(Tab(link))
    }
}

class ParamMenuItem(
    title: String,
    private val navigationTarget: Class<out HasUrlParamLayout<Long>>
) : MenuItem(title) {
    private val link: RouterLink = createRouteLink(title)
    private val tab: Tab = createHiddenTab(link)

    override fun toComponents(): List<Component> {
        return listOf(tab)
    }

    fun show() {
        tab.isVisible = true
        tab.isSelected = false
    }

    fun setRoute(id: Long) {
        link.setRoute(navigationTarget, id)
    }
}

class ComboBoxMenuItem(
    title: String,
    private val contents: List<RecruitmentResponse>,
    private val innerItems: List<ParamMenuItem>
) : MenuItem(title) {
    override fun toComponents(): List<Component> {
        val comboBoxTab = Tab(createComboBox()).apply {
            style.set("justify-content", "center")
        }

        return mutableListOf<Component>().apply {
            add(comboBoxTab)
            addAll(innerItems.flatMap { it.toComponents() })
        }
    }

    private fun createComboBox(): ComboBox<RecruitmentResponse> {
        return ComboBox<RecruitmentResponse>("모집을 선택해 선발을 진행하세요.").apply {
            placeholder = title
            setItems(contents)
            setItemLabelGenerator { it.title }

            addValueChangeListener {
                innerItems.forEach {
                    it.show()
                    it.setRoute(value.id)
                }
            }
        }
    }
}
