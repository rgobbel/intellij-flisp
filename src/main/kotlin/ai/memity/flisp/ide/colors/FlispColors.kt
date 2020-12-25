package ai.memity.flisp.ide.colors

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors as Default

enum class FlispColors(humanName: String, default: TextAttributesKey? = null) {
    BLOCK_COMMENT("Comments//Block comment", Default.BLOCK_COMMENT),
    LINE_COMMENT("Comments//Line comment", Default.LINE_COMMENT),
    COMMENTED("Comments//Form comment", Default.TEMPLATE_LANGUAGE_COLOR),
    SYMBOL("Literals//Symbol", Default.IDENTIFIER),
    STRING("Literals//String", Default.STRING),
    CHARACTER("Literals//Character", Default.STRING),
    NUMBER("Literals//Number", Default.NUMBER),
    BOOLEAN("Literals//Boolean", Default.CONSTANT),
    QUOTED("Literals//Quoted form", Default.STRING),
    KW_LITERAL("Literals//Keyword", Default.CONSTANT),
    COMMA("Punctuation//Comma", Default.COMMA),
    DOT("Punctuation//Dot", Default.DOT),
    QUOTE("Punctuation//Quote", Default.STRING),
    QUASIQUOTE("Punctuation//Backquote", Default.TEMPLATE_LANGUAGE_COLOR),
    UNQUOTE("Punctuation//Unquote", Default.PARAMETER),
    DEREF("Punctuation//Dereference", Default.OPERATION_SIGN),
    PARENS("Grouping//Parens", Default.PARENTHESES),
    BRACKETS("Grouping//Brackets", Default.BRACKETS),
    CALLABLE("Entities//Callable (list head)", Default.FUNCTION_CALL),
    DEFINITION("Entities//Definition", Default.FUNCTION_DECLARATION),
    FN_ARGUMENT("Entities//Function or macro argument", Default.PARAMETER),
    LET_BINDING("Entities//Local binding", Default.LOCAL_VARIABLE),
    READER_MACRO("Entities//Reader macro"),
    BAD_CHARACTER("Entities//Bad character", HighlighterColors.BAD_CHARACTER),
    BUILTIN("Entities//Builtin", Default.PREDEFINED_SYMBOL),
    ;

    val textAttributesKey = TextAttributesKey.createTextAttributesKey("${this::class.qualifiedName}.$name", default)
    val attributesDescriptor = AttributesDescriptor(humanName, textAttributesKey)
    val testSeverity: HighlightSeverity = HighlightSeverity(name, HighlightSeverity.INFORMATION.myVal)
}