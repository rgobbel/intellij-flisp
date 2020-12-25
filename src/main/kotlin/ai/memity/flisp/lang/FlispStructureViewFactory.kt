package ai.memity.flisp.lang

import ai.memity.flisp.editor.LONG_TEXT_MAX
import ai.memity.flisp.editor.getFormPlaceholderText
import ai.memity.flisp.lang.psi.FlForm
import ai.memity.flisp.lang.psi.FlList
import ai.memity.flisp.lang.psi.FlPForm
import ai.memity.flisp.lang.psi.FlispFile
import ai.memity.flisp.util.flTraverser
import ai.memity.flisp.util.*
import ai.memity.flisp.util.parentForms
import com.intellij.ide.structureView.*
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.LocationPresentation
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.containers.JBIterable
import com.intellij.util.ui.EmptyIcon
import javax.swing.Icon

class FlispStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile) = object : TreeBasedStructureViewBuilder() {
        override fun createStructureViewModel(editor: Editor?) = MyModel(psiFile, editor)
    }

    private class MyModel constructor(file: PsiFile, editor: Editor?) :
        StructureViewModelBase(file, editor, MyElement(file)), StructureViewModel.ElementInfoProvider {
        init {
            withSuitableClasses(FlList::class.java)
        }

        override fun isAlwaysShowsPlus(o: StructureViewTreeElement) = false
        override fun isAlwaysLeaf(o: StructureViewTreeElement) = false
        override fun shouldEnterElement(o: Any?) = true
        override fun isSuitable(o: PsiElement?): Boolean {
            if (o is PsiFile) return true
            if (o !is FlList) return false
            val count = o.parentForms.size()
            return count < 2 || o.asDef != null
        }
    }

    private class MyElement(o: PsiElement) : PsiTreeElementBase<PsiElement>(o), SortableTreeElement,
        LocationPresentation {

        override fun getAlphaSortKey() = presentableText

        override fun getChildrenBase(): Collection<MyElement> = element.let { o ->
            when (o) {
                is FlispFile -> o.defs()
                is FlForm -> o.flTraverser()
                    .expand { it == o || it.asDef == null }
                    .traverse()
                    .skip(1).map { it.asDef }.notNulls()
                else -> JBIterable.empty()
            }.map(::MyElement)
        }

        override fun getPresentableText(): String {
            val element = element as? FlForm ?: return ""
            val def = element.asDef?.def
            if (def != null) {
                return def.name.text
            }
            return getFormPlaceholderText(element, LONG_TEXT_MAX)
        }

        override fun getLocationString() = (element as? FlForm)?.asDef?.def?.type ?: ""
        override fun getLocationPrefix() = " "
        override fun getLocationSuffix() = ""

        override fun getIcon(open: Boolean): Icon = (element as? NavigationItem)?.presentation?.getIcon(open) ?: EmptyIcon.ICON_16
    }

}
