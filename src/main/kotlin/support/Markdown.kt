package support

import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

private val flavour: MarkdownFlavourDescriptor = CommonMarkFlavourDescriptor()

fun markdownToHtml(markdownText: String): String {
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdownText)
    return HtmlGenerator(markdownText, parsedTree, flavour).generateHtml()
}
