lexer grammar LALexer;

PALAVRA_CHAVE   : 
      ALG | 'leia'
    ;

ALG     :
    'algoritmo'
    ;

IDENT   : 
        ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')*
    ;


