// This is a generated file. Not intended for manual editing.
package ai.memity.flisp.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static ai.memity.flisp.lang.psi.FlispElementTypes.*;
import ai.memity.flisp.lang.psi.*;

public class FlFormImpl extends FlElementImpl implements FlForm {

  public FlFormImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FlispVisitor visitor) {
    visitor.visitForm(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlispVisitor) accept((FlispVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<FlReaderMacro> getReaderMacroList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, FlReaderMacro.class);
  }

  @Override
  @NotNull
  public String toString() {
    return FlispPsiImplUtil.toString(this);
  }

}
