package ai.memity.flisp.util

import ai.memity.flisp.lang.FlispFileType
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.COMMENTS
import ai.memity.flisp.lang.parser.FlispParserDefinition.Companion.WHITESPACES
import ai.memity.flisp.lang.psi.*
import ai.memity.flisp.lang.psi.FlispTokenType.Companion.DEF_ALIKE_SYMBOLS
import ai.memity.flisp.lang.psi.ext.FlElement
import ai.memity.flisp.lang.psi.ext.Role
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.isMacroDef
import ai.memity.flisp.lang.psi.impl.FlispPsiImplUtil.Companion.isTopLevelForm
import com.intellij.lang.ASTNode
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.SmartList
import com.intellij.util.containers.JBIterable
import kotlin.reflect.KClass

fun findSymbols(project: Project, key: String) : List<FlSymbol> {
    val result: SmartList<FlSymbol> = SmartList()
    val virtualFiles =
        FileTypeIndex.getFiles(FlispFileType, GlobalSearchScope.allScope(project))
    for (virtualFile in virtualFiles) {
        val flFile: FlispFile = PsiManager.getInstance(project).findFile(virtualFile) as FlispFile
        val symbols = PsiTreeUtil.getChildrenOfType(flFile, FlSymbol::class.java)
        for (symbol in symbols) {
            if (key == symbol.text) result.add(symbol)
        }
    }
    return result
}

fun findSymbols(project: Project) : List<FlSymbol> {
    val result: SmartList<FlSymbol> = SmartList()
    val virtualFiles =
        FileTypeIndex.getFiles(FlispFileType, GlobalSearchScope.allScope(project))
    for (virtualFile in virtualFiles) {
        val flFile: FlispFile = PsiManager.getInstance(project).findFile(virtualFile) as FlispFile
        val symbols = PsiTreeUtil.getChildrenOfType(flFile, FlSymbol::class.java)
        result.addAll(symbols)
    }
    return result
}

fun findTopLevelSymbols(project: Project) = findSymbols(project).filter { it.parentForm?.isTopLevelForm ?: false }

fun PsiElement?.flTraverser(): SyntaxTraverser<PsiElement> = _flTraverser().withRoot(this)
//fun PsiElement?.flTraverserRCAware(): SyntaxTraverser<PsiElement> = flTraverser().forceDisregard { e ->
//    (e as? CElement)?.role.let { r -> r == Role.RCOND || r == Role.RCOND_S } ||
//            e.parent.role.let { pr -> pr == Role.RCOND_S && e.prevForm is CKeyword }
//}.forceIgnore { e ->
//    e is CReaderMacro && e.firstChild.elementType.let { it == ClojureTypes.C_SHARP_QMARK_AT || it == ClojureTypes.C_SHARP_QMARK } ||
//            e.parentForm?.role.let { it == Role.RCOND || it == Role.RCOND_S } && e.prevForm !is CKeyword
//}

fun ASTNode?.flNodeTraverser(): SyntaxTraverser<ASTNode> = _flNodeTraverser().withRoot(this)


@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> Any?.cast(): T? = this as? T

fun <E> E.isIn(c: Collection<E>) = c.contains(this)
fun String?.prefixedBy(c: Iterable<String>) = this != null && c.find { this.startsWith("$it.") } != null
fun <E> Array<E>?.iterate() = this.jbIt()
fun <T> Iterable<T>?.iterate() = this.jbIt()
fun <E: Any> E?.generate(f : (E)->E?) = JBIterable.generate<E>(this, f)
fun <E: Any> E?.asListOrEmpty() = listOfNotNull(this)
fun <T> Iterable<T>?.jbIt() = JBIterable.from(this)
fun <T> Array<T>?.jbIt() = if (this == null) JBIterable.empty() else JBIterable.of(*this)
inline fun <T> Iterable<T>?.forEachWithPrev(f: (T, T?)->Unit) = if (this == null) Unit
    else { var prev: T? = null; forEach { cur -> f(cur, prev); prev = cur; }}

fun <T> readAction(block : () -> T) = ReadAction.compute<T, RuntimeException> { block.invoke() }

fun PsiElement?.isAncestorOf(o: PsiElement) = PsiTreeUtil.isAncestor(this, o, false)
fun <T : PsiElement> PsiElement?.findParent(c: KClass<T>) = PsiTreeUtil.getParentOfType(this, c.java)
fun <T : PsiElement> PsiElement?.findChild(c: KClass<T>) = PsiTreeUtil.getChildOfType(this, c.java)
fun <T : PsiElement> PsiElement?.findNext(c: KClass<T>) = PsiTreeUtil.getNextSiblingOfType(this, c.java)
fun <T : PsiElement> PsiElement?.findPrev(c: KClass<T>) = PsiTreeUtil.getPrevSiblingOfType(this, c.java)

val PsiElement?.role : Role get() = (this as? FlElement)?.role ?: Role.NONE
val PsiElement?.flags : Int get() = (this as? FlElement)?.flags ?: 0
fun <T : FlForm> PsiElement?.childForm(c: KClass<T>) = skipComments({ findChild(c) }) { findNext(c) }
fun <T : FlForm> PsiElement?.nextForm(c: KClass<T>) = skipComments { findNext(c) }
fun <T : FlForm> PsiElement?.prevForm(c: KClass<T>) = skipComments { findPrev(c) }
private fun <E : PsiElement> PsiElement?.skipComments(f0: (PsiElement?.() -> E?)? = null, f : PsiElement?.()->E?): E? {
    var o = (f0 ?: f)(); while (true) if (notComment(o)) return o else o = o.f()
}
private fun <E : PsiElement> notComment(e: E?) = !(e is FlCommented) //e.fastFlags and FLAG_COMMENTED != FLAG_COMMENTED

