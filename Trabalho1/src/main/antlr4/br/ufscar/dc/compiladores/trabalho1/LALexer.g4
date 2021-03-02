lexer grammar LALexer;

PALAVRA_CHAVE 
: 
    ( 'algoritmo' | 'declare'    | 'fim_algoritmo' |
      'literal'   | 'inteiro'    | 'leia'          |
      'escreva'   | 'constante'  | 'logico'        |
      'falso'     | 'entao'      | 'enquanto'      |
      'nao'       | 'e'          | 'verdadeiro'    |
      'se'        | 'faca'       | 'fim_enquanto'  |
      'fim_se'    | 'tipo'       | 'real'          |
      'var'       | 'fim_para'   | 'fim_registro'  | 
      'ou'        | 'senao'      | 'caso'          |
      'seja'      | 'para'       | 'ate'           |
      'fim_caso'  | 'registro'   | 'retorne'       |
      'funcao'    | 'fim_funcao' | 'procedimento'  |
      'fim_procedimento'
    )   
;

COMENTARIO 
:
    '{' ~('}' | '\n')* '}' -> skip
;

COMENTARIO_ERRO
:
    '{' ~('}')* '\n'
;

CADEIA_ERRO
:
    '"' ~('"')* '\n'
;

CADEIA 
:
    '"' ~('"' | '\n')* '"'
;

IDENT 
: 
    ( 'a'..'z' | 'A'..'Z' ) ( 'a'..'z' | 'A'..'Z' | '0'..'9' | '_' )*
;

NUM_INT 
:
    ( '0'..'9' )+
;

NUM_REAL
:
    ( '0'..'9' )+ '.' ('0'..'9')+
;

DELIM 
:
    ( ':' | ',' | '(' | 
      ')' | '[' | ']' | 
      '_' | '.' 
    )
;

OPERADORES 
:
    ( '='  | '+'  | '-'  |
      '<=' | '>=' | '<-' |
      '<>' | '<'  | '>'  | 
      '/'  | '*'  | '..' |
      '%'  | '^'  | '&'
    )
;

WS 
: 
    ( ' ' | '\t' | '\r' | '\n' ) -> skip
;

ERRO_SIMBOLO
:
    .
;