// Aluno: Marco Antônio Bernardi Grivol
// RA:    758619

// lexemas da linguagem TGEN
grammar TGEN;

WS : // espaços em branco 
    ( ' ' | '\t' | '\r' | '\n' | '\r\n' ) -> skip
;

programa : 
    // um programa é formado por inimigos e ondas
    inimigos 'start' ondas 'end'
;
inimigos : 
    // inimigos são formados pela declaração e um ou mais inimigos
    'inimigos' '{' ( inimigo )+ '}'
;
inimigo : 
    // inimigos são formados por um identificador e possíveis parâmetros
    // caso nenhum parâmetro seja providenciado, o inimigo irá possuir valores padrões pré-definidos
    IDENT '{' ( parametroInimigo ';' )* '}'
;
parametroInimigo : 
    // existem quatro tipos de parâmetros
    parInimigoForca
    | parInimigoVelocidade
    | parInimigoVida
    | parInimigoModelo
;
parInimigoForca : 
    // a força de um inimigo é definida por um valor inteiro
    'forca' '=' INT
;
parInimigoVelocidade :
    // a velocidade é definida por um valor flutuante
    'velocidade' '=' FLOAT
;
parInimigoVida :
    // a vida é definida por um valor flutuante
    'vida' '=' FLOAT 
;
parInimigoModelo :
    // o modelo é definida por uma cadeia de caracteres
    'modelo' '=' CADEIA
;
ondas :
    // ondas são definidas por uma ou mais onda
    ( onda )+
;
onda :
    // cada onda possuí a declaração e um ou mais comandos
    'onda' '{' ( comando ';' )+ '}'
;
comando :
    // existem dois tipos de comandos
    cmdSpawn
    | cmdAguarde
;
cmdSpawn :
    // comando para nascer inimigos
    semDelay
    | comDelay
;
semDelay :
    // comando para nascer inimigos sem espera
    IDENT '(' INT ')'
;
comDelay :
    // comandos para nascer inimigos com um intervalo entre cada um
    IDENT '(' INT ',' FLOAT ')' 
;
cmdAguarde :
    // comando para aguarde a quantidade de segundos
    'aguarde' '(' FLOAT ')' 
;
INT :
    // definição de número natural
    ( '0'..'9' )+
;
FLOAT :
    // definição de número real positivo
    ( '0'..'9' )+ '.' ( '0'..'9' )+
;
IDENT :
    // definição de um identificador
    ( 'a'..'z' | 'A'..'Z' ) ( 'a'..'z' | 'A'..'Z' )*
;
COMENTARIO :
    // definição de um comentário
    '%' ~( '\n' | '%' )* '%' -> skip
;
ERRO_COMENTARIO :
    // definição de um comentário com erro
    '%' ~( '%' )* '\n'
;
CADEIA :
    // definição de uma cadeia de caracteres
    '"' ~('"' | '\n')* '"'
;
ERRO_CADEIA :
    // definição de uma cadeia não fechada
    '"' ~('"')* '\n'
;
ERRO_DESCONHECIDO :
    // definição de símbolo desconhecido
    .
;