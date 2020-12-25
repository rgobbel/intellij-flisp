// This is a generated file. Not intended for manual editing.
package ai.memity.flisp.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import ai.memity.flisp.lang.psi.impl.*;

public interface FlispElementTypes {

  IElementType F_BOOL_LITERAL = new FlispElementType("F_BOOL_LITERAL");
  IElementType F_BQUOTED = new FlispElementType("F_BQUOTED");
  IElementType F_CHAR_LITERAL = new FlispElementType("F_CHAR_LITERAL");
  IElementType F_COMMENTED = new FlispElementType("F_COMMENTED");
  IElementType F_DOTTED = new FlispElementType("F_DOTTED");
  IElementType F_FORM = new FlispElementType("F_FORM");
  IElementType F_KW_LITERAL = new FlispElementType("F_KW_LITERAL");
  IElementType F_LIST = new FlispElementType("F_LIST");
  IElementType F_LITERAL = new FlispElementType("F_LITERAL");
  IElementType F_NUM_LITERAL = new FlispElementType("F_NUM_LITERAL");
  IElementType F_QUOTED = new FlispElementType("F_QUOTED");
  IElementType F_READER_MACRO = new FlispElementType("F_READER_MACRO");
  IElementType F_STRING_LITERAL = new FlispElementType("F_STRING_LITERAL");
  IElementType F_SYMBOL = new FlispElementType("F_SYMBOL");
  IElementType F_VECTOR = new FlispElementType("F_VECTOR");

  IElementType F_ATSIGN = new FlispTokenType("@");
  IElementType F_BACKREF = new FlispTokenType("#.#");
  IElementType F_BACKREF_DEF = new FlispTokenType("#=");
  IElementType F_BACK_AT = new FlispTokenType("\\\\@");
  IElementType F_BLOCK_COMMENT = new FlispTokenType("BLOCK_COMMENT");
  IElementType F_BLOCK_COMMENT_BODY = new FlispTokenType("BLOCK_COMMENT_BODY");
  IElementType F_BLOCK_COMMENT_END = new FlispTokenType("|#");
  IElementType F_BLOCK_COMMENT_START = new FlispTokenType("#|");
  IElementType F_BOOL = new FlispTokenType("BOOL");
  IElementType F_BQUOTE_CHAR = new FlispTokenType("`");
  IElementType F_CHAR = new FlispTokenType("CHAR");
  IElementType F_CHAR_LITERAL_START = new FlispTokenType("CHAR_LITERAL_START");
  IElementType F_CHAR_NAME = new FlispTokenType("CHAR_NAME");
  IElementType F_COLON = new FlispTokenType(":");
  IElementType F_COMMA = new FlispTokenType(",");
  IElementType F_COMMA_AT = new FlispTokenType(",@");
  IElementType F_COMMA_BACK_AT = new FlispTokenType(",\\\\@");
  IElementType F_COMMA_DOT = new FlispTokenType(",.");
  IElementType F_COMMENT = new FlispTokenType("comment");
  IElementType F_DOT = new FlispTokenType(".");
  IElementType F_DOT_SHARP = new FlispTokenType(".#");
  IElementType F_EQUALS = new FlispTokenType("=");
  IElementType F_FORM_COMMENT_START = new FlispTokenType("#;");
  IElementType F_KW = new FlispTokenType("KW");
  IElementType F_LBRACE = new FlispTokenType("{");
  IElementType F_LBRACK = new FlispTokenType("[");
  IElementType F_LINE_COMMENT = new FlispTokenType(";.*");
  IElementType F_LPAR = new FlispTokenType("(");
  IElementType F_LPAR_VEC = new FlispTokenType("#(");
  IElementType F_NUM = new FlispTokenType("num");
  IElementType F_ONE_CHAR_LITERAL = new FlispTokenType("ONE_CHAR_LITERAL");
  IElementType F_PIPE = new FlispTokenType("|");
  IElementType F_QUOTE_CHAR = new FlispTokenType("'");
  IElementType F_RBRACE = new FlispTokenType("}");
  IElementType F_RBRACK = new FlispTokenType("]");
  IElementType F_RPAR = new FlispTokenType(")");
  IElementType F_SEMI = new FlispTokenType(";");
  IElementType F_SHARP = new FlispTokenType("#");
  IElementType F_SHARP_BACK_AT = new FlispTokenType("#\\\\@");
  IElementType F_SHARP_DOT = new FlispTokenType("#.");
  IElementType F_SPACE = new FlispTokenType(" ");
  IElementType F_STRING = new FlispTokenType("STRING");
  IElementType F_SYM = new FlispTokenType("sym");
  IElementType F_UNICODE = new FlispTokenType("UNICODE");
  IElementType F_WHITE_SPACE = new FlispTokenType("WHITE_SPACE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == F_BOOL_LITERAL) {
        return new FlBoolLiteralImpl(node);
      }
      else if (type == F_BQUOTED) {
        return new FlBquotedImpl(node);
      }
      else if (type == F_CHAR_LITERAL) {
        return new FlCharLiteralImpl(node);
      }
      else if (type == F_COMMENTED) {
        return new FlCommentedImpl(node);
      }
      else if (type == F_DOTTED) {
        return new FlDottedImpl(node);
      }
      else if (type == F_FORM) {
        return new FlFormImpl(node);
      }
      else if (type == F_KW_LITERAL) {
        return new FlKwLiteralImpl(node);
      }
      else if (type == F_LIST) {
        return new FlListImpl(node);
      }
      else if (type == F_NUM_LITERAL) {
        return new FlNumLiteralImpl(node);
      }
      else if (type == F_QUOTED) {
        return new FlQuotedImpl(node);
      }
      else if (type == F_READER_MACRO) {
        return new FlReaderMacroImpl(node);
      }
      else if (type == F_STRING_LITERAL) {
        return new FlStringLiteralImpl(node);
      }
      else if (type == F_SYMBOL) {
        return new FlSymbolImpl(node);
      }
      else if (type == F_VECTOR) {
        return new FlVectorImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
