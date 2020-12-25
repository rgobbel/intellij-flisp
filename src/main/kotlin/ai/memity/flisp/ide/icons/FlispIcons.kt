package ai.memity.flisp.ide.icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object FlispIcons {
    private fun load(path: String): Icon = IconLoader.getIcon(path, FlispIcons::class.java)
    val FLISP = load("/icons/flisp.svg")
    val FN_DEF = load("/icons/function-def-icon.svg")
    val MACRO_DEF = load("/icons/macro-def-icon.svg")
    val VAR_DEF = load("/icons/var-def-icon.svg")

    val FLISP_FILE = load("/icons/flisp-file.svg")
}