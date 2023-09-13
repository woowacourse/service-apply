package support

import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

private val flavour: MarkdownFlavourDescriptor = CommonMarkFlavourDescriptor()
private val parser: MarkdownParser = MarkdownParser(flavour)

fun markdownToHtml(markdownText: String): String {
    val parsedTree = parser.buildMarkdownTreeFromString(markdownText)
    return generateHtml(markdownText, parsedTree)
}

// https://github.com/JetBrains/markdown/issues/72
fun markdownToEmbeddedHtml(markdownText: String): String {
    val parsedTree = parser.parse(IElementType("ROOT"), markdownText)
    return generateHtml(markdownText, parsedTree)
}

private fun generateHtml(markdownText: String, parsedTree: ASTNode): String {
    return HtmlGenerator(markdownText, parsedTree, flavour).generateHtml()
}
