package ai.memity.flisp.lang

import ai.memity.flisp.ide.icons.FlispIcons
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

@Suppress("DialogTitleCapitalization")
object FlispFileType : LanguageFileType(FlispLanguage) {

    override fun getName(): String = "Femtolisp"

    override fun getIcon(): Icon = FlispIcons.FLISP_FILE

    override fun getDefaultExtension(): String = "scm"

    override fun getCharset(file: VirtualFile, content: ByteArray): String = "UTF-8"

    override fun getDescription(): String = "Femtolisp Files"
}