package ai.memity.flisp.editor

import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.LPAR_ALIKE
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.RPAR_ALIKE
import ai.memity.flisp.lang.psi.FlForm
import ai.memity.flisp.lang.psi.FlList
import ai.memity.flisp.lang.psi.FlPForm
import ai.memity.flisp.lang.psi.FlSForm
import ai.memity.flisp.lang.psi.FlispElementTypes.F_COMMA
import ai.memity.flisp.lang.psi.ext.FlElement
import ai.memity.flisp.lang.psi.impl.fastDef
import ai.memity.flisp.util.*
import ai.memity.flisp.util.flTraverser
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.CustomFoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.TreeTraversal

val SHORT_TEXT_MAX = 10
val LONG_TEXT_MAX = 30

class FlispFoldingBuilder : CustomFoldingBuilder(), DumbAware {

    override fun getLanguagePlaceholderText(node: ASTNode, range: TextRange): String = getFormPlaceholderText(node.psi as FlElement, LONG_TEXT_MAX)

    override fun buildLanguageFoldRegions(descriptors: MutableList<FoldingDescriptor>, root: PsiElement, document: Document, quick: Boolean) {
        root.flTraverser()
            .traverse()
            .filter(FlPForm::class)
            .filter {
                it.textRange.let { r -> document.getLineNumber(r.endOffset) - document.getLineNumber(r.startOffset) > 1 }
            }
            .transform { FoldingDescriptor(it, it.textRange) }.addAllTo(descriptors)
    }

    override fun isRegionCollapsedByDefault(node: ASTNode) = false
}

fun getFormPlaceholderText(o: FlElement, max: Int = SHORT_TEXT_MAX): String = when (o) {
    is FlList -> o.fastDef?.name?.let { name ->
        "(" + o.childForms[0]!!.text + " " + name.text  + "...)" } ?: dumpElementText(o, max)
    is FlSForm -> o.text
    else -> dumpElementText(o, max)
}

private fun dumpElementText(o: PsiElement, max: Int): String = StringBuilder().run {
    val iterator = o.flTraverser()
        .traverse(TreeTraversal.LEAVES_DFS)
        .typedIterator<TreeTraversal.TracingIt<PsiElement>>()
    var wsOrComment = false
    var prevType: IElementType? = null
    loop@ for (part in iterator) {
        if (part is PsiWhiteSpace || part is PsiComment) {
            wsOrComment = true
            continue@loop
        }
        else {
            val curType = part.elementType
            if (wsOrComment && prevType != F_COMMA &&
                !LPAR_ALIKE.contains(prevType) &&
                !RPAR_ALIKE.contains(curType)) {
                append(" ")
            }
            prevType = curType
            wsOrComment = false
        }
        when {
            length < max -> {
                part.text.let { text ->
                    if (max - length + 1 >= text.length) append(text)
                    else append(text, 0, max - length).append("…")
                }
            }
            else -> {
                var first = true
                iterator.backtrace()
                    .transform { PsiTreeUtil.getDeepestLast(it) }
                    .filter { RPAR_ALIKE.contains(it.elementType) }
                    .unique().forEach {
                        if (first && it !== part) {
                            first = false; if (!endsWith("… ") && !endsWith("…")) append("…")
                        }
                        append(it.text)
                    }
                break@loop
            }
        }
    }
    toString()
}
