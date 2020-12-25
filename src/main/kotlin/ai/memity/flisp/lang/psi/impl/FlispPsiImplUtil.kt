package ai.memity.flisp.lang.psi.impl

import ai.memity.flisp.lang.parser.FlispParserUtil.Companion.nameToChar
import ai.memity.flisp.lang.psi.*
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.CHAR_NAMES
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.CL_FN_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.MACRO_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.SCM_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.VAR_DEF_SYMBOLS
import ai.memity.flisp.lang.psi.ext.FlElement
import ai.memity.flisp.util.childForms
import ai.memity.flisp.util.jbIt
import ai.memity.flisp.util.nextForm
import com.intellij.psi.PsiElement
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.containers.JBIterable

class FlispPsiImplUtil {
    companion object {
        @JvmStatic fun toString(o: PsiElement) =
            "${StringUtil.getShortName(o::class.java)}{${PsiUtilCore.getElementType(o)}}(${o.text})"
        @JvmStatic fun getFirst(o: FlList): FlForm? = o.childForms.jbIt().get(0)
        @JvmStatic fun getFirst(o: FlPForm): FlForm? = o.childForms.jbIt().get(0)
        @JvmStatic fun getRest(o: FlList): JBIterable<FlForm> = o.childForms.drop(1).jbIt()
        @JvmStatic fun getRest(o: FlPForm): JBIterable<FlForm> = o.childForms.drop(1).jbIt()
        @JvmStatic fun getName(o: FlSymbol): String? = o.text
        @JvmStatic fun getTextOffset(o: FlSymbol): Int = o.lastChild.textRange.startOffset
        @JvmStatic fun getValue(o: FlSymbol): String = o.parent.text
        @JvmStatic val FlElement.isTopLevelForm: Boolean get() = this.parent is FlispFile
        @JvmStatic val PsiElement?.parentForm : FlElement get() = (this as? FlElement)?.parent as FlForm
        @JvmStatic val Any?.notThere: Boolean get() = this == null || ((this is Collection<*>) && this.isEmpty())
        @JvmStatic fun <E: Any, T: Collection<E>> T?.whenExists(func: T.() -> E): E? = if (this.isNullOrEmpty()) null else func()
        @JvmStatic fun <E: Any, T: Collection<E>> T?.whenThere(func: (T) -> E): E? = if (this.isNullOrEmpty()) null else func(this)
        @JvmStatic fun getCar(o: FlList): FlForm? = o.childForms.jbIt().get(0)
        @JvmStatic fun getCar(o: FlPForm): FlForm? = o.childForms.jbIt().get(0)
        @JvmStatic fun getCdr(o: FlList): JBIterable<FlForm> = o.childForms.drop(1).jbIt()
        @JvmStatic fun getNth(o: FlList, n: Int): FlForm? = o.childForms.drop(n).jbIt().get(0)
        @JvmStatic fun getNth(o: FlDotted, n: Int): FlForm? = o.childForms.drop(n).jbIt().get(0)
        @JvmStatic fun getNth(o: FlPForm, n: Int): FlForm? = o.childForms.drop(n).jbIt().get(0)
        @JvmStatic fun getCar(o: FlDotted): FlForm? = o.childForms.toList().jbIt().get(0)
        @JvmStatic fun getCdr(o: FlDotted): JBIterable<FlForm> = o.childForms.drop(1).jbIt()
        @JvmStatic fun getValue(o: FlBoolLiteral): Boolean = o.text == "#t"
        @JvmStatic fun getValue(o: FlKwLiteral) = o.text
        @JvmStatic fun getValue(o: FlStringLiteral) = o.text
//        @JvmStatic fun isDotted(o: FlList): Boolean = o.children.indexOf<FlElement>(F_DOT) == o.children.size - 1
        @JvmStatic val FlSymbol.isClFnDefSym: Boolean get() = with (this) {
            parentForm.parent is FlispFile &&
            parentForm.childForms.indexOf(this) == 1 &&
            CL_FN_DEF_SYMBOLS.contains(parentForm.childForms[0]?.text)
        }
        @JvmStatic val FlSymbol.isSchemeFnDefSym: Boolean get() = with (this) {
            parentForm.parentForm.parent is FlispFile &&
            SCM_DEF_SYMBOLS.contains(parentForm.parentForm.childForms[0]?.text)
        }
        @JvmStatic val FlSymbol.isClFnDef: Boolean get() = with (this) {
            text == "defun" &&
            parentForm is FlList &&
            parentForm.parent is FlispFile}
        @JvmStatic val FlSymbol.isSchemeFnDef: Boolean get() = with (this) {
            text == "define" &&
            parentForm is FlList &&
            parentForm.parent is FlispFile &&
                    (nextForm is FlList || nextForm is FlDotted)
        }
        @JvmStatic val FlSymbol.isMacroDef: Boolean get() = MACRO_DEF_SYMBOLS.contains(this.text) && this.parentForm.isTopLevelForm
        @JvmStatic val FlSymbol.isVarDef: Boolean get() = VAR_DEF_SYMBOLS.contains(this.text) && this.parentForm.isTopLevelForm
        @JvmStatic val FlSymbol.isFnDef: Boolean get() = this.isClFnDef || this.isSchemeFnDef || ((this.nextSibling as? FlList)?.isLambda == true)
        @JvmStatic val FlList.isLambda: Boolean get() = this.car?.text == "lambda"
        @JvmStatic fun getValue(o: FlCharLiteral): Char? {
            val text = o.text
            if (text.length == 3) {
                return text[2]
            } else {
                val rest = text.drop(2)
                if (CHAR_NAMES.contains(rest)) {
                    return nameToChar(rest)
                } else {
                    return rest.toInt().toChar()
                }
            }
        }
        @JvmStatic fun getValue(o: FlNumLiteral): Int {
            // TODO implement this for real
            return 1
        }
        @JvmStatic val PsiElement.isFnDefSym: Boolean get() =
            with (this as FlSymbol) { isClFnDefSym || isSchemeFnDefSym }
    }
}
