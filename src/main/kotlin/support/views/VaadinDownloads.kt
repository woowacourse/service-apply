package support.views

import com.vaadin.flow.component.UI
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.server.VaadinSession
import java.io.ByteArrayInputStream

fun downloadFile(name: String, content: ByteArrayInputStream) {
    val file = { content }
    val registration = VaadinSession.getCurrent()
        .resourceRegistry
        .registerResource(StreamResource(name, file))
    UI.getCurrent().page.open(registration.resourceUri.toString())
}
