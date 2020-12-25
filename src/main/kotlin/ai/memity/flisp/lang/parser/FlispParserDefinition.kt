package ai.memity.flisp.lang.parser

import ai.memity.flisp.lang.FlispLanguage
import ai.memity.flisp.lang.lexer.FlispLexer
import ai.memity.flisp.lang.psi.FlispElementTypes.*
import ai.memity.flisp.lang.psi.FlispFile
import ai.memity.flisp.lang.psi.impl.FlispFileImpl
import com.intellij.lang.ASTNode
import com.intellij.lang.BracePair
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class FlispParserDefinition : ParserDefinition {

    override fun createParser(project: Project?) = FlispParser()

    override fun createLexer(project: Project?) = FlispLexer()

    override fun createFile(viewProvider: FileViewProvider) = FlispFileImpl(viewProvider)

    override fun getFileNodeType() = FILE

    override fun getCommentTokens() = COMMENTS

    override fun getStringLiteralElements() = STRINGS

    override fun createElement(node: ASTNode?): PsiElement =
        Factory.createElement(node)

    companion object {
        @JvmField val WHITESPACES = TokenSet.create(TokenType.WHITE_SPACE)
        @JvmField val FILE = IFileElementType(FlispLanguage)
        @JvmField val COMMENTS = TokenSet.create(
            F_LINE_COMMENT,
            F_BLOCK_COMMENT)
        @JvmField val STRINGS = TokenSet.create(F_STRING, F_QUOTED)
        @JvmField val BRACE_PAIRS = listOf(
            BracePair(F_LPAR, F_RPAR, true),
            BracePair(F_LBRACK, F_RBRACK, true),
            BracePair(F_LBRACE, F_RBRACE, false)
        )
        @JvmField val LPAR_ALIKE = TokenSet.create(F_LPAR, F_LBRACK)
        @JvmField val RPAR_ALIKE = TokenSet.create(F_RPAR, F_RBRACK)
        @JvmField val PAREN_ALIKE = TokenSet.orSet(LPAR_ALIKE, RPAR_ALIKE)
        @JvmField val MACROS = TokenSet.create(F_ATSIGN, F_BQUOTE_CHAR, F_COMMA_AT, F_COMMA,
            F_COMMA_BACK_AT, F_COMMA_DOT)
        @JvmField val SHARPS = TokenSet.create(F_SHARP, F_SHARP_BACK_AT, F_SHARP_BACK_AT)
        @JvmField val LITERALS = TokenSet.create(F_BOOL, F_NUM, F_STRING, F_CHAR)
    }
}