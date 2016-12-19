# Krakatoa2C-Compiler

Este compilador gera código fonte em C, ou seja, o compilador irá gerar um código fonte em C a partir de um código fonte em Krakatoa (uma linguagem orientada a objetos, mais precisamente um subconjunto de Java).
Os dois artigos utilizados neste projeto se encontram no repositório:

1. [The Krakatoa Language] (https://github.com/igorracca/Krakatoa2C-Compiler/blob/master/The%20Krakatoa%20Language.pdf)
2. [Geracao de Codigo em C para Krakatoa] (https://github.com/igorracca/Krakatoa2C-Compiler/blob/master/Geracao-de-Codigo-em-C-para-Krakatoa.pdf)

## Descricao

**Segundo Trabalho de Laboratório de Compiladores**

A primeira fase do projeto se encontra [neste repositório] (https://github.com/igorracca/Krakatoa2Krakatoa).
A partir da primeira fase do trabalho, o compilador que contém os analisadores, léxicos, sintáticos e semânticos da linguagem Krakatoa, a ASA corretamente implementada e a geração de código em Krakatoa, faça a geração de código em C a partir de um ou mais arquivos em Krakatoa.

Utilize o artigo “Geração de Código em C para Krakatoa”. O código gerado em C deve estar corretamente identado. Pegue a última versão do compilador gcc, que será utilizado para compilar o código gerado em C.
Os programas em C resultantes da compilação Krakatoa-C deverão ser compilados no gcc e exibir o resultado exatamente igual ao resultado esperado que sera printado também na saída.
Os arquivos .kra para teste estão na pasta KraFiles.
  
-----------

## Execucao

A maneira mais fácil de executar este projeto é importá-lo como um projeto existente no Eclipse e rodá-lo passando um diretório com os arquivos .kra como parâmetro.
Você pode fazer isso da seguinte maneira:
RUN > RUN CONFIGURATIONS > ARGUMENTS > PROGRAM ARGUMENTS
Existe um diretório chamado KraFiles que contém arquivos .kra para teste. 
  
  ou
  
Note que o compilador fornecido, ao ser chamado passando um diretório como parâmetro, compila todos os arquivos “*.kra” daquele diretório e produz um relatório de erros. Assim, ao chamar o compilador,
            C:\Dropbox\16-2\LC\krakatoa\bin>java -cp . comp.Comp "C:\Dropbox\16-2\LC\tests"
este criará, no diretório corrente, os arquivos “*.c” correspondentes ao código gerado em Krakatoa
 


