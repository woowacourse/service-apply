package apply.utils

import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

object MarkdownProcessor {
    private val flavour = CommonMarkFlavourDescriptor()

    fun generateToHtml(source: String): String {
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(source)
        return HtmlGenerator(source, parsedTree, flavour).generateHtml()
    }
}
