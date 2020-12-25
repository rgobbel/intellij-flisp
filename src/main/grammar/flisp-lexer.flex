package ai.memity.flisp.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static ai.memity.flisp.lang.psi.FlispElementTypes.*;

%%

%{
  public _FlispLexer() {
    this((java.io.Reader)null);
  }
%}

%{
  private int zzNestedCommentLevel = 0;
  private int zzPostponedMarkedPos = -1;

  IElementType finishBlockComment() {
    assert(zzNestedCommentLevel == 0);
    yybegin(YYINITIAL);
    zzStartRead = zzPostponedMarkedPos;
    zzPostponedMarkedPos = -1;
    return F_BLOCK_COMMENT;
  }
%}

%state IN_BLOCK_COMMENT IN_CHAR_CONSTANT READ_MACRO_DISPATCH
%state SUPERQUOTE IN_STRING STRING_ESCAPE SQ_ESCAPE

//%debug

%public
%class _FlispLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode





//EOL=\R

EOL_WS           = \n | \r | \r\n
//LINE_WS          = [\ \t]
LINE_WS=[ \t\n\x0B\f\r]+
WHITE_SPACE_CHAR = {EOL_WS} | {LINE_WS}
WHITE_SPACE      = {WHITE_SPACE_CHAR}+

BOOL=#t|#f
CHAR_LITERAL_START=#\\
CHAR_NAME=(nul|alarm|backspace|tab|linefeed|vtab|page|return|esc|delete|newline)
SINGLE_CHAR_CONSTANT=#\\\w
UNICODE=[xu]\p{XDigit}{1,4}
//MACRO_DISPATCH=(#'|#\\@|#.)
//BLOCK_COMMENT_BODY=([^#=]|(=[^#])|(#[^=]))
//BLOCK_COMMENT=#\|([^#=]|(=[^#])|(#[^=]))\|#
LINE_COMMENT=;.*
STR_CHAR=[^\\\"]|\\.|\\\"
STRING=\" {STR_CHAR}* \"
//STRING=\"[^\"]*\"
KW=(:[^()\[\]'\";`,\\| \f\n\r\t]+|:[^()\[\]'\";`,\\| \f\n\r\t]+:|[^()\[\]'\";`,\\| \f\n\r\t]+:)
NUM=(0x\p{XDigit}+|0[0-7]+|(\d+|(\d?\.\d+)|(\d+\.\d*))|#(b[01]+|o[0-7]+|d(\d+|(\d?\.\d+)|(\d+\.\d*))|x\p{XDigit}+)|\+NaN|-NaN|\+Inf|-Inf)
SYM=([^()\[\]'\";`,\\| \f\n\r\t]|\?|:)+
//ONE_CHAR_LITERAL=\\.

%%
<YYINITIAL> {
  {WHITE_SPACE}     { return WHITE_SPACE; }
  {LINE_COMMENT}    { return F_LINE_COMMENT; }

//  "let"             { return F_BI_LET; }
//  "let*"            { return F_BI_LET_STAR; }
//  "defun"           { return F_CL_DEFUN; }
//  "defmacro"        { return F_CL_DEFMACRO; }
//  "defvar"          { return F_CL_DEFVAR; }
//  "define"          { return F_SCM_DEFINE; }
//  "define-macro"    { return F_SCM_DEFMACRO; }
  "("               { return F_LPAR; }
  ")"               { return F_RPAR; }
  "["               { return F_LBRACK; }
  "]"               { return F_RBRACK; }
  "{"               { return F_LBRACE; }
  "}"               { return F_RBRACE; }
  "#("              { return F_LPAR_VEC; }
  ",."              { return F_COMMA_DOT; }
  "."               { return F_DOT; }
  "#;"              { return F_FORM_COMMENT_START; }
  "'"               { return F_QUOTE_CHAR; }
  "`"               { return F_BQUOTE_CHAR; }
  "#"               { yybegin(READ_MACRO_DISPATCH); }
  ","               { return F_COMMA; }
  "|"               { yybegin(SUPERQUOTE); }
  '\"'              { yybegin(IN_STRING); }
//  ",@"              { return F_COMMA_AT; }
//  ",."              { return F_COMMA_DOT; }
//  ",\\@"            { return F_COMMA_BACK_AT; }
//  "\\@"             { return F_BACK_AT; }
//  "#.#"             { return F_BACKREF; }
//  "#="              { return F_BACKREF_DEF; }
  "#|"              { yybegin(IN_BLOCK_COMMENT); yypushback(2); }
//  "#."              { return F_SHARP_DOT; }
//  "@"               { return F_ATSIGN; }
//  "#\\@"            { return F_SHARP_BACK_AT; }

  {STRING}                  { return F_STRING; }
//  {BLOCK_COMMENT_BODY}      { return F_BLOCK_COMMENT_BODY; }
//  {BLOCK_COMMENT}           { return F_BLOCK_COMMENT; }
  {KW}              { return F_KW; }
  {BOOL}            { return F_BOOL; }
  {CHAR_LITERAL_START}      { yybegin(IN_CHAR_CONSTANT); }
//  {ONE_CHAR_LITERAL}        { return F_ONE_CHAR_LITERAL; }
//  {WHITE_SPACE}             { return F_WHITE_SPACE; }
//  {UNICODE}                 { return F_UNICODE; }
  {NUM}                     { return F_NUM; }
  {SYM}                     { return F_SYM; }
  [^]               { return BAD_CHARACTER; }
}

<IN_BLOCK_COMMENT> {
  "#|"    { if (zzNestedCommentLevel++ == 0)
              zzPostponedMarkedPos = zzStartRead;
          }

  "|#"    { if (--zzNestedCommentLevel == 0)
              return finishBlockComment();
          }

  <<EOF>> { zzNestedCommentLevel = 0; return finishBlockComment(); }

  [^]     { }
}

<IN_STRING> {
    "\""    { yybegin(YYINITIAL); return F_STRING; }
    "\\"    { yybegin(STRING_ESCAPE); }
}

<STRING_ESCAPE> {
    [^]     { yybegin(IN_STRING); }
}

<SUPERQUOTE> {
    "|"  { yybegin(YYINITIAL); return F_SYM; }
    "\\"  { yybegin(SQ_ESCAPE); }
    [^]  { }
}

<SQ_ESCAPE> {
    [^]  { yybegin(SUPERQUOTE); }
}

<IN_CHAR_CONSTANT> {
    {CHAR_NAME}|{UNICODE} { yybegin(YYINITIAL); return F_CHAR; }
    [^] { yybegin(YYINITIAL); return F_CHAR; }
//    {ONE_CHAR_LITERAL} { if (yylength() == 1) {
//          yybegin(YYINITIAL);
//          return F_ONE_CHAR_LITERAL;
//          }
//      }
}

<READ_MACRO_DISPATCH> {
//  ";"     { yybegin(YYINITIAL); return F_COMMENTED; }
  "("     { yybegin(YYINITIAL); return F_LPAR_VEC; }
  "\\@"   { yybegin(YYINITIAL); return F_SHARP_BACK_AT; }
  "."     { yybegin(YYINITIAL); return F_SHARP_DOT; }
  "\\"    { yybegin(IN_CHAR_CONSTANT); }
  ".#"    { yybegin(YYINITIAL); return F_BACKREF; }
  "="     { yybegin(YYINITIAL); return F_BACKREF_DEF; }
}


[^] { return BAD_CHARACTER; }
