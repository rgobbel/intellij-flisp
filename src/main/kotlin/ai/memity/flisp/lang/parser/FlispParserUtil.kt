package ai.memity.flisp.lang.parser

import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.COMMENTS
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.LITERALS
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.LPAR_ALIKE
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.MACROS
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.PAREN_ALIKE
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.SHARPS
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.WHITESPACES
import ai.memity.flisp.lang.psi.FlForm
import ai.memity.flisp.lang.psi.FlispElementTypes.*
import ai.memity.flisp.lang.psi.ext.FLAG_COMMENTED
import ai.memity.flisp.lang.psi.impl.FlElementImpl
import ai.memity.flisp.util.wsOrComment
import com.intellij.lang.PsiBuilder
import com.intellij.lang.WhitespacesBinders
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import kotlin.reflect.KClass


class FlispParserUtil : GeneratedParserUtilBase() {
    companion object {
        @JvmStatic
        val namesToChars = mapOf(
            "nul" to '\u0000',
            "alarm" to '\u0007',
            "backspace" to '\b',
            "tab" to '\t',
//            "linefeed" to '\n',
            "vtab" to '\u000b',
            "page" to '\u000c',
            "return" to '\r',
            "esc" to '\u001b',
            "delete" to '\u007f',
            "newline" to '\n',
        )
        @JvmStatic
        fun parseTree(b: PsiBuilder, l: Int, p: Parser) =
            parseAsTree(ErrorState.get(b), b, l,
                DUMMY_BLOCK, false, p, TRUE_CONDITION)

        @JvmStatic
        fun nospace(b: PsiBuilder, l: Int): Boolean {
            if (space(b, l)) {
                b.mark().apply { b.tokenType; error("No <whitespace> allowed") }
                    .setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.GREEDY_RIGHT_BINDER)
            }
            return true
        }

        @JvmStatic
        fun space(b: PsiBuilder, l: Int): Boolean {
            return b.rawLookup(0).wsOrComment() || b.rawLookup(-1).wsOrComment()
        }
        @JvmStatic
        fun nameToChar(name: String): Char? = namesToChars[name]

        private val RECOVER_SET = TokenSet.orSet(
            SHARPS, MACROS, PAREN_ALIKE, LITERALS,
            TokenSet.create(F_DOT, F_SYM, F_QUOTE_CHAR))

        @JvmStatic
        fun formRecover(b: PsiBuilder, l: Int): Boolean {
            return !RECOVER_SET.contains(b.tokenType)
        }

        @JvmStatic
        fun rootFormRecover(b: PsiBuilder, l: Int): Boolean {
            val type = b.tokenType
            return LPAR_ALIKE.contains(type) || !RECOVER_SET.contains(type)
        }
    }
}

