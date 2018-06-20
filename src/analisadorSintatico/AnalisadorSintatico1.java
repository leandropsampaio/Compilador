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
     * ************************* TERMINAR AS DECLARAÇÕES
     * ********************************
     * **********************************************************************************
     */
    public void declaracao() {
        declaracaoDeFuncao();

    }

    private void declaracaoDeFuncao() {
        if (validarToken("function")) {
            funcId();
            if (validarToken("(")) {
                funcaoProcedimentoFim();
            } else {
                panicMode();
            }
        }
    }

    private void funcId() {
        tipo();
        if (validarToken("identificador")) {

        } else {
            panicMode();
        }
    }

    private void panicMode() {
        System.out.println("Implementar modo pânico!!!!!");
    }

    private void tipo() {
        tipobase();
        tipoAux();
    }

    private void tipobase() {
        if (escalar()) {
        } else if (declaracaoDeStruct()) {

        } else if (validarToken("identificador")) {

        } else if (validarToken("struct")) {
            if (validarToken("identificador")) {
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
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

    private void Extends() {
        if (validarToken("extends")) {
            if (validarToken("identificador")) {

            } else {
                panicMode();
            }
        } // PODE SER VAZIO
    }

    private void declaracaoDeStructCorpo() {
        declaracaoDeStructLinha();
        declaracaoDeStructCorpoAux();
    }

    private void declaracaoDeStructLinha() {
        tipo();
        expressaoIdentificadoresStruct();
    }

    private void expressaoIdentificadoresStruct() {
        expressaoIdentificadorStruct();
        expressaoIdentificadoresStructAux();
    }

    private void expressaoIdentificadorStruct() {
        if (validarToken("identificador")) {

        } else {
            panicMode();
        }
    }

    private void expressaoIdentificadoresStructAux() {
        if (validarToken(";")) {

        }
        if (validarToken(",")) {
            expressaoIdentificadoresStruct();
        }
    }

    /*
        TERMINAR!!!!!!!!
     */
    private void tipoAux() {
        if (tipoVetorDeclarando()) {

        }
        // PODE SER VAZIO
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

    private void declaracaoDeStructCorpoAux() {
        declaracaoDeStructCorpo();
    }

    /**
     * ***********************************************************************
     */
    private void funcaoProcedimentoFim() {
        parametros();
        if (validarToken(")")) {
            bloco();
        }
    }

    private void parametros() {
        parametro();
        parametrosAux();
    }

    private void bloco() {
        if (validarToken("{")) {
            blocoAux();
        }
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

    private void blocoAux() {
        if (listaDeInstrucoes()) {
            if (validarToken("}")) {

            }
        }
        if (validarToken("}")) {

        }
    }

    private boolean listaDeInstrucoes() {
        /* CORRIGIR !!!!!!!!!!!!!!!!!!!!!!!!!!!! */
        return false;
    }



}
