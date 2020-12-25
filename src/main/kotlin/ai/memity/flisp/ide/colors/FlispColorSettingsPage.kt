package ai.memity.flisp.ide.colors

import ai.memity.flisp.ide.FlispHighlighter
import ai.memity.flisp.ide.icons.FlispIcons
import ai.memity.flisp.lang.FlispLanguage
import com.intellij.codeHighlighting.RainbowHighlighter
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.RainbowColorSettingsPage
import org.jetbrains.annotations.NotNull

class FlispColorSettingsPage : RainbowColorSettingsPage {

    private val ATTRS = FlispColors.values().map { it.attributesDescriptor }.toTypedArray()

    override fun getDemoText() = DEMO_TEXT
    override fun getDisplayName() = "Femtolisp"
    override fun getIcon() = FlispIcons.FLISP
    override fun getAttributeDescriptors() = ATTRS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter() = FlispHighlighter()
    override fun getAdditionalHighlightingTagToDescriptorMap() = ANNOTATOR_TAGS
    override fun isRainbowType(key: TextAttributesKey?): Boolean =
        key == FlispColors.FN_ARGUMENT.textAttributesKey || key == FlispColors.LET_BINDING.textAttributesKey
    override fun getLanguage() = FlispLanguage

    // This tags should be kept in sync with FlispAnnotator highlighting logic
    private val ANNOTATOR_TAGS = FlispColors.values().associateBy({ it.name }, { it.textAttributesKey })

    companion object {
        private val DEMO_TEXT =
            """
            <LINE_COMMENT>;; Femtolisp programming examples</LINE_COMMENT>

            <BLOCK_COMMENT>#| Femtolisp has
            block comments
            |#</BLOCK_COMMENT>

            <COMMENTED>#;(this is 
            a commented form)</COMMENTED>
            <QUOTED>`(this is a quoted form></QUOTED>

            <LINE_COMMENT>;; numeric literals</LINE_COMMENT>
            <NUMBER>0x1abc</NUMBER> <NUMBER>#x1fff</NUMBER> <NUMBER>#d123</NUMBER> <NUMBER>#o123</NUMBER> <NUMBER>#b1011</NUMBER> <NUMBER>123</NUMBER>

            <LINE_COMMENT>;; character literals</LINE_COMMENT>
            <CHARACTER>#\a</CHARACTER> <CHARACTER>#\0</CHARACTER> <CHARACTER>#\x0012</CHARACTER> <CHARACTER>#\u0001</CHARACTER>

            <LINE_COMMENT>;; strings</LINE_COMMENT>
            "a string"

            <LINE_COMMENT>;; vectors</LINE_COMMENT>
            [1 2 3 4]

            (<BUILTIN>define</BUILTIN> (<DEFINITION>foo</DEFINITION> <FN_ARGUMENT>x</FN_ARGUMENT> <FN_ARGUMENT>y</FN_ARGUMENT>)
                (<BUILTIN>if</BUILTIN> (<BUILTIN>eq?</BUILTIN> x :akeyword) #t #f)
                (<BUILTIN>*</BUILTIN> x y))

            (<BUILTIN>define-macro</BUILTIN> (<DEFINITION>case</DEFINITION> <FN_ARGUMENT>key</FN_ARGUMENT> . <FN_ARGUMENT>clauses</FN_ARGUMENT>)
              (<BUILTIN>define</BUILTIN> (<DEFINITION>vals->cond</DEFINITION> <FN_ARGUMENT>key</FN_ARGUMENT> <FN_ARGUMENT>v</FN_ARGUMENT>)
                (<BUILTIN>cond</BUILTIN>
                  ((<BUILTIN>eq?</BUILTIN> v 'else)   'else)
            	  ((<BUILTIN>null?</BUILTIN> v)       #f)
            	  ((<BUILTIN>symbol?</BUILTIN> v)     <QUASIQUOTE>`(<BUILTIN>eq?</BUILTIN>  ,key ,(<CALLABLE>quote-value</CALLABLE> v))</QUASIQUOTE>)
                  ((<BUILTIN>atom?</BUILTIN> v)       <QUASIQUOTE>`(<BUILTIN>eqv?</BUILTIN> ,key ,(<CALLABLE>quote-value</CALLABLE> v))</QUASIQUOTE>)
            	  ((<BUILTIN>null?</BUILTIN> (<BUILTIN>cdr</BUILTIN> v)) <QUASIQUOTE>`(<BUILTIN>eqv?</BUILTIN> ,key ,(quote-value (car v))</QUASIQUOTE>))
            	  ((<CALLABLE>every</CALLABLE> symbol? v)
            	                   `(<BUILTIN>memq</BUILTIN> ,key ',v))
            	  (<BUILTIN>else</BUILTIN>            `(<BUILTIN>memv</BUILTIN> ,key ',v))))
              (<BUILTIN>let</BUILTIN> ((<LET_BINDING>g</LET_BINDING> (<BUILTIN>gensym</BUILTIN>)))
                `(<BUILTIN>let</BUILTIN> ((,g ,key))
                   (<BUILTIN>cond</BUILTIN> ,.(<BUILTIN>map</BUILTIN> (<BUILTIN>lambda</BUILTIN> (clause)
            		      (<BUILTIN>cons</BUILTIN> (vals->cond g (<BUILTIN>car</BUILTIN> clause))
            			    (<BUILTIN>cdr</BUILTIN> clause)))
            		    clauses)))))

            (<BUILTIN>define-macro</BUILTIN> (<DEFINITION>assert</DEFINITION> <FN_ARGUMENT>expr</FN_ARGUMENT>) <QUASIQUOTE>`(<BUILTIN>if</BUILTIN> ,expr #t (<BUILTIN>raise</BUILTIN> '(<CALLABLE>assert-failed</CALLABLE> ,expr))</QUASIQUOTE>))

            ;            ${RainbowHighlighter.generatePaletteExample("\n; ")}

        """.trimIndent()
    }

}