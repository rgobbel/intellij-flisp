package ai.memity.flisp.lang

import com.intellij.lang.Language

object FlispLanguage : Language("Femtolisp",
    "text/flisp", "text/x-flisp", "text/femtolisp", "application/x-flisp") {

    override fun isCaseSensitive() = true
    override fun getDisplayName() = "Femtolisp"
}