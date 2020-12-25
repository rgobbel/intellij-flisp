package ai.memity.flisp.lang.psi.ext

import ai.memity.flisp.lang.psi.FlForm
import ai.memity.flisp.lang.psi.FlSymbol
import com.intellij.psi.PsiElement

interface FlElement : PsiElement {
    val role: Role
    val flags: Int
    val def: Def?
}

enum class Role {
    NONE, DEF, FN_DEF, VAR_DEF, MACRO_DEF, NAME,
    CL_ARG_LIST, SCM_ARG_LIST, LET_BIND_LIST, BODY,
    ARG, LET_BINDING
}

data class Def(val name: FlSymbol, val type: String)

class FnDef(val args: List<Arg>, val form: FlForm)
class Arg(val name: String)

const val FLAG_COMMENTED = 0x1
const val FLAG_QUOTED = 0x2
const val FLAG_UNQUOTED = 0x4
