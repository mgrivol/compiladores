lexer grammar LALexer;

PALAVRA_CHAVE 
: 
    ( 'algoritmo' | 'declare'   | 'fim_algoritmo' |
      'literal'   | 'inteiro'   | 'leia'          |
      'escreva'   | 'constante' | 'logico'        |
      'falso'     | 'entao'     | 'enquanto'      |
      'nao'       | 'e'         | 'verdadeiro'    |
      'se'        | 'faca'      | 'fim_enquanto'  |
      'fim_se'    | 'tipo'      | 'real'          |
      'var'       | 'fim_registro' | 'fim_procedimento'
    )   
;

IDENT 
: 
    ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')*
;

NUM_INT 
:
    ( '0'..'9' )+
;

CADEIA 
:
    '"' (CADEIA | .)*? '"'
;

CADEIA_ERRO
:
    '"' (CADEIA | .)*?
;

OPERADORES 
:
    ( '='  | '+'  | '-'  |
      '<=' | '>=' | '<-' |
      '<'  | '>'  | '/'  |
      '*'
    )
;

DELIM 
:
    ( ':' | ',' | '(' | 
      ')' | '[' | ']' | 
      '_' | '.' 
    )
;

WS 
: 
    ( ' ' | '\t' | '\r' | '\n' ) -> skip
;

COMENTARIO_ERRO
:
    '{' ~('}')* '\n'
;

COMENTARIO 
:
    '{' ~('}' | '\n')* '}' -> skip
;

ERRO
:
    .
;