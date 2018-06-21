
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

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
    /**
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

    public boolean programa() {
        if (declaracao()) {
            if (programaAux()) {
                return true;
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
        return false;
    }

    public boolean programaAux() {
        if (programa()) {
            return true;
        }
        return true;
    }

    public boolean declaracao() {
        if (declaracaoDeFuncao()) {

        }

    

    private boolean declaracaoDeFuncao() {
        if (validarToken("function")) {
            if (funcId()) {
                if (validarToken("(")) {

                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        } else {
            panicMode();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean tipo() {
        if (tipobase()) {
            if (tipoAux()) {

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
            }
        }
        return false;
    }

    private boolean escalar() {
        if (validarToken("int") || validarToken("float") || validarToken("bool") || validarToken("string")) {
            return true;
        } else {
            panicMode();
        }
        return false;
    }

    private boolean declaracaoDeStruct() {
        if (validarToken("struct")) {
            if (declaracaoDeStructAux()) {

            }
        }

        return false;
    }

    private boolean declaracaoDeStructAux() {
        if (validarToken("identificador")) {
            if (Extends()) {
                if (validarToken("{")) {
                    if (declaracaoDeStructCorpo()) {
                        if (validarToken("}")) {
                            return true;
                        }
                    }
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
        }
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

            }
        }

        return false;
    }

    private boolean declaracaoDeStructCorpoAux() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean expressaoIdentificadoresStruct() {
        if (expressaoIdentificadorStruct()) {
            if(expressaoIdentificadoresStructAux()){
                return true;
            }
        }
        return false;
    }

    private boolean expressaoIdentificadorStruct() {
        if (validarToken("identificador")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean expressaoIdentificadoresStructAux() {
        
        
        return false;
    }

}
