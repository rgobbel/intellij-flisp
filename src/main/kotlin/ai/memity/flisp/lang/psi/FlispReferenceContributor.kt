package ai.memity.flisp.lang.psi

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.ProcessingContext

class FlispSymbolReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(FlSymbol::class.java),
            FlispFileReferenceProvider()
        )
    }
}

private class FlispFileReferenceProvider: PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext) : Array<FileReference> {
        val sym: FlSymbol = element as FlSymbol
        val value: String = sym.value
        val fs = element.containingFile.originalFile.virtualFile.fileSystem
        return FlispSymbolReferenceSet(value, sym, sym.startOffset, fs.isCaseSensitive).allReferences
    }
}

private class FlispSymbolReferenceSet(str: String, element: FlSymbol, startOffset: Int, isCaseSensitive: Boolean) :
    FileReferenceSet(str, element, startOffset, null, isCaseSensitive) {
    override fun getDefaultContexts(): MutableCollection<PsiFileSystemItem> = parentDirectoryContext
    }