val PsiElement?.asDef : FlList? get() = if (role == Role.DEF) this as? FlList else null
val PsiElement?.elementType : IElementType? get() = this?.node?.elementType
val PsiElement?.deepFirst: PsiElement? get() = this?.let{ PsiTreeUtil.getDeepestFirst(this) }
val PsiElement?.deepLast: PsiElement? get() = this?.let{ PsiTreeUtil.getDeepestLast(this) }
val PsiElement?.firstForm: FlForm? get() = childForm(FlForm::class)
val PsiElement?.nextForm: FlForm? get() = nextForm(FlForm::class)
val PsiElement?.prevForm: FlForm? get() = prevForm(FlForm::class)
val PsiElement?.prevForms: JBIterable<FlForm> get() = JBIterable.generate(prevForm(FlForm::class)) { it.prevForm }
val PsiElement?.thisForm: FlForm? get() = (this as? FlForm ?: findParent(FlForm::class))//.let {
//   ((it as? FlSymbol)?.parent as? FlSymbol ?: it).let { it?.parent as? CSForm ?: it } }
val PsiElement?.parentForm: FlForm? get() = thisForm.findParent(FlForm::class)
val PsiElement?.parentForms: JBIterable<FlForm> get() = JBIterable.generate(this.thisForm) { it.parentForm }
val PsiElement?.childForms: JBIterable<FlForm> get() = iterate(FlForm::class).filter(::notComment)
fun <T : FlForm> PsiElement.childForms(c: KClass<T>) = iterate(c).filter(::notComment)
val PsiElement?.nextForms: JBIterable<FlForm> get() = siblings().filter(FlForm::class).filter(::notComment)
fun IElementType?.wsOrComment() = this != null && (WHITESPACES.contains(this) || COMMENTS.contains(this))

//fun PsiElement?.findChild(role: Role) = iterate().find { (it as? FlElement)?.role == role } as FlForm?
fun PsiElement?.findChild(c: IElementType) = this?.node?.findChildByType(c)?.psi
fun PsiElement?.findNext(c: IElementType) = TreeUtil.findSibling(this?.node, c)?.psi
fun PsiElement?.findPrev(c: IElementType) = TreeUtil.findSiblingBackward(this?.node, c)?.psi

//val IDef.qualifiedName: String
//    get() = name.withNamespace(namespace)
//fun String.withNamespace(namespace: String) = if (namespace.isEmpty()) this else "$namespace/$this"
//fun String.withPackage(packageName: String) = if (packageName.isEmpty()) this else "$packageName.$this"

fun VirtualFile.toIoFile() = VfsUtil.virtualToIoFile(this)

@Suppress("UNCHECKED_CAST")
fun <T> JBIterable<T>.notNulls(): JBIterable<T> = filter { it != null }

//fun <PsiElement> JBIterable<PsiElement>.notNulls(): JBIterable<PsiElement> = filter { it != null } as JBIterable<PsiElement>
fun <T: Any?, E: Any> JBIterable<T>.filter(c : KClass<E>) = filter(c.java)

fun ASTNode?.iterate(): JBIterable<ASTNode> =
    if (this == null) JBIterable.empty() else flNodeTraverser().expandAndSkip(Conditions.equalTo(this)).traverse()

fun FlForm?.iterate(): JBIterable<PsiElement> = (this as PsiElement?).iterate()

fun PsiElement?.iterate(): JBIterable<PsiElement> = when {
    this == null -> JBIterable.empty()
    this is FlispFile -> flTraverser().expandAndSkip(Conditions.equalTo(this)).traverse()
    else -> firstChild?.siblings() ?: JBIterable.empty()
}
fun <E> SyntaxTraverser<E>.iterate(e: E) = withRoot(e).expandAndSkip(Conditions.equalTo(e)).traverse()
fun <T> Iterator<T>.safeNext(): T? = if (hasNext()) next() else null

fun <T: Any> PsiElement?.iterate(c: KClass<T>): JBIterable<T> = iterate().filter(c)

//fun PsiElement?.iterateRCAware(): JBIterable<PsiElement> =
//    if (this == null) JBIterable.empty() else flTraverserRCAware().expandAndSkip(Conditions.equalTo(this)).traverse()

fun PsiElement?.siblings(): JBIterable<PsiElement> =
    if (this == null) JBIterable.empty() else JBIterable.generate(this) { it.nextSibling }.notNulls()

fun PsiElement?.prevSiblings(): JBIterable<PsiElement> =
    if (this == null) JBIterable.empty() else JBIterable.generate(this) { it.prevSibling }.notNulls()

fun PsiElement?.parents(): JBIterable<PsiElement> = SyntaxTraverser.psiApi().parents(this)
fun PsiElement?.contexts(): JBIterable<PsiElement> = JBIterable.generate(this) { it.context }

fun _flTraverser(): SyntaxTraverser<PsiElement> = SyntaxTraverser.psiTraverser()
    .forceDisregardTypes { it == GeneratedParserUtilBase.DUMMY_BLOCK }

fun _flNodeTraverser(): SyntaxTraverser<ASTNode> = SyntaxTraverser.astTraverser()
    .forceDisregardTypes { it == GeneratedParserUtilBase.DUMMY_BLOCK }

val FlForm.asDef : FlList? get() {
    ((this is FlList) && this.isTopLevelForm) || return null
    if (DEF_ALIKE_SYMBOLS.contains(this.childForms[0]?.text)) return (this as FlList)
    return null
}