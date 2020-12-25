package ai.memity.flisp.ide

import ai.memity.flisp.ide.colors.FlispColors
import ai.memity.flisp.lang.lexer.FlispHighlightingLexer
import ai.memity.flisp.lang.lexer.FlispHighlightingLexer.Companion.KW_LITERAL
import ai.memity.flisp.lang.psi.FlispElementTypes.*
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType

class FlispHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer() = FlispHighlightingLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(map(tokenType)?.textAttributesKey)

    companion object {
        fun map(tokenType: IElementType): FlispColors? = when (tokenType) {
            TokenType.BAD_CHARACTER -> FlispColors.BAD_CHARACTER
            F_LINE_COMMENT -> FlispColors.LINE_COMMENT
            F_BLOCK_COMMENT, F_COMMENTED -> FlispColors.BLOCK_COMMENT
            F_QUOTED -> FlispColors.STRING
            F_STRING -> FlispColors.STRING
            F_BQUOTED -> FlispColors.QUASIQUOTE
            F_CHAR -> FlispColors.CHARACTER
            F_NUM -> FlispColors.NUMBER
            F_BOOL -> FlispColors.BOOLEAN
            F_KW-> FlispColors.KW_LITERAL
            F_SYM -> FlispColors.SYMBOL
            F_COMMA -> FlispColors.COMMA
            F_QUOTE_CHAR -> FlispColors.QUOTE
            F_COMMA, F_COMMA_AT -> FlispColors.UNQUOTE
            F_ATSIGN -> FlispColors.DEREF
            F_READER_MACRO -> FlispColors.READER_MACRO
            F_LPAR, F_RPAR -> FlispColors.PARENS
            F_LBRACK, F_RBRACK -> FlispColors.BRACKETS
//			F_MACRO_DEF, F_FN_DEF -> FlispColor.DEFINITION
            FlispHighlightingLexer.CALLABLE -> FlispColors.CALLABLE
            FlispHighlightingLexer.BUILTIN -> FlispColors.BUILTIN
            KW_LITERAL -> FlispColors.KW_LITERAL
            FlispHighlightingLexer.QUOTED -> FlispColors.QUOTED
            else -> null
        }
    }

}
