package support

import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

private val flavour: MarkdownFlavourDescriptor = CommonMarkFlavourDescriptor()
private val parser: MarkdownParser = MarkdownParser(flavour)

fun markdownToHtml(markdownText: String): String {
    val parsedTree = parser.buildMarkdownTreeFromString(markdownText)
    return HtmlGenerator(markdownText, parsedTree, flavour).generateHtml()
}
