// This is a generated file. Not intended for manual editing.
package ai.memity.flisp.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.JBIterable;

public interface FlDotted extends FlPForm {

  @Nullable
  FlReaderMacro getReaderMacro();

  @Nullable
  FlForm getCar();

  @NotNull
  JBIterable<FlForm> getCdr();

}
