import java_cup.runtime.*;

%%
/* ----------------- Options and Declarations Section----------------- */

/*
   The name of the class JFlex will create will be Scanner.
   Will write the code to the file Scanner.java.
*/
%class Scanner

/*
   The current line number can be accessed with the variable yyline
   and the current column number with the variable yycolumn.
*/
%line
%column

/*
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup
%unicode

/*
   Declarations

   Code between %{ and %}, both of which must be at the beginning of a
   line, will be copied letter to letter into the lexer class source.
   Here you declare member variables and functions that are used inside
   scanner actions.
*/

%{
   /**
      The following two methods create java_cup.runtime.Symbol objects
   **/
   StringBuffer buffer = new StringBuffer();
   private Symbol symbol(int type) {
      return new Symbol(type, yyline, yycolumn);
   }
   private Symbol symbol(int type, Object value) {
      return new Symbol(type, yyline, yycolumn, value);
   }
%}

/*
   Macro Declarations

   These declarations are regular expressions that will be used latter
   in the Lexical Rules Section.
*/

/*
   A line terminator is a \r (carriage return), \n (line feed), or \r\n.
*/
LineTerminator = [\r\n]

/*
   White space is a line terminator, space, tab, or line feed.
*/
WhiteSpace     = {LineTerminator} | [\ \t\f]

/*
   An identifier is a word beginning with a character and followed
   by zero or more characters, numbers and underscores.
*/
FID            = [a-zA-Z][a-zA-Z0-9_]*\(
VID            = [a-zA-Z][a-zA-Z0-9_]*
FUNC_DEC       = \){WhiteSpace}*\{

%state STRING

%%
/* ------------------------Lexical Rules Section---------------------- */

<YYINITIAL> {
   "reverse"      { return symbol(sym.REVERSE); }
   "+"            { return symbol(sym.PLUS); }
   "if"           { return symbol(sym.IF); }
   "else"         { return symbol(sym.ELSE); }
   "prefix"       { return symbol(sym.PREFIX); }
   {FID}          { return symbol(sym.FID, yytext()); }
   {VID}          { return symbol(sym.VID, yytext()); }
   {FUNC_DEC}     { return symbol(sym.FUNC_DEC); }
   "}"            { return symbol(sym.RCURLPAREN); }
   "("            { return symbol(sym.LPAREN); }
   ")"            { return symbol(sym.RPAREN); }
   ","            { return symbol(sym.COMMA); }
   \"             { buffer.setLength(0); yybegin(STRING); }
   {WhiteSpace}   { /* just skip what was found, do nothing */ }
}

<STRING> {
   \"             { yybegin(YYINITIAL); return symbol(sym.STRING_LIT, buffer.toString()); }
   [^\n\r\"\\]+   { buffer.append(yytext()); }
   \\t            { buffer.append('\t'); }
   \\n            { buffer.append('\n'); }
   \\r            { buffer.append('\r'); }
   \\\"           { buffer.append('\"'); }
   \\             { buffer.append('\\'); }
}

/*
   No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found.
*/
[^]               { throw new Error("Illegal character <"+yytext()+">"); }
