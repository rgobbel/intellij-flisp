package ai.memity.flisp.lang.psi

import ai.memity.flisp.lang.psi.ext.FlElement
import ai.memity.flisp.util.findSymbols
import ai.memity.flisp.util.findTopLevelSymbols
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import org.jetbrains.annotations.NotNull

class FlispReference(o: PsiElement, textRange: TextRange = o.lastChild.textRange) :
    PsiReferenceBase<FlSymbol>(o as @NotNull FlSymbol) {
    override fun resolve(): PsiElement {
        val project: Project = element.project
        val matches = findSymbols(project, this.element.text)
        return matches[0]
    }


}