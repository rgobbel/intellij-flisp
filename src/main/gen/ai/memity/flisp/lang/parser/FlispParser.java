// This is a generated file. Not intended for manual editing.
package ai.memity.flisp.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static ai.memity.flisp.lang.psi.FlispElementTypes.*;
import static ai.memity.flisp.lang.parser.FlispParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;
import static ai.memity.flisp.lang.parser.FlispParserUtil.adapt_builder_;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class FlispParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType type, PsiBuilder builder) {
    parseLight(type, builder);
    return builder.getTreeBuilt();
  }

  public void parseLight(IElementType type, PsiBuilder builder) {
    boolean result;
    builder = adapt_builder_(type, builder, this, EXTENDS_SETS_);
    Marker marker = enter_section_(builder, 0, _COLLAPSE_, null);
    result = parse_root_(type, builder);
    exit_section_(builder, 0, marker, type, result, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType type, PsiBuilder builder) {
    return parse_root_(type, builder, 0);
  }

  static boolean parse_root_(IElementType type, PsiBuilder builder, int level) {
    return root(builder, level + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(F_BOOL_LITERAL, F_BQUOTED, F_CHAR_LITERAL, F_COMMENTED,
      F_DOTTED, F_FORM, F_KW_LITERAL, F_LIST,
      F_LITERAL, F_NUM_LITERAL, F_QUOTED, F_STRING_LITERAL,
      F_SYMBOL, F_VECTOR),
  };

  /* ********************************************************** */
  // BOOL
  public static boolean bool_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "bool_literal")) return false;
    if (!nextTokenIs(builder, F_BOOL)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_BOOL);
    exit_section_(builder, marker, F_BOOL_LITERAL, result);
    return result;
  }

  /* ********************************************************** */
  // '`' form
  public static boolean bquoted(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "bquoted")) return false;
    if (!nextTokenIs(builder, F_BQUOTE_CHAR)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_BQUOTE_CHAR);
    result = result && form(builder, level + 1);
    exit_section_(builder, marker, F_BQUOTED, result);
    return result;
  }

  /* ********************************************************** */
  // CHAR
  public static boolean char_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "char_literal")) return false;
    if (!nextTokenIs(builder, F_CHAR)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_CHAR);
    exit_section_(builder, marker, F_CHAR_LITERAL, result);
    return result;
  }

  /* ********************************************************** */
  // '#;' form
  public static boolean commented(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "commented")) return false;
    if (!nextTokenIsFast(builder, F_FORM_COMMENT_START)) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_, F_COMMENTED, null);
    result = consumeTokenFast(builder, F_FORM_COMMENT_START);
    pinned = result; // pin = 1
    result = result && form(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  /* ********************************************************** */
  // '.'  form
  public static boolean dotted(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "dotted")) return false;
    if (!nextTokenIs(builder, F_DOT)) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _LEFT_ | _UPPER_, F_DOTTED, null);
    result = consumeToken(builder, F_DOT);
    pinned = result; // pin = 1
    result = result && form(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  /* ********************************************************** */
  // (form_prefix form_prefix *) form_upper | form_base
  public static boolean form(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "form")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, F_FORM, "<form>");
    result = form_0(builder, level + 1);
    if (!result) result = form_base(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // (form_prefix form_prefix *) form_upper
  private static boolean form_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "form_0")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_);
    result = form_0_0(builder, level + 1);
    pinned = result; // pin = 1
    result = result && form_upper(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  // form_prefix form_prefix *
  private static boolean form_0_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "form_0_0")) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _NONE_);
    result = form_prefix(builder, level + 1);
    pinned = result; // pin = 1
    result = result && form_0_0_1(builder, level + 1);
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  // form_prefix *
  private static boolean form_0_0_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "form_0_0_1")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!form_prefix(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "form_0_0_1", pos)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // p_forms | s_forms
  static boolean form_base(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "form_base")) return false;
    boolean result;
    result = p_forms(builder, level + 1);
    if (!result) result = s_forms(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // commented | reader_macro
  static boolean form_prefix(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "form_prefix")) return false;
    boolean result;
    result = commented(builder, level + 1);
    if (!result) result = reader_macro(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // form_base
  public static boolean form_upper(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "form_upper")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_ | _UPPER_, F_FORM, "<form>");
    result = form_base(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // (<<recover>> (commented | <<param>>)) *
  static boolean items(PsiBuilder builder, int level, Parser aRecover, Parser aParam) {
    if (!recursion_guard_(builder, level, "items")) return false;
    Marker marker = enter_section_(builder, level, _NONE_);
    while (true) {
      int pos = current_position_(builder);
      if (!items_0(builder, level + 1, aRecover, aParam)) break;
      if (!empty_element_parsed_guard_(builder, "items", pos)) break;
    }
    exit_section_(builder, level, marker, true, false, aRecover);
    return true;
  }

  // <<recover>> (commented | <<param>>)
  private static boolean items_0(PsiBuilder builder, int level, Parser aRecover, Parser aParam) {
    if (!recursion_guard_(builder, level, "items_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = aRecover.parse(builder, level);
    result = result && items_0_1(builder, level + 1, aParam, aRecover);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // commented | <<param>>
  private static boolean items_0_1(PsiBuilder builder, int level, Parser aParam, Parser aRecover) {
    if (!recursion_guard_(builder, level, "items_0_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = commented(builder, level + 1);
    if (!result) result = aParam.parse(builder, level);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // (<<sentinel>> (commented | <<param>>)) *
  static boolean items2(PsiBuilder builder, int level, Parser aSentinel, Parser aParam, Parser aRecover) {
    if (!recursion_guard_(builder, level, "items2")) return false;
    Marker marker = enter_section_(builder, level, _NONE_);
    while (true) {
      int pos = current_position_(builder);
      if (!items2_0(builder, level + 1, aSentinel, aParam, aRecover)) break;
      if (!empty_element_parsed_guard_(builder, "items2", pos)) break;
    }
    exit_section_(builder, level, marker, true, false, aRecover);
    return true;
  }

  // <<sentinel>> (commented | <<param>>)
  private static boolean items2_0(PsiBuilder builder, int level, Parser aSentinel, Parser aParam, Parser aRecover) {
    if (!recursion_guard_(builder, level, "items2_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = aSentinel.parse(builder, level);
    result = result && items2_0_1(builder, level + 1, aParam, aRecover);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // commented | <<param>>
  private static boolean items2_0_1(PsiBuilder builder, int level, Parser aParam, Parser aRecover) {
    if (!recursion_guard_(builder, level, "items2_0_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = commented(builder, level + 1);
    if (!result) result = aParam.parse(builder, level);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // KW
  public static boolean kw_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "kw_literal")) return false;
    if (!nextTokenIs(builder, F_KW)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_KW);
    exit_section_(builder, marker, F_KW_LITERAL, result);
    return result;
  }

  /* ********************************************************** */
  // '(' list_body dotted? ')'
  public static boolean list(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "list")) return false;
    if (!nextTokenIs(builder, F_LPAR)) return false;
    boolean result, pinned;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, F_LIST, null);
    result = consumeToken(builder, F_LPAR);
    pinned = result; // pin = 1
    result = result && report_error_(builder, list_body(builder, level + 1));
    result = pinned && report_error_(builder, list_2(builder, level + 1)) && result;
    result = pinned && consumeToken(builder, F_RPAR) && result;
    exit_section_(builder, level, marker, result, pinned, null);
    return result || pinned;
  }

  // dotted?
  private static boolean list_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "list_2")) return false;
    dotted(builder, level + 1);
    return true;
  }

  /* ********************************************************** */
  // <<items2 !('.'|')') form !(')'|'.')>>
  static boolean list_body(PsiBuilder builder, int level) {
    return items2(builder, level + 1, FlispParser::list_body_0_0, FlispParser::form, FlispParser::list_body_0_2);
  }

  // !('.'|')')
  private static boolean list_body_0_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "list_body_0_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NOT_);
    result = !list_body_0_0_0(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // '.'|')'
  private static boolean list_body_0_0_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "list_body_0_0_0")) return false;
    boolean result;
    result = consumeToken(builder, F_DOT);
    if (!result) result = consumeToken(builder, F_RPAR);
    return result;
  }

  // !(')'|'.')
  private static boolean list_body_0_2(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "list_body_0_2")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NOT_);
    result = !list_body_0_2_0(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // ')'|'.'
  private static boolean list_body_0_2_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "list_body_0_2_0")) return false;
    boolean result;
    result = consumeToken(builder, F_RPAR);
    if (!result) result = consumeToken(builder, F_DOT);
    return result;
  }

  /* ********************************************************** */
  // quoted | bquoted | num_literal | kw_literal | string_literal | bool_literal | char_literal  {
  // //    methods=[number="" quoted="" bquoted=""]
  // }
  public static boolean literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "literal")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _COLLAPSE_, F_LITERAL, "<literal>");
    result = quoted(builder, level + 1);
    if (!result) result = bquoted(builder, level + 1);
    if (!result) result = num_literal(builder, level + 1);
    if (!result) result = kw_literal(builder, level + 1);
    if (!result) result = string_literal(builder, level + 1);
    if (!result) result = bool_literal(builder, level + 1);
    if (!result) result = literal_6(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // char_literal  {
  // //    methods=[number="" quoted="" bquoted=""]
  // }
  private static boolean literal_6(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "literal_6")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = char_literal(builder, level + 1);
    result = result && literal_6_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // {
  // //    methods=[number="" quoted="" bquoted=""]
  // }
  private static boolean literal_6_1(PsiBuilder builder, int level) {
    return true;
  }

  /* ********************************************************** */
  // !<<eof>>
  static boolean not_eof(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "not_eof")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NOT_);
    result = !eof(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // num
  public static boolean num_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "num_literal")) return false;
    if (!nextTokenIs(builder, F_NUM)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_NUM);
    exit_section_(builder, marker, F_NUM_LITERAL, result);
    return result;
  }

  /* ********************************************************** */
  // dotted | list | vector
  static boolean p_forms(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "p_forms")) return false;
    boolean result;
    result = dotted(builder, level + 1);
    if (!result) result = list(builder, level + 1);
    if (!result) result = vector(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // "'" form
  public static boolean quoted(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "quoted")) return false;
    if (!nextTokenIs(builder, F_QUOTE_CHAR)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_QUOTE_CHAR);
    result = result && form(builder, level + 1);
    exit_section_(builder, marker, F_QUOTED, result);
    return result;
  }

  /* ********************************************************** */
  // ',.' | '.#' | ',' | ',@' | '=' | ',\\@' | '\\@' | '|'
  public static boolean reader_macro(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "reader_macro")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, F_READER_MACRO, "<form>");
    result = consumeToken(builder, F_COMMA_DOT);
    if (!result) result = consumeToken(builder, F_DOT_SHARP);
    if (!result) result = consumeToken(builder, F_COMMA);
    if (!result) result = consumeToken(builder, F_COMMA_AT);
    if (!result) result = consumeToken(builder, F_EQUALS);
    if (!result) result = consumeToken(builder, F_COMMA_BACK_AT);
    if (!result) result = consumeToken(builder, F_BACK_AT);
    if (!result) result = consumeToken(builder, F_PIPE);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  /* ********************************************************** */
  // root_entry *
  static boolean root(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "root")) return false;
    while (true) {
      int pos = current_position_(builder);
      if (!root_entry(builder, level + 1)) break;
      if (!empty_element_parsed_guard_(builder, "root", pos)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // not_eof (commented|form)
  static boolean root_entry(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "root_entry")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = not_eof(builder, level + 1);
    result = result && root_entry_1(builder, level + 1);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // commented|form
  private static boolean root_entry_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "root_entry_1")) return false;
    boolean result;
    result = commented(builder, level + 1);
    if (!result) result = form(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // symbol | literal
  static boolean s_forms(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "s_forms")) return false;
    boolean result;
    result = symbol(builder, level + 1);
    if (!result) result = literal(builder, level + 1);
    return result;
  }

  /* ********************************************************** */
  // STRING
  public static boolean string_literal(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "string_literal")) return false;
    if (!nextTokenIs(builder, F_STRING)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_STRING);
    exit_section_(builder, marker, F_STRING_LITERAL, result);
    return result;
  }

  /* ********************************************************** */
  // sym
  public static boolean symbol(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "symbol")) return false;
    if (!nextTokenIs(builder, F_SYM)) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_SYM);
    exit_section_(builder, marker, F_SYMBOL, result);
    return result;
  }

  /* ********************************************************** */
  // '[' vector_body ']' | '#(' list_body ')'
  public static boolean vector(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "vector")) return false;
    if (!nextTokenIs(builder, "<vector>", F_LBRACK, F_LPAR_VEC)) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NONE_, F_VECTOR, "<vector>");
    result = vector_0(builder, level + 1);
    if (!result) result = vector_1(builder, level + 1);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

  // '[' vector_body ']'
  private static boolean vector_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "vector_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_LBRACK);
    result = result && vector_body(builder, level + 1);
    result = result && consumeToken(builder, F_RBRACK);
    exit_section_(builder, marker, null, result);
    return result;
  }

  // '#(' list_body ')'
  private static boolean vector_1(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "vector_1")) return false;
    boolean result;
    Marker marker = enter_section_(builder);
    result = consumeToken(builder, F_LPAR_VEC);
    result = result && list_body(builder, level + 1);
    result = result && consumeToken(builder, F_RPAR);
    exit_section_(builder, marker, null, result);
    return result;
  }

  /* ********************************************************** */
  // <<items !']' form>>
  static boolean vector_body(PsiBuilder builder, int level) {
    return items(builder, level + 1, FlispParser::vector_body_0_0, FlispParser::form);
  }

  // !']'
  private static boolean vector_body_0_0(PsiBuilder builder, int level) {
    if (!recursion_guard_(builder, level, "vector_body_0_0")) return false;
    boolean result;
    Marker marker = enter_section_(builder, level, _NOT_);
    result = !consumeToken(builder, F_RBRACK);
    exit_section_(builder, level, marker, result, false, null);
    return result;
  }

}
