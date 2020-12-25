package ai.memity.flisp.lang.psi.impl

import ai.memity.flisp.lang.psi.FlList
import ai.memity.flisp.lang.psi.FlSymbol
import ai.memity.flisp.lang.psi.ext.Def
import ai.memity.flisp.lang.psi.ext.FlElement
import ai.memity.flisp.lang.psi.ext.Role
import ai.memity.flisp.util.nextForm
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

open class FlElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), FlElement {
    override val role: Role get() = role(data)
    override val flags: Int get() = role.run { flagsImpl }
    override val def: Def? get() = data as? Def

    @JvmField internal var dataImpl: Any? = null
    @JvmField internal var flagsImpl: Int = 0
    internal val roleImpl: Role get() = role(dataImpl)

    internal val data: Any get() = dataImpl ?: (containingFile as FlispFileImpl).let {
        dataImpl ?: Role.NONE.also { dataImpl = it }
    }
    private fun role(data: Any?): Role = when (data) {
        is Role -> data
        is Def -> Role.DEF
        else -> Role.NONE
    }

}

val DEF_ROLES = setOf(Role.FN_DEF, Role.MACRO_DEF, Role.VAR_DEF, Role.DEF)
val PsiElement?.fastFlags: Int get() = (this as? FlElementImpl)?.flagsImpl ?: 0
val PsiElement?.fastRole: Role get() = (this as? FlElementImpl)?.roleImpl ?: Role.NONE
fun PsiElement?.fastFlagIsSet(flag: Int): Boolean = fastFlags and flag == flag
val FlList?.fastDef: Def?
    get() = (this as? FlListImpl)?.run {
        ((this as FlElementImpl).dataImpl as? Def)
    }