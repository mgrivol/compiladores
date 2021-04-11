// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619

// lexemas da linguagem TGEN
grammar TGEN;

WS : 
    ( ' ' | '\t' | '\r' | '\n' | '\r\n' ) -> skip
;

programa :
    inimigos 'start' ondas 'end'
;
inimigos :
    'inimigos' '{' ( inimigo )+ '}'
;
inimigo :
    IDENT '{' ( parametroInimigo ';' )* '}'
;
parametroInimigo :
    parInimigoForca
    | parInimigoVelocidade
    | parInimigoVida
    | parInimigoModelo
;
parInimigoForca :
    'forca' '=' INT
;
parInimigoVelocidade :
    'velocidade' '=' FLOAT
;
parInimigoVida :
    'vida' '=' INT 
;
parInimigoModelo :
    'modelo' '=' CADEIA
;
ondas :
    ( onda )+
;
onda :
    'onda' '{' ( comando ';' )+ '}'
;
comando :
    cmdSpawn
    | cmdAguarde
;
cmdSpawn :
    semDelay
    | comDelay
;
semDelay :
    IDENT '(' INT ')'
;
comDelay :
    IDENT '(' INT ',' FLOAT ')' 
;
cmdAguarde :
    'aguarde' '(' FLOAT ')' 
;
INT :
    ( '0'..'9' )+
;
FLOAT :
    ( '0'..'9' )+ '.' ( '0'..'9' )+
;
IDENT :
    ( 'a'..'z' | 'A'..'Z' ) ( 'a'..'z' | 'A'..'Z' | '0'..'9' | '_' )*
;
COMENTARIO :
    '%' ~( '\n' | '%' )* '%' -> skip
;
ERRO_COMENTARIO :
    '%' ~( '%' )* '\n'
;
CADEIA :
    '"' ~('"' | '\n')* '"'
;
ERRO_CADEIA :
    '"' ~('"')* '\n'
;
ERRO_DESCONHECIDO :
    .
;