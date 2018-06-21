/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisadorSintatico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Leandro Sampaio e Elvis Huges
 */
public class AnalisadorSintatico1 {

    private Token tokenAtual, tokenAnterior;
    private int posicao = -1;
    private ArrayList<Token> tokens;
    private BufferedWriter saidaSintatico;
    private int errosSintaticos = 0;
    private int contador = 0;

    /**
     * Método que inicia a análise sintática.
     *
     * @param tokens lista de tokens extraídos da análise léxica
     * @param file diretório para armazenar os resultados
     */
    public void iniciar(ArrayList tokens, String nomeArquivo) throws IOException {
        FileWriter saidaSintatico = new FileWriter("entrada\\saidaSintatico\\saida-" + nomeArquivo + ".txt");
        try {

            saidaSintatico.write("Análise Sintática iniciada para o arquivo " + nomeArquivo);
            saidaSintatico.append("/n");
            System.out.println("Análise Sintática iniciada para o arquivo " + nomeArquivo);
            this.tokens = tokens;
            proximoToken();
            Iterator iterador = this.tokens.listIterator();
            while (iterador.hasNext()) {
                Token token = (Token) iterador.next();
                System.out.println(token.getNome());
            }

            programa();
            if (errosSintaticos == 0) { // adicionar verificador de main !!!
                System.out.println("Análise Sintática finalizada com sucesso para o arquivo " + nomeArquivo);
                saidaSintatico.write("Análise Sintática finalizada com sucesso para o arquivo " + nomeArquivo);
            } else {
                System.out.println("Análise Sintática finalizada com erro para o arquivo " + nomeArquivo);
                saidaSintatico.write("Análise Sintática finalizada com erro para o arquivo " + nomeArquivo);
            }
            saidaSintatico.close();

        } catch (IOException ex) {
            Logger.getLogger(AnalisadorSintatico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
        Caso tenha '|' na grámatica seria if - if ...
        Caso seja direto na grámatica seria if - else if ...
     */
    private boolean proximoToken() {
        if (posicao + 1 < tokens.size()) {
            posicao++;
            tokenAnterior = tokenAtual;
            tokenAtual = tokens.get(posicao);
            // Pulando comentários de linha e bloco
            //validarToken("Comentário de Linha");
            //validarToken("Comentário de Bloco");
            return true;
        }
        return false;
    }

    private boolean tokenAnterior() {
        for (int i = 0; i < contador; i++) {
            if (posicao - 1 < tokens.size()) {
                posicao--;
                tokenAnterior = tokenAtual;
                tokenAtual = tokens.get(posicao);
                // Pulando comentários de linha e bloco
                //validarToken("Comentário de Linha");
                //validarToken("Comentário de Bloco");
                return true;
            }
        }
        this.contador = 0;
        return false;
    }

    private boolean validarToken(String tipo) {
        //System.out.println("TESTE!");
        if (tokenAtual.getTipo().equals(tipo) || tokenAtual.getNome().equals(tipo)) {
            System.out.println(tokenAtual);
            proximoToken();
            return true;
        }
        //System.out.println("VALIDANDO TOKEN: " + tipo);
        return false;
    }

    private Token showProx() {
        if (posicao + 1 < tokens.size()) {
            return tokens.get(posicao + 1);
        }
        return null;
    }

    public boolean programa() {
        if (declaracao()) {
            if (programaAux()) {
                return true;
            }
        }
        return false;
    }

    public boolean programaAux() {
        if (programa()) {
            return true;
        }
        return true;
    }

    /**
     * **********************************************************************************
     * ************************* TERMINAR AS DECLARAÇÕES E VERIFICAR OS QUE
     * ********************* POSSUEM VAZIO PARA ADICIONAR O ELSE(RETURN FALSE)
     * **********************************************************************************
     */
    public boolean declaracao() {
        if (declaracaoDeFuncao()) {
            return true;
        } else if (declaracaoDeProcedimento()) {
            return true;
        } else if (declaracaoDeInicio()) {
            return true;
        } else if (declaracaoDeVar()) {
            return true;
        } else if (declaracaoDeConst()) {
            return true;
        } else if (declaracaoDeStruct()) {
            return true;
        } else if (declaracaoDeTypedef()) {
            return true;
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return false;

    }

    private boolean declaracaoDeFuncao() {
        if (validarToken("function")) {
            funcId();
            if (validarToken("(")) {
                funcaoProcedimentoFim();
            } else {
                panicMode();
            }
        }
        return false;
    }

    private boolean declaracaoDeProcedimento() {
        if (validarToken("procedure")) {
            System.out.println("1");
            if (validarToken("IDE")) {
                System.out.println("17");
                if (validarToken("(")) {
                    funcaoProcedimentoFim();
                } else {
                    panicMode();
                }
            }

        }
        return false;
    }

    private boolean declaracaoDeInicio() {
        if (validarToken("start")) {
            if (validarToken("(")) {
                if (validarToken(")")) {
                    bloco(); //verificar !!!!!!
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }

        }
        return false;
    }

    private boolean declaracaoDeVar() {
        if (validarToken("var")) {
            if (validarToken("{")) {
                if (declaracaoDeVariavelCorpo()) {
                    if (validarToken("}")) {
                        return true;
                    } //verificar !!!!!!
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        }
        return false;
    }

    private boolean declaracaoDeConst() {
        if (validarToken("const")) {
            if (validarToken("{")) {
                if (declaracaoDeConstanteCorpo()) {
                    if (validarToken("}")) {
                        return true;
                    } //verificar !!!!!!
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        }
        return false;
    }

    private boolean funcId() {
        if (tipo()) {
            if (validarToken("IDE")) {
                System.out.println("2");
                return true;
            }
        }
        return false;
    }

    private void panicMode() {
        System.out.println("Implementar modo pânico!!!!!");
    }

    private boolean tipo() {
        if (tipobase()) {
            if (tipoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean tipobase() {
        if (escalar()) {
            return true;
        } else if (declaracaoDeStruct()) {
            return true;
        } else if (validarToken("IDE")) {
            System.out.println("3");
            return true;
        } else if (validarToken("struct")) {
            if (validarToken("IDE")) {
                System.out.println("4");
                return true;
            } else {
                panicMode();
            }
        }
        return false;
    }

    private boolean escalar() {
        if (validarToken("int") || validarToken("float") || validarToken("bool") || validarToken("string")) {
            return true;
        }
        return false;
    }

    private boolean declaracaoDeStruct() {
        if (validarToken("struct")) {
            declaracaoDeStructAux();
        }
        return false;
    }

    private boolean declaracaoDeStructAux() {
        if (validarToken("IDE")) {
            System.out.println("5");
            if (Extends()) { // LEMBRARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
                if (validarToken("{")) {
                    declaracaoDeStructCorpo();
                    if (validarToken("}")) {
                        return true;
                    }
                }
            }
        } else if (Extends()) {
            if (validarToken("{")) {
                declaracaoDeStructCorpo();
                if (validarToken("}")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean Extends() {
        if (validarToken("extends")) {
            if (validarToken("IDE")) {
                System.out.println("6");
                return true;
            } else {
                panicMode();
            }
        } // PODE SER VAZIO        
        return true;
    }

    private boolean declaracaoDeStructCorpo() {
        if (declaracaoDeStructLinha()) {
            if (declaracaoDeStructCorpoAux()) {
                return true;
            }
        }
        return false;

    }

    private boolean declaracaoDeStructLinha() {
        if (tipo()) {
            if (expressaoIdentificadoresStruct()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressaoIdentificadoresStruct() {
        if (expressaoIdentificadorStruct()) {
            if (expressaoIdentificadoresStructAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressaoIdentificadorStruct() {
        if (validarToken("IDE")) {
            System.out.println("7");
            return true;
        }
        return false;
    }

    private boolean expressaoIdentificadoresStructAux() {
        if (validarToken(";")) {
            return true;
        } else if (validarToken(",")) {
            if (expressaoIdentificadoresStruct()) {
                return true;
            }
        }
        return false;
    }

    /*
        TERMINAR!!!!!!!!
     */
    private boolean tipoAux() {
        if (tipoVetorDeclarando()) {
            return true;
        }
        return true;
    }

    private boolean tipoVetorDeclarando() {
        if (tipoVetorDeclarado()) {
            if (tipoVetorDeclarandoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean tipoVetorDeclarado() {
        if (validarToken("[")) {
            if (validarToken("]")) {
                return true;
            } else {
                panicMode();
            }
        }
        return false;
    }

    private boolean tipoVetorDeclarandoAux() {
        if (tipoVetorDeclarando()) {
            return true;
        }
        return false;
    }

    private boolean declaracaoDeStructCorpoAux() {
        if (declaracaoDeStructCorpo()) {
            return true;
        }
        return true;
    }

    /**
     * ***********************************************************************
     */
    private boolean funcaoProcedimentoFim() {
        System.out.println("CCCCCCCCCCCCCC");
        if (parametros()) {
            System.out.println("BBBBBBBBBBBBBBBBBBb");
            if (validarToken(")")) {
                if (bloco()) {
                    return true;
                }
            }
        } else if (validarToken(")")) {
            System.out.println("AAAAAAAAAAAAAA");
            if (bloco()) {
                return true;
            }
        }
        return false;
    }

    private boolean parametros() {
        if (parametro()) {
            if (parametrosAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean bloco() {
        System.out.println("1 - Bloco");
        if (validarToken("{")) {
            if (blocoAux()) {
                System.out.println("2");
                return true;
            }
        }
        System.out.println("BLOCO SAIU!");
        return false;
    }

    private boolean parametro() {
        if (tipo()) {
            if (validarToken("IDE")) {
                System.out.println("8");
                return true;
            }
        }
        return false;
    }

    private boolean parametrosAux() {
        if (validarToken(",")) {
            if (parametros()) {
                return true;
            }
        }
        return true;
    }

    private boolean blocoAux() {
        System.out.println("blocoAux");
        if (listaDeInstrucoes()) {
            if (validarToken("}")) {
                return true;
            }
        } else if (validarToken("}")) {
            return true;
        }
        System.out.println("blocoAux SAIU");
        return false;
    }

    private boolean listaDeInstrucoes() {
        if (instrucao()) {
            if (listaDeInstrucoesAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean instrucao() {
        if (instrucaoNormal()) {
            return true;
        } else if (estruturaCondicional()) {
            return true;
        } else if (While()) {
            return true;
        } else if (declaracaoDeVar()) {
            return true;
        } else if (declaracaoDeConst()) {
            return true;
        } else if (declaracaoDeTypedef()) {
            return true;
        }
        return false;
    }

    private boolean listaDeInstrucoesAux() {
        if (listaDeInstrucoes()) {
            return true;
        }
        // Pode ser Vazio
        return true;
    }

    private boolean instrucaoNormal() {
        if (operacaoDeAtribuicao()) {
            if (validarToken(";")) {
                return true;
            } else {
                panicMode();
            }
        } else if (declaracaoDeStruct()) {
            if (validarToken(";")) {
                return true;
            } else {
                panicMode();
            }
        } else if (instrucaoDeRetorno()) {
            if (validarToken(";")) {
                return true;
            } else {
                panicMode();
            }
        } else if (Print()) {
            if (validarToken(";")) {
                return true;
            } else {
                panicMode();
            }
        } else if (scan()) {
            if (validarToken(";")) {
                return true;
            } else {
                panicMode();
            }
        }
        return false;
    }

    /*TRÊS PRIMEIROS COMO IDENTIFICADOR*/
    private boolean operacaoDeAtribuicao() {
        System.out.println("OPERACAO DE ATRIBUICAO");
        if (expressao()) {
            System.out.println("EXPRESSAO!");
            return true;
        } else if (Final()) {
            System.out.println("FINAL!");
            if (validarToken("=")) {
                if (expressao()) {
                    return true;
                }
            }
        } else if (validarToken("IDE")) {
            System.out.println("9");
            if (validarToken("=")) {
                if (expressao()) {
                    return true;
                }
            }
        }
        System.out.println("SAIU DE OPERACAO DE ATRIBUICAO");
        return false;
    }

    private boolean instrucaoDeRetorno() {
        if (validarToken("return")) {
            if (instrucaoDeRetornoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean Print() {
        if (validarToken("print")) {
            if (validarToken("(")) {
                if (saida()) {
                    if (outrasSaidas()) {
                        if (validarToken(")")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean scan() {
        if (validarToken("scan")) {
            if (validarToken("(")) {
                if (entrada()) {
                    if (outrasEntradas()) {
                        if (validarToken(")")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean estruturaCondicional() {
        if (ifThen()) {
            if (estruturaCondicionalAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean While() {
        if (validarToken("while")) {
            if (validarToken("(")) {
                if (expressao()) {
                    if (validarToken(")")) {
                        if (bloco()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean declaracaoDeTypedef() {
        if (validarToken("typedef")) {
            if (declaracaoDeTypedefAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean Final() {
        if (validarToken("IDE")) {
            System.out.println("10");
            if (acessando()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressao() {
        if (opE()) {
            if (expressaoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean instrucaoDeRetornoAux() {
        if (expressao()) {
            return true;
        }
        return true;
    }

    private boolean saida() {
        if (expressao()) {
            return true;
        }
        return false;
    }

    private boolean outrasSaidas() {
        if (validarToken(",")) {
            if (saida()) {
                if (outrasSaidas()) {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean entrada() {
        if (Final()) {
            return true;
        } else if (validarToken("IDE")) {
            System.out.println("11");
            return true;
        }
        return false;
    }

    private boolean outrasEntradas() {
        if (validarToken(",")) {
            if (entrada()) {
                if (outrasEntradas()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean ifThen() {
        if (validarToken("if")) {
            if (validarToken("(")) {
                if (expressao()) {
                    if (validarToken(")")) {
                        if (validarToken("then")) {
                            if (bloco()) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean estruturaCondicionalAux() {
        if (validarToken("else")) {
            if (bloco()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean declaracaoDeVariavelCorpo() {
        if (declaracaoDeVariavelLinha()) {
            if (declaracaoDeVariavelCorpoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean declaracaoDeVariavelLinha() {
        if (tipo()) {
            if (expressaoIdentificadoresVar()) {
                return true;
            }
        }
        return false;
    }

    private boolean declaracaoDeVariavelCorpoAux() {
        if (declaracaoDeVariavelCorpo()) {
            return true;
        }
        return true;
    }

    private boolean expressaoIdentificadoresVar() {
        if (expressaoIdentificadorVar()) {
            if (expressaoIdentificadoresVarAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressaoIdentificadorVar() {
        if (validarToken("IDE")) {
            System.out.println("12");
            if (expressaoIdentificadorVarAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressaoIdentificadoresVarAux() {
        if (validarToken(";")) {
            return true;
        } else if (validarToken(",")) {
            if (expressaoIdentificadoresVar()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressaoIdentificadorVarAux() {
        if (validarToken("=")) {
            if (expressao()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean declaracaoDeConstanteCorpo() {
        if (declaracaoDeConstanteLinha()) {
            if (declaracaoDeConstanteCorpoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean declaracaoDeConstanteLinha() {
        if (tipo()) {
            if (expressaoIdentificadoresConst()) {
                return true;
            }
        }
        return false;
    }

    private boolean declaracaoDeConstanteCorpoAux() {
        if (declaracaoDeConstanteCorpo()) {
            return true;
        }
        return true;
    }

    private boolean expressaoIdentificadoresConst() {
        if (expressaoIdentificadorConst()) {
            if (expressaoIdentificadoresConstAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressaoIdentificadorConst() {
        if (validarToken("IDE")) {
            System.out.println("13");
            if (validarToken("=")) {
                if (expressao()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean expressaoIdentificadoresConstAux() {
        if (validarToken(";")) {
            return true;
        } else if (validarToken(",")) {
            if (expressaoIdentificadoresConst()) {
                return true;
            }
        }
        return false;
    }

    private boolean declaracaoDeTypedefAux() {
        if (tipo()) {
            if (validarToken("IDE")) {
                System.out.println("14");
                if (validarToken(";")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean acessando() {
        if (acesso()) {
            if (acessandoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean acesso() {
        if (validarToken(".")) {
            if (validarToken("IDE")) {
                System.out.println("15");
                return true;
            }
        } else if (validarToken("[")) {
            if (expressao()) {
                if (validarToken("]")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean acessandoAux() {
        if (acessando()) {
            return true;
        }
        return true;
    }

    private boolean opE() {
        if (opRelacional()) {
            if (opEAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean expressaoAux() {
        if (validarToken("||")) {
            if (expressao()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean opRelacional() {
        if (valorRelacional()) {
            if (opRelacionalAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean opEAux() {
        if (validarToken("&&")) {
            if (opE()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean valorRelacional() {
        if (opMult()) {
            if (valorRelacionalAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean opRelacionalAux() {
        if (escalarRelacional()) {
            if (opRelacional()) {
                return true;
            }
        }
        return true;
    }

    private boolean opMult() {
        if (opUnary()) {
            if (opMultAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean valorRelacionalAux() {
        if (validarToken("+")) {
            if (opMult()) {
                if (valorRelacionalAux()) {
                    return true;
                }
            }
        } else if (validarToken("-")) {
            if (opMult()) {
                if (valorRelacionalAux()) {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean escalarRelacional() {
        if (validarToken("!=")) {
            return true;
        } else if (validarToken("==")) {
            return true;
        } else if (validarToken("<")) {
            return true;
        } else if (validarToken("<=")) {
            return true;
        } else if (validarToken(">")) {
            return true;
        } else if (validarToken(">=")) {
            return true;
        }
        return false;
    }

    private boolean opUnary() {
        if (validarToken("!")) {
            if (opUnary()) {
                return true;
            }
        } else if (validarToken("++")) {
            if (opUnary()) {
                return true;
            }
        } else if (validarToken("--")) {
            if (opUnary()) {
                return true;
            }
        } else if (Final()) {
            if (simboloUnario()) {
                return true;
            }
        } else if (valor()) {
            if (simboloUnario()) {
                return true;
            }
        }
        return false;
    }

    private boolean opMultAux() {
        if (validarToken("*")) {
            if (opUnary()) {
                if (opMultAux()) {
                    return true;
                }
            }
        } else if (validarToken("/")) {
            if (opUnary()) {
                if (opMultAux()) {
                    return true;
                }
            }
        }
        return true;
    }

    private boolean simboloUnario() {
        if (validarToken("++")) {
            return true;
        } else if (validarToken("--")) {
            return true;
        }
        return true;
    }

    private boolean valor() {
        System.out.println("VALOR!!!");
        if (validarToken("IDE")) {
            System.out.println("16");
            if (valorAux1()) {
                return true;
            }
        } else if (validarToken("(")) {
            if (expressao()) {
                if (validarToken(")")) {
                    return true;
                }
            }
        } else if (validarToken("NRO")) {
            return true;
        } else if (validarToken("CAD")) {
            return true;
        } else if (validarToken("true")) {
            return true;
        } else if (validarToken("false")) {
            return true;
        }
        return false;
    }

    private boolean valorAux1() {
        System.out.println("111111111111111111111111");
        if (validarToken("(")) {
            if (valorAux2()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean valorAux2() {
        System.out.println("222222222222222222222222");
        if (parametrosFuncao()) {
            if (validarToken(")")) {
                return true;
            }
        } else if (validarToken(")")) {
            return true;
        }
        return false;
    }

    private boolean parametrosFuncao() {
        if (expressao()) {
            if (parametrosFuncaoAux()) {
                return true;
            }
        }
        return false;
    }

    private boolean parametrosFuncaoAux() {
        if (validarToken(",")) {
            if (parametrosFuncao()) {
                return true;
            } else {
                panicMode();
            }
        }
        return true;
    }

}
