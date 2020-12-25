package ai.memity.flisp.lang.psi

import ai.memity.flisp.lang.FlispFileType
import ai.memity.flisp.lang.FlispLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.util.containers.JBIterable

interface FlispFile : PsiFile {

//    override fun getReference(): FlispReference? = null

    override fun getOriginalFile(): FlispFile

    override fun getFileType(): FileType

    override fun toString(): String

    fun defs(): JBIterable<FlList>

}

class FlToken(tokenType: FlispTokenType, text: CharSequence) : LeafPsiElement(tokenType, text)
