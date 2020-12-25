package ai.memity.flisp.lang.psi.impl

import ai.memity.flisp.lang.FlispFileType
import ai.memity.flisp.lang.FlispLanguage
import ai.memity.flisp.lang.psi.*
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.CL_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.CL_FN_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.DEF_ALIKE_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.FN_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.LET_ALIKE_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.MACRO_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.SCM_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.ext.*
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.isLambda
import ai.memity.flisp.util.*
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.ResolveState
import com.intellij.psi.impl.source.DummyHolder
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.util.containers.JBIterable
import java.util.ArrayDeque
import java.util.HashMap
import kotlin.reflect.KClass

class FlispFileImpl(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, FlispLanguage), FlispFile {

    override fun getOriginalFile(): FlispFile = super.getOriginalFile() as FlispFile

    override fun getFileType(): FileType = FlispFileType

    override fun toString(): String = "Femtolisp File"

    override fun defs(): JBIterable<FlList> {
        return state.definitions.jbIt()
    }

    private data class State(
        val timeStamp: Long,
        val definitions: List<FlList>,
    )
    @Volatile
    private var myRolesDirty: Boolean = false
    @Volatile
    private var myState: State? = State(-1, emptyList())
    private val state: State
        get() {
            val curTimeStamp = manager.modificationTracker.modificationCount
            val curState = myState
            if (curState != null && curState.timeStamp == curTimeStamp) return curState
            myState = null

            // makes sure half-applied roles due to ProcessCanceledException
            // are cleared on subtreeChanged
            myRolesDirty = true

            val helper = RoleHelper()
            helper.assignRoles(this, curState != null && curState.timeStamp < 0)
//            val definitions = flTraverser().traverse()
//                .filter { (it as? FlElementImpl)?.dataImpl is Def }
//                .filter(FlList::class).toList()
            val defs0 = flTraverser().traverse()
            val defs1 = defs0.filter { (it as? FlElementImpl)?.dataImpl is Def }
            val defs2 = defs1.filter(FlList::class)
            val definitions = defs2.toList()
            val state = State(curTimeStamp, definitions)
            myState = state
            return state
        }

    override fun subtreeChanged() {
        super.subtreeChanged()
        val clearRoles = myRolesDirty || myState != null
//        fileStub = null
        myState = null
        if (clearRoles) {
            clearRoles()
            myRolesDirty = false
        }
    }

    private fun clearRoles() {
        for (e in flTraverser().traverse()) {
            setData(e, null)
            resetFlags(e)
            setFlag((e as? FlCommented)?.form, FLAG_COMMENTED)
        }
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        super.processDeclarations(processor, state, lastParent, place)
        return true
//        val context = context
//        val placeCF = (lastParent ?: place).containingFile.originalFile
//        val placeFile = ((placeCF as? DummyHolder)?.context?.containingFile ?: placeCF) as? FlispFileImpl ?: return true
//        val placeParent = (place as? FlElement)?.parent
//        fun processDef(def: Any, parentDef: Def?, processor: PsiScopeProcessor, state: ResolveState): Boolean {
//            val defdef = (def as? FlElement)?.def
//            if (!processor.execute(
//                    def as? FlElement ?: defService.getDefinition(defOrKey as SymKey))
//            return true
//        }
//        defs().forEach {
//            val def = it.def!!
//            val parentDef = it.parentForm?.def
//            if (!processDef(it, parentDef, processor, state)) return false
//        }
    }

}

private fun setData(o: PsiElement?, data: Any?) {
    if (o is FlElementImpl) o.dataImpl = data
}
private fun setFlag(o: PsiElement?, flag: Int) {
    if (o is FlElementImpl) o.flagsImpl = o.flagsImpl or flag
}

private fun resetFlags(o: PsiElement?) {
    if (o is FlElementImpl) o.flagsImpl = 0
}

private class RoleHelper {

