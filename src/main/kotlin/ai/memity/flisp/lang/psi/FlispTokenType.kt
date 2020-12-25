package ai.memity.flisp.lang.psi

import ai.memity.flisp.lang.FlispLanguage
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.ILeafElementType

open class FlispTokenType(debugName: String) : IElementType(debugName, FlispLanguage), ILeafElementType {
    override fun createLeafNode(leafText: CharSequence) = FlToken(this, leafText)
    override fun toString() = "FlispTokenType." + super.toString()

    companion object {
        @JvmStatic val SCM_DEF_SYMBOLS = "\\s+".toRegex().split("""
			define define-macro
		""".trim()).toSet()
        @JvmStatic val CL_FN_DEF_SYMBOLS = "\\s+".toRegex().split("""
		    defun
		""".trim()).toSet()
        @JvmStatic val CL_DEF_SYMBOLS = "\\s+".toRegex().split("""
		    defun defvar defmacro
		""".trim()).toSet()
        @JvmStatic val FN_DEF_SYMBOLS = "\\s+".toRegex().split("""
			defun define
		""".trim()).toSet()
        @JvmStatic val DEF_ALIKE_SYMBOLS = "\\s+".toRegex().split("""
			defun define define-macro defvar
		""".trim()).toSet()
        @JvmStatic val LET_ALIKE_SYMBOLS = "\\s+".toRegex().split("""
			let let*
		""".trim()).toSet()
        @JvmStatic val MACRO_DEF_SYMBOLS = "\\s+".toRegex().split("""
		    define-macro defmacro
		""".trim()).toSet()
        @JvmStatic val VAR_DEF_SYMBOLS = "\\s+".toRegex().split("""
			define defvar set-top-level-value!
		""".trim()).toSet()
        @JvmStatic val CHAR_NAMES = "\\s+".toRegex().split("""
			nul alarm backspace tab linefeed vtab page return esc delete newline
		""".trim()).toSet()
        @JvmStatic val BUILTINS = "\\s+".toRegex().split("""
            quote cond if and or while lambda macro label
            progn
            eq atom cons list car cdr set-car! set-cdr! read eval print
            set not load symbolp numberp + - * / < div0 = compare
            prog1 apply rplaca rplacd boundp error exit princ
            consp assoc nconc assq memq length raise symbol

            eq? eqv? equal? atom? not null? boolean? symbol? keyword?
            number? bound? pair? builtin? vector? fixnum?
            function? top-level-value set-top-level-value! environment
            constant? integer-valued? integer? fixnum truncate
            
            vector aref aset! time.now time.string time.fromstring path.cwd
            path.exists? os.getenv os.setenv vector.alloc exit
            rand rand.uint32 rand.uint64 rand.double rand.float
            sqrt exp log sin cos tan asin acos atan
		""".trim()).toSet()
        @JvmStatic val SPECIAL_FORMS = "\\s+".toRegex().split("""
            if while let let* raise try catch error
            set! set-top-level-value! quote lambda prog1 progn
        
            Infinity -Infinity +NaN -NaN
        """.trim()).toSet()
    }
}

