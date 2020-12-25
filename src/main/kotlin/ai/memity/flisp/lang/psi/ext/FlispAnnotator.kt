package ai.memity.flisp.lang.psi.ext

import ai.memity.flisp.ide.colors.FlispColors
import ai.memity.flisp.lang.psi.*
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.BUILTINS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.CL_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.SCM_DEF_SYMBOLS
import ai.memity.flisp.util.childForms
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

class FlispAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is FlCommented -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element.textRange)
                    .textAttributes(FlispColors.COMMENTED.textAttributesKey).create()
            }
            is FlQuoted -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element.textRange)
                    .textAttributes(FlispColors.QUOTE.textAttributesKey).create()
            }
            is FlBquoted -> {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element.textRange)
                    .textAttributes(FlispColors.QUASIQUOTE.textAttributesKey).create()
            }
            else -> {
                if (element is FlList) {
                    if (element.parent is FlispFile) {
                        element.car?.let { defForm ->
                            when {
                                CL_DEF_SYMBOLS.contains(defForm.text) ->
                                    defForm.childForms[1]?.let { defSym ->
                                        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                            .range(defSym.textRange)
                                            .textAttributes(FlispColors.DEFINITION.textAttributesKey).create()
                                    }
                                SCM_DEF_SYMBOLS.contains(defForm.text) ->
                                    element.childForms[1]?.let { definee ->
                                        when (definee) {
                                            is FlList, is FlDotted -> definee.let {
                                                it.childForms[0]?.let { defSym ->
                                                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                                        .range(defSym.textRange)
                                                        .textAttributes(FlispColors.DEFINITION.textAttributesKey)
                                                        .create()
                                                }
                                            }
                                            is FlSymbol -> {
                                                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                                    .range(definee.textRange)
                                                    .textAttributes(FlispColors.DEFINITION.textAttributesKey)
                                                    .create()
                                            }
                                            else -> {
                                                println("definee ${definee.text} is ${definee.elementType}")
                                            }
                                        }
                                    }
                                else -> {
//                                    println("element.childForms[0]: ${element.childForms[0]?.text} ${element.childForms[0]}")
                                }
                            }
                        }
                    } else {
                        element.childForms[0]?.let { headForm ->
                            if (BUILTINS.contains(headForm.text)) {
                                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                    .range(headForm.textRange)
                                    .textAttributes(FlispColors.KW_LITERAL.textAttributesKey).create()
                            } else {
                                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                    .range(headForm.textRange)
                                    .textAttributes(FlispColors.CALLABLE.textAttributesKey).create()
                            }
                        }
                    }
                }
            }
        }
    }
}