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

public class FlQuotedImpl extends FlLiteralImpl implements FlQuoted {

  public FlQuotedImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull FlispVisitor visitor) {
    visitor.visitQuoted(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlispVisitor) accept((FlispVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public FlForm getForm() {
    return findNotNullChildByClass(FlForm.class);
  }

}
