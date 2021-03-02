# Trabalho 1
**Aluno: Marco Antônio Bernardi Grivol**\
**RA: 758619**

Analisador léxico para a linguagem LA é um programa que recebe como entrada um arquivo contendo um algoritmo escrito na linguagem LA e retorna os tokens gerados.

## Instalação
Os seguintes programas devem ser instalados:
1. Java [openjdk 11.0.10](https://openjdk.java.net/) ou equivalente.
2. [Apache Maven 3.6.3](https://maven.apache.org/) ou equivalente.
3. [Apache NetBeans IDE 12.2](https://netbeans.apache.org/) ou equivalente.

Acesse o sites e siga as instruções de instalação. No caso do SO Ubuntu 20.04, verifique a instalação digitando os seguintes comandos:\
``
java --version
``\
``
mvn --version
``
### Repositório
Clone este repósitorio em um local de fácil acesso para facilitar a execução: ``git clone https://github.com/marcogrivol/compiladores``

### Build
Abra o NetBeans, selecione **File → Open Project**, acesse o diretório com o clone do repositório, selecione o diretório **Trabalho1** e abra o projeto.
Na barra de ferramentas da IDE, selecione **Clean and Build Project**.

## Execução
Após realizada a construção (build) do projeto, acesse o terminal e digite: \
``java -jar LOCAL_CLONE/compiladores/Trabalho1/target/Trabalho1-1.0-SNAPSHOT-jar-with-dependencies.jar ARQUIVO_ENTRADA ARQUIVO_SAIDA``
* LOCAL_CLONE: é o caminho até o diretório contendo o repositório da etapa anterior.
* ARQUIVO_ENTRADA: é o caminho até o arquivo .txt contendo a linguagem LA que será realizada a análise léxica.
* ARQUIVO_SAIDA: é o caminho onde será gerada a saída com os tokens gerados.