    fun assignRoles(file: FlispFile, firstTime: Boolean) {
        val seenDefs = mutableSetOf<FlSymbol>()
        val delayedDefs = mutableMapOf<FlList, Def>()

        val s = file.flTraverser().expand {
            it !is FlListImpl || (it as FlElementImpl).roleImpl.let { r -> !DEF_ROLES.contains(r) }
        }.traverse()

        if (firstTime) {
            s.onEach(this::initFlags)
        }
        for (e in s) {
            initFlags(e)
            if (e is FlCommented) {
                setFlag(e, FLAG_COMMENTED)
                e.childForms[0]?.let { setFlag(e.childForms[0], FLAG_COMMENTED) }
                continue
            }
            else if (e is FlToken) {
                if (e.elementType == FlispElementTypes.F_RPAR && e.parent is FlList) {
                    val parent = e.parent as FlListImpl
                    val def = delayedDefs.remove(parent) ?: continue
                    setData(parent, def)
                }
            }
            else if (e is FlListImpl && DEF_ROLES.contains(e.role)) {
                ((e as FlElement).def?.name)?.let { seenDefs.add(it) }
            }
//            if (e is FlElement) {
//                // optimization: finishing up the delayed def
//                if (e.elementType == FlispElementTypes.F_LPAR && e.parent is FlList) {
//                    val parent = e.parent as FlList
//                    val def = delayedDefs.remove(parent) ?: continue
//                    setData(parent, def)
//                }
//            }
            // optimization: take other threads work into account
            else if (e is FlList) {
                val first = e.firstForm
                var nameSym: FlSymbol? = null
                var isScmDef = false
                val firstName = (first as? FlSymbol)?.text ?: continue
                if (firstName == "comment") {
                    setFlag(e, FLAG_COMMENTED)
                    continue
                }
                if (CL_DEF_SYMBOLS.contains(firstName)) {
                    nameSym = (first.nextForm as? FlSymbol)?.apply { initFlags(this) }
                } else if (firstName == "define") {
                    isScmDef = true
                    if (first.nextForm is FlList || (e.getNth(2) as? FlList)?.isLambda == true) {
                        nameSym = (first.nextForm.firstForm as? FlSymbol)?.apply { initFlags(this) }
                    } else {
                        nameSym = (first.nextForm as? FlSymbol)?.apply { initFlags(this) }
//                        setData(e, Role.DEF)
                    }
                }
                if (nameSym != null) {
//                    if (!nameSym.fastFlagIsSet(FLAG_QUOTED) && !nameSym.fastFlagIsSet(FLAG_UNQUOTED)) {
//                    // optimization: delay up until the end, so that other threads may skip this
//                    setData(nameSym, Role.NAME)
//                    delayedDefs[e] = createDef(e, nameSym)
//                    seenDefs.add(nameSym)
                    if (CL_FN_DEF_SYMBOLS.contains(firstName) || nameSym.role != Role.DEF) {
                        if (!nameSym.fastFlagIsSet(FLAG_QUOTED) && !nameSym.fastFlagIsSet(FLAG_UNQUOTED)) {
                            // optimization: delay up until the end, so that other threads may skip this
//                            setData(e, Role.DEF)
                            setData(nameSym, Role.NAME)
                            nameSym.nextForm(FlList::class)?.let { setData(it, Role.CL_ARG_LIST)}
                            delayedDefs[e] = createDef(e, nameSym)
                            seenDefs.add(nameSym)
                        }
                        else if (firstName == "define") {
                            setData(nameSym, Role.NAME)
                            if (e.childForms[1] is FlListImpl) {
                                val argList = e.childForms[1]
                                setData(argList, Role.SCM_ARG_LIST)
                                delayedDefs[e] = createDef(e, nameSym)
                                seenDefs.add(nameSym)
                            } else if ((e.getNth(2) as? FlList)?.isLambda == true) {
                                val argList = (e.getNth(2) as? FlList)?.getNth(1)
                                setData(argList, Role.SCM_ARG_LIST)
                                delayedDefs[e] = createDef(e, nameSym)
                                seenDefs.add(nameSym)
                            } else {
                                delayedDefs[e] = Def(nameSym, "value")
                                seenDefs.add(nameSym)
//                                setData(e, Role.DEF)
                            }
                        }
                    } else if (firstName == "defvar") {
//                        setData(e, Role.DEF)
                        setData(nameSym, Role.NAME)
                        delayedDefs[e] = createDef(e, nameSym)
                        seenDefs.add(nameSym)
                    } else if (MACRO_DEF_SYMBOLS.contains(firstName)) {
//                        setData(e, Role.DEF)
                        setData(nameSym, Role.NAME)
                        if (firstName == "defmacro") {
                            nameSym.nextForm(FlList::class)?.let { setData(it, Role.CL_ARG_LIST)}
                        } else if (isScmDef) {
                            setData(nameSym.parentForm, Role.SCM_ARG_LIST)
                        }
                    }
                }
                else if (LET_ALIKE_SYMBOLS.contains(firstName)) {
                    setData(e.childForm(FlList::class), Role.LET_BIND_LIST)
                }
//                else if (FN_DEF_SYMBOLS.contains(firstName)) {
//                    processFnDef(e).size()
//                }
            }
        }
    }

    private fun processArglist(e: FlList) {
        when {
            CL_FN_DEF_SYMBOLS.contains(e.car?.text) && e.getNth(2) is FlList -> e.getNth(2).childForms // Common Lisp fn def
            SCM_DEF_SYMBOLS.contains(e.car?.text) && e.getNth(2) is FlList -> (e.getNth(2) as FlList).cdr // Scheme fn or macro def
            e.getNth(1) is FlSymbol && ((e.getNth(2) as? FlList)?.isLambda == true) -> listOf((e.getNth(2) as? FlList)?.getNth(1)).jbIt()
            else -> null
        }?.map { arg -> setData(arg, Role.ARG)}
//        theList?.let {setData(e, Role.CL_ARG_LIST)}
//        theList?.map { arg -> setData(arg, Role.ARG)}
    }

    private fun createDef(e: FlList, nameSym: FlSymbol): Def {
        processArglist(e)
        return Def(nameSym, "function")
    }

    private fun initFlags(e: PsiElement) {
        setFlag((e as? FlCommented)?.form, FLAG_COMMENTED)
        when ((e as? FlReaderMacro)?.firstChild?.elementType) {
            FlispElementTypes.F_QUOTE_CHAR, FlispElementTypes.F_BQUOTE_CHAR -> setFlag(e.parent, FLAG_QUOTED)
            FlispElementTypes.F_COMMA, FlispElementTypes.F_COMMA_AT -> setFlag(e.parent, FLAG_UNQUOTED)
        }
    }
}

