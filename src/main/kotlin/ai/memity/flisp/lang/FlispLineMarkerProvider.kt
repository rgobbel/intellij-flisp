package ai.memity.flisp.lang

import ai.memity.flisp.ide.colors.FlispColors
import ai.memity.flisp.ide.icons.FlispIcons
import ai.memity.flisp.lang.psi.FlList
import ai.memity.flisp.lang.psi.FlSymbol
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.isClFnDef
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.isFnDef
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.isMacroDef
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.isVarDef
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.parentForm
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.debugger.impl.DebuggerUtilsEx.isLambda
import com.intellij.icons.AllIcons
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class FlispLineMarkerProvider : LineMarkerProvider {
    companion object {
        val fnDefIcon =  FlispIcons.FN_DEF // AllIcons.Nodes.Function
        val macroDefIcon = FlispIcons.MACRO_DEF // AllIcons.Nodes.Method
        val varDefIcon =  FlispIcons.VAR_DEF // AllIcons.Nodes.Variable
//        val VALDEF = Option("flisp.value.defn", "Value definition", AllIcons.Gutter.WriteAccess)
    }

    //    override fun getName() = "Femtolisp line markers"
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        return when (element) {
            is FlSymbol -> {
                element.lastChild.let { leaf ->
                    with (element) {
                        when {
                            isFnDef -> {
                                NavigationGutterIconBuilder.create(fnDefIcon)
                                    .setTooltipText("Function definition")
                                    .setTarget(element)
                                    .createLineMarkerInfo(leaf)
                            }
                            isMacroDef -> {
                                NavigationGutterIconBuilder.create(macroDefIcon)
                                    .setTooltipText("Macro definition")
                                    .setTarget(element)
                                    .createLineMarkerInfo(leaf)
                            }
                            isVarDef -> {
                                NavigationGutterIconBuilder.create(varDefIcon)
                                    .setTooltipText("Variable definition")
                                    .setTarget(element)
                                    .createLineMarkerInfo(leaf)
                            }
                            else -> null
                        }
                    }
                }
            }
            else -> null
        }
    }
}