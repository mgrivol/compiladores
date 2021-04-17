# Trabalho 4 - Linguagem TGEN
**Aluno: Marco Antônio Bernardi Grivol**\
**RA: 758619**

A linguagem TGEN (Tower Defense Generator) busca facilitar a criação de cenários para um jogo de Tower Defense. Este documento apresenta a linguagem, seu compilador, casos de teste e como utilizá-la.

## Sumário
1. [TGEN](#tgen)
2. [Instalação de Programas](#instalação-de-programas)
2. [Uso da Linguagem](#uso-da-linguagem)
3. [Compilador](#compilador)
4. [Corretor](#corretor)
5. [Vídeo Demonstrativo](#vídeo-demonstrativo)

## TGEN
Um jogo de Tower Defense (TD) consiste em defender um ponto de ondas de inimigos. Quando um inimigo chega ao ponto o jogador perde vida, quando  sua vida chega a zero o jogo termina. Para defender o ponto o jogador deve colocar torres que atiram diversos projéteis nos inimigos levando em consideração uma estratégia para matar todos os inimigos antes de perder o jogo.


Um cenário de um jogo de TD consiste em definir ondas de inimigos, cada onda é composta por um ou mais tipos de inimigos. Dessa forma, a linguagem TGEN consiste em definir inimigos e o conteúdo de cada onda.

Um **inimigo** possui os seguintes atributos:
* Força: entre 1 e 100, padrão = 10
* Modelo: "PEQUENO", "MEDIO" ou "GRANDE", padrão = "MEDIO"
* Vida: entre 10.0 e 1000.0, padrão = 100.0
* Velocidade: entre 0.1 e 4.0, padrão = 1.0

Uma **onda** é composta por:
* Comando para nascer N inimigos. Podendo definir um tempo em segundos entre cada inimigo.
* Comando para aguardar N segundos.

Um arquivo da linguagem TGEN possui a seguinte estrutura:
``` 
% isto é um comentário %
inimigos { 
    IDENT {					% IDENT := (uma ou mais letras do alfabeto) %
    	ATRIBUTOS
    }
}
start
    onda {
    	COMANDOS
    }
end
```

### Inimigos
A linguagem permite inúmeros inimigos, cada um deve possuir um nome único definido por `IDENT`. Um inimigo pode ou não conter os atributos mencionados na seção anterior, os atributos podem ser declarados da seguinte forma:
```
forca = INT;            % INT    := valores naturais                       %
modelo = CADEIA;	% CADEIA := qualquer coisa entre aspas "cadeia"    %
vida = FLOAT;	        % FLOAT  := valores reais                          %
velocidade = FLOAT;
```
Um inimigo pode ou não possuir atributos, caso um atributo não seja declarado, o valor padrão da será selecionado.

### Ondas
Ondas são definidas entre `start` e `end`. Cada onda deve possuir pelo menos um dos três comandos:
```
IDENT(INT);	     % Nascer N inimigos do tipo IDENT. N := INT                                           %
IDENT(INT, FLOAT);   % Nascer (1 inimigo do tipo IDENT a cada X segundos) N vezes. N := INT e X := FLOAT   %
aguarde(FLOAT);      % Aguarde X segundos antes de realizar a próxima instrução. X := FLOAT                %
```

### Exemplo
```
inimigos {
    % definindo inimigos %
    A {
        vida = 100.0;
	forca = 10;
	velocidade = 2.0;
	modelo = "PEQUENO";
    }
    B {
        vida = 300.0;
	forca = 30;
	velocidade = 1.25;
        % modelo padrão será "MEDIO" %
    }
}
start
    % definindo ondas %
    onda {	            % primeira onda %
        aguarde(5.0);       % aguarde 10.0 antes da próxima instrução %
	A(10);              % nascer 10 inimigos do tipo A %
        aguarde(5.0);       
	B(2); 
	aguarde(5.0);
    }
    onda {		    % segunda onda %
	B(5, 1.5);          % nascer 5 inimigos do tipo B com intervalo de 1.5 %
	aguarde(4.0);
        A(10);
    }
end
```

## Instalação de Programas
### Repositório
Instale o [Git](https://git-scm.com/downloads) e clone este repositório em um local de fácil acesso com o comando: `git clone https://github.com/marcogrivol/compiladores`


### Java
Os seguintes programas devem ser instalados:
1. [Java JDK 11.0.10](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) ou equivalente.
3. [Apache NetBeans IDE 12.2](https://netbeans.apache.org/) ou equivalente.


Acesse os sites e siga as instruções de instalação. No caso do SO Windows 10, verifique a instalação digitando os seguintes comandos:\
``
java --version
``

### Unity
É necessário instalar [Unity 2019.4.20f1](https://unity3d.com/unity/qa/lts-releases).
Recomendo instalar [Unity Hub](https://unity3d.com/unity/qa/lts-releases) para facilitar a abertura e controle de versão da engine. Após a instalação, abra o Unity Hub, clique em ADD e selecione a pasta TowerDefenseGenerator no local de clone deste repositório.


## Uso da Linguagem
Esta linguagem pode, teoricamente, ser utilizada em qualquer jogo TD para gerar cernários, basta alterar o gerador de código. Para este trabalho foi escolhido um mini-jogo tutorial de Unity em C# para demonstrar o funcionamento. Disponível no site [Catlike Coding](https://catlikecoding.com/), o mini-jogo apresenta um cenário customizável pelo usuário e uma forma rápida de colocar a linguagem em funcionamento. O código presente neste trabalho foi adaptado do tutorial para poder ser usado com o gerador de código do compilador.

### Executar

Para gerar um cenário com TGEN, crie um arquivo txt e insira o código TGEN. Em seguida [execute o compilador](#compilador) com o parâmetro `ARQUIVO_SAIDA` no formato C# (.cs). Em seguida, copie o conteúdo do arquivo C# para: TowerDefenseGenerator > Assets > Scripts > Compiler e insira o conteúdo em GameScenarioCompiler.cs.

Após inserir o código gerado no arquivo, basta abrir o projeto com Unity e clicar em play para jogar.

## Compilador
O compilador pode ser utilizado com o arquivo jar providenciado ou compilado a partir da pasta [Compilador](https://github.com/MarcoGrivol/compiladores/tree/master/Trabalho4/Compilador). 

### Compilar
Abra o NetBeans, selecione **File → Open Project**, acesse o diretório com o clone do repositório, selecione o diretório **Trabalho4/Compilador** e abra o projeto.
Na barra de ferramentas da IDE, selecione **Clean and Build Project**.

### Executar
Ulize o arquivo "Compilador.jar" ou realize a compilação do projeto, acesse o terminal e digite: \
``java -jar LOCAL_CLONE/compiladores/Trabalho4/Compilador/target/Compilador-1.0-SNAPSHOT-jar-with-dependencies.jar ARQUIVO_ENTRADA ARQUIVO_SAIDA MODO``
* **LOCAL_CLONE**: é o caminho até o diretório contendo o repositório da etapa anterior.
* **ARQUIVO_ENTRADA**: é o caminho até o arquivo .txt contendo a linguagem TGEN que será compilado.
* **ARQUIVO_SAIDA**: é o caminho onde será gerada a saída com as mensagens de compilação ou código C#.
* **MODO** (opcional, padrão="gerador"): pode ser utilizado para selecionar o modo do compilador. Digite "sintatico" para realizar a análise léxica e sintática, "semantico" para realizar análise sintática e semântica, ou "gerador" para realizar a análise semântica e gerar o código caso não existam erros.

## Corretor
Um [corretor foi disponibilizado](https://github.com/MarcoGrivol/compiladores/blob/master/Trabalho4/corretor.jar) para facilitar a correção. Este corretor foi modificado de [dlucredio/compiladores-corretor-automatico](https://github.com/dlucredio/compiladores-corretor-automatico) para ser compatível com o código gerado e ignorar a análise léxica (que é feita automaticamente pelas outras etapas).

O corretor pode ser utilizado com os mesmos argumentos descritos nas instruções de uso de [dlucredio/compiladores-corretor-automatico](https://github.com/dlucredio/compiladores-corretor-automatico) com exceção do modo de análise léxica. Os arquivos de testes foram disponibilizados neste repositório.

O [código fonte do corretor](https://github.com/MarcoGrivol/compiladores/tree/master/Trabalho4/compiladores-corretor-automatico) está disponível neste repositório.

## Vídeo Demonstrativo
Youtube: https://youtu.be/mxaQLVeAE7s



