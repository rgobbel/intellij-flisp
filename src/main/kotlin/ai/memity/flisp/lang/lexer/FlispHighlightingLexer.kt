package ai.memity.flisp.lang.lexer

import ai.memity.flisp.lang.FlispLanguage
import ai.memity.flisp.lang.parser.FlispParserDefinition
import ai.memity.flisp.lang.psi.FlispElementTypes.*
import com.intellij.lexer.Lexer
import com.intellij.lexer.LookAheadLexer
import com.intellij.psi.tree.IElementType

class FlispHighlightingLexer : LookAheadLexer(FlispLexer()) {
    companion object {
        val CALLABLE = IElementType("CALLABLE", FlispLanguage)
        val KW_LITERAL = IElementType("KW_LITERAL", FlispLanguage)
        val QUOTED = IElementType("QUOTED", FlispLanguage)
        val BUILTIN = IElementType("BUILTIN", FlispLanguage)
        val COMMENTED = IElementType("COMMENTED", FlispLanguage)
    }

    override fun lookAhead(baseLexer: Lexer) {
        fun skipWs(l: Lexer) {
            while (FlispParserDefinition.WHITESPACES.contains(l.tokenType)
                || FlispParserDefinition.COMMENTS.contains(l.tokenType)
            ) {
                advanceLexer(l)
            }
        }

        val tokenType0 = baseLexer.tokenType
//        println("in lexer: ${tokenType0?.debugName}")
        when (tokenType0) {
            F_SHARP -> {
                baseLexer.advance()
                when (baseLexer.tokenType) {
                    F_STRING, F_LPAR, F_LBRACK -> advanceAs(baseLexer, baseLexer.tokenType)
                    else -> addToken(baseLexer.tokenStart, F_SHARP)
                }
            }
//            F_QUOTE_CHAR -> {
//                advanceAs(baseLexer, tokenType0)
//                skipWs(baseLexer)
//                advanceAs(baseLexer, QUOTED_SYM)
//            }
//            F_BQUOTE_CHAR -> {
//                advanceAs(baseLexer, tokenType0)
//                skipWs(baseLexer)
//                advanceAs(baseLexer, F_BQUOTED)
//            }
////            F_FORM_COMMENT -> advanceAs(baseLexer, F_COMMENTED)
//            F_KW_LITERAL -> {
////                advanceAs(baseLexer, tokenType0)
////                if (baseLexer.tokenType === F_SYM) {
//                advanceAs(baseLexer, KEYWORD)
////                }
//            }
//            F_FORM_COMMENT_START -> {
//                advanceAs(baseLexer, tokenType0)
//                skipWs(baseLexer)
//                advanceAs(baseLexer, F_COMMENTED)
//            }
//            F_LPAR -> {
//                advanceAs(baseLexer, tokenType0)
//                skipWs(baseLexer)
//                val callableType = if (baseLexer.tokenType.let { it == F_COLON }) CALLABLE_KEYWORD else CALLABLE
//                advanceSymbolAs(baseLexer, callableType)
//            }
            else -> {
//                println("off end in lexer: ${tokenType0?.debugName}")
                super.lookAhead(baseLexer)
            }
        }
    }

}