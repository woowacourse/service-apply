package apply.ui.admin

import apply.application.RecruitmentResponse
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.accordion.Accordion
import com.vaadin.flow.component.details.DetailsVariant
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.router.RouterLink
import support.views.createTabs

infix fun String.of(navigationTarget: Class<out Component>): MenuItem {
    return SingleMenuItem(this, navigationTarget)
}

fun String.accordionOf(path: String, recruitments: List<RecruitmentResponse>): MenuItem {
    return AccordionMenuItem(this, path, recruitments.map { it.toContent() })
}

private fun RecruitmentResponse.toContent(): AccordionContent = AccordionContent(id, title)

sealed class MenuItem(protected val title: String) {
    abstract fun toComponent(): Component
}

class SingleMenuItem(
    title: String,
    private val navigationTarget: Class<out Component>
) : MenuItem(title) {
    override fun toComponent(): Component {
        return Tab(RouterLink(title, navigationTarget))
    }
}

data class AccordionContent(val id: Any, val title: String)

class AccordionMenuItem(
    title: String,
    private val path: String,
    private val contents: List<AccordionContent>
) : MenuItem(title) {
    override fun toComponent(): Component {
        return Accordion().apply {
            add(title, createTabs()).addThemeVariants(DetailsVariant.REVERSE)
            close()
        }
    }

    private fun createTabs(): Component? {
        if (contents.isEmpty()) {
            return null
        }
        return createTabs(contents.toTabs(path))
    }

    private fun List<AccordionContent>.toTabs(path: String): List<Component> {
        return map { Tab(Anchor("$path/${it.id}", it.title)) }
    }
}
