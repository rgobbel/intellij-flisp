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
import com.intellij.util.containers.JBIterable;

public class FlListImpl extends FlPFormImpl implements FlList {

  public FlListImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull FlispVisitor visitor) {
    visitor.visitList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlispVisitor) accept((FlispVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public FlForm getCar() {
    return FlispPsiImplUtil.getCar(this);
  }

  @Override
  @NotNull
  public JBIterable<FlForm> getCdr() {
    return FlispPsiImplUtil.getCdr(this);
  }

  @Override
  @Nullable
  public FlForm getFirst() {
    return FlispPsiImplUtil.getFirst(this);
  }

  @Override
  @NotNull
  public JBIterable<FlForm> getRest() {
    return FlispPsiImplUtil.getRest(this);
  }

  @Override
  @Nullable
  public FlForm getNth(int n) {
    return FlispPsiImplUtil.getNth(this, n);
  }

}
