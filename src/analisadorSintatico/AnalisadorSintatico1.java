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

    /**
     * Método que inicia a análise sintática.
     *
     * @param tokens lista de tokens extraídos da análise léxica
     * @param file diretório para armazenar os resultados
     */
    public void iniciar(ArrayList tokens) {
        File file = null;
        FileWriter output;
        try {
            output = new FileWriter(new File(file.getParent(), "output_sin_" + file.getName()));
            saidaSintatico = new BufferedWriter(output);
            saidaSintatico.write("Análise Sintática iniciada para o arquivo " + file.getName());
            saidaSintatico.newLine();
            System.out.println("Análise Sintática iniciada para o arquivo " + file.getName());
            this.tokens = tokens;
            programa();
            if (errosSintaticos == 0) { // adicionar verificador de main !!!
                System.out.println("Análise Sintática finalizada com sucesso para o arquivo " + file.getName());
                saidaSintatico.write("Análise Sintática finalizada com sucesso para o arquivo " + file.getName());
            } else {
                System.out.println("Análise Sintática finalizada com erro para o arquivo " + file.getName());
                saidaSintatico.write("Análise Sintática finalizada com erro para o arquivo " + file.getName());
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
            validarToken("Comentário de Linha");
            validarToken("Comentário de Bloco");
            return true;
        }
        return false;
    }

    private boolean validarToken(String tipo) {
        if (tokenAtual.getTipo().equals(tipo) || tokenAtual.getNome().equals(tipo)) {
            System.out.println(tokenAtual);
            proximoToken();
            return true;
        }
        return false;
    }

    private Token showProx() {
        if (posicao + 1 < tokens.size()) {
            return tokens.get(posicao + 1);
        }
        return null;
    }

    public void programa() {
        declaracao();
        programaAux();
    }

    public void programaAux() {
        programa();
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
        return true;

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
            if (validarToken("identificador")) {
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
                if (validarToken("(")) {
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
            if (validarToken("identificador")) {
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
        } else if (validarToken("identificador")) {
            return true;
        } else if (validarToken("struct")) {
            if (validarToken("identificador")) {
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
        if (validarToken("identificador")) {
            Extends();
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
            if (validarToken("identificador")) {
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
        if (validarToken("identificador")) {
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
        if (parametros()) {
            if (validarToken(")")) {
                if (bloco()) {
                    return true;
                }
            }
        } else if (validarToken(")")) {
            if (bloco()) {
                return true;
            }
        }
        return false;
    }

    private void parametros() {
        parametro();
        parametrosAux();
    }

    private boolean bloco() {
        if (validarToken("{")) {
            if (blocoAux()) {
                return true;
            }
        }
        return false;
    }

    private void parametro() {
        tipo();
        if (validarToken("identificador")) {

        } else {
            panicMode();
        }
    }

    private void parametrosAux() {
        if (validarToken(",")) {
            parametros();
        } else {
            // Vazio
        }
    }

    private boolean blocoAux() {
        if (listaDeInstrucoes()) {
            if (validarToken("}")) {
                return true;
            }
        } else if (validarToken("}")) {
            return true;
        }
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

    private boolean operacaoDeAtribuicao() {
        if (Final()) {
            if (validarToken("=")) {
                if (expressao()) {
                    return true;
                }
            }
        } else if (validarToken("identificador")) {
            if (validarToken("=")) {
                if (expressao()) {
                    return true;
                }
            }
        } else if (expressao()) {
            return true;
        }
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
        if (validarToken("identificador")) {
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
        } else if (validarToken("identificador")) {
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
        if (validarToken("identificador")) {
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
        if (validarToken("identificador")) {
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
            if (validarToken("identificador")) {
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
            if (validarToken("identificador")) {
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
<<<<<<< HEAD

=======
>>>>>>> 0f9b4fae3d897b80682eff2ece3e09c9f8a6221d
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
        if (validarToken("identificador")) {
            if (valorAux1()) {
                return true;
            }
        } else if (validarToken("(")) {
            if (expressao()) {
                if (validarToken(")")) {
                    return true;
                }
            }
        } else if (validarToken("digitos")) {
            return true;
        } else if (validarToken("cadeiaDeCaracteres")) {
            return true;
        } else if (validarToken("true")) {
            return true;
        } else if (validarToken("false")) {
            return true;
        }
        return false;
    }

    private boolean valorAux1() {
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
