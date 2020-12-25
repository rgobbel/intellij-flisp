// This is a generated file. Not intended for manual editing.
package ai.memity.flisp.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import ai.memity.flisp.lang.psi.ext.FlElement;

public class FlispVisitor extends PsiElementVisitor {

  public void visitBoolLiteral(@NotNull FlBoolLiteral o) {
    visitLiteral(o);
  }

  public void visitBquoted(@NotNull FlBquoted o) {
    visitLiteral(o);
  }

  public void visitCharLiteral(@NotNull FlCharLiteral o) {
    visitLiteral(o);
  }

  public void visitCommented(@NotNull FlCommented o) {
    visitForm(o);
  }

  public void visitDotted(@NotNull FlDotted o) {
    visitPForm(o);
  }

  public void visitForm(@NotNull FlForm o) {
    visitElement(o);
  }

  public void visitKwLiteral(@NotNull FlKwLiteral o) {
    visitLiteral(o);
  }

  public void visitList(@NotNull FlList o) {
    visitPForm(o);
  }

  public void visitLiteral(@NotNull FlLiteral o) {
    visitSForm(o);
  }

  public void visitNumLiteral(@NotNull FlNumLiteral o) {
    visitLiteral(o);
  }

  public void visitPForm(@NotNull FlPForm o) {
    visitForm(o);
  }

  public void visitQuoted(@NotNull FlQuoted o) {
    visitLiteral(o);
  }

  public void visitReaderMacro(@NotNull FlReaderMacro o) {
    visitElement(o);
  }

  public void visitSForm(@NotNull FlSForm o) {
    visitForm(o);
  }

  public void visitStringLiteral(@NotNull FlStringLiteral o) {
    visitLiteral(o);
  }

  public void visitSymbol(@NotNull FlSymbol o) {
    visitSForm(o);
  }

  public void visitVector(@NotNull FlVector o) {
    visitPForm(o);
  }

  public void visitElement(@NotNull FlElement o) {
    super.visitElement(o);
  }

}
