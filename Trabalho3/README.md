# Trabalho 3
**Aluno: Marco Antônio Bernardi Grivol**\
**RA: 758619**

Analisador sintático para a linguagem LA é um programa que recebe como entrada um arquivo contendo um algoritmo escrito na linguagem LA e retorna qualquer erro sintático existente.

## Instalação
Os seguintes programas devem ser instalados:
1. Java [openjdk 11.0.10](https://openjdk.java.net/) ou equivalente.
3. [Apache NetBeans IDE 12.2](https://netbeans.apache.org/) ou equivalente.

Acesse o sites e siga as instruções de instalação. No caso do SO Windows 10, verifique a instalação digitando os seguintes comandos:\
``
java --version
``
### Repositório
Clone este repósitorio em um local de fácil acesso para facilitar a execução: ``git clone https://github.com/marcogrivol/compiladores``

### Compilar
Abra o NetBeans, selecione **File → Open Project**, acesse o diretório com o clone do repositório, selecione o diretório **Trabalho3** e abra o projeto.
Na barra de ferramentas da IDE, selecione **Clean and Build Project**.

## Execução
Ulize o arquivo "Trabalho3-1.0-SNAPSHOT-jar-with-dependencies.jar" ou realize a compilação do projeto, acesse o terminal e digite: \
``java -jar LOCAL_CLONE/compiladores/Trabalho3/target/Trabalho3-1.0-SNAPSHOT-jar-with-dependencies.jar ARQUIVO_ENTRADA ARQUIVO_SAIDA``
* LOCAL_CLONE: é o caminho até o diretório contendo o repositório da etapa anterior.
* ARQUIVO_ENTRADA: é o caminho até o arquivo .txt contendo a linguagem LA que será realizada a análise léxica.
* ARQUIVO_SAIDA: é o caminho onde será gerada a saída com as mensagens de compilação.
