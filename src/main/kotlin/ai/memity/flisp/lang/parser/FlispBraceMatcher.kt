package ai.memity.flisp.lang.parser

import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.BRACE_PAIRS
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.COMMENTS
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.WHITESPACES
import ai.memity.flisp.lang.psi.FlispElementTypes.F_RBRACK
import ai.memity.flisp.lang.psi.FlispElementTypes.F_RPAR
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class FlispBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = BRACE_PAIRS.toTypedArray()
    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int) = openingBraceOffset
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, tokenType: IElementType?) = tokenType === null
            || WHITESPACES.contains(tokenType) || COMMENTS.contains(tokenType)
            || tokenType === F_RPAR || tokenType === F_RBRACK
}
