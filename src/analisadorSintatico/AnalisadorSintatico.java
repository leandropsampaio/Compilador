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
public class AnalisadorSintatico {

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
            inicio();
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

    private void inicio() {
        proximoToken();
        variavelConstanteObjeto(); // parece ser declaração
        classe();
    }

    private void classe() {
        if (validarToken("function")) {
            if (validarToken("Identificador")) {
                //herancaNao();
                if (validarToken("{")) {
                    variavelConstanteObjeto();
                    //metodo();
                    if (validarToken("}")) {
                        classe();
                    } else {
                        panicMode();
                    }
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        }
    }

    private void start() {
        if (validarToken("(")) {
            if (validarToken(")")) {
                if (validarToken("{")) {
                    program();
                    if (validarToken("}")) {
                        variavelConstanteObjeto();
                    } else {
                        panicMode();
                    }
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
    }

    private void variavelConstanteObjeto() {
        
        
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

    // adicionar o restante dos tipos
    private boolean tipo() {
        if (validarToken("float") || validarToken("int") || validarToken("string") || validarToken("bool")) {
            return true;
        }
        return false;
    }

    private void program() {
        if (validarToken("var")) {
            varConsumido();
        } else if (validarToken("struct")) {
            structConsumido();
        } else if (validarToken("const")) {
            constConsumido();
        } else if (validarToken("if")) {
            ifConsumido();
        } else if (validarToken("scan")) {
            scanConsumido();
        } else if (validarToken("print")) {
            printConsumido();
        } else if (validarToken("while")) {
            whileConsumido();
        } else if (validarToken("typedef")) {
            typedefConsumido();
        } else if (validarToken("return")) {
            returnConsumido();
        }
        program();
    }

    private void printConsumido() {
        if (validarToken("(")) {
            impressao();
            multiplasImpressoes();
            if (validarToken(")")) {
                if (validarToken(";")) {
                    program();
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
    }

    private void impressao() {

    }

    private void multiplasImpressoes() {
        if (validarToken(",")) {
            impressao();
            multiplasImpressoes();
        }
    }

    private void scanConsumido() {
        if (validarToken("(")) {
            if (validarToken("Identificador")) {
                multiplasLeituras();
                if (validarToken(")")) {
                    if (validarToken(";")) {
                        program();
                    } else {
                        panicMode();
                    }
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
    }

    private void multiplasLeituras() {
        if (validarToken(",")) {
            if (validarToken("Identificador")) {
                multiplasLeituras();
            } else {
                panicMode();
            }
        }
    }

    private void ifConsumido() {
        if (validarToken("(")) {
            // EXPRESSÃO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (validarToken(")")) {
                if (validarToken("then")) {
                    if (validarToken("{")) {
                        program();
                        if (validarToken("}")) {
                            if (validarToken("else")) {
                                elseConsumido();
                            }
                            program();
                        } else {
                            panicMode();
                        }
                    } else {
                        panicMode();
                    }
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }

    }

    private void elseConsumido() {
        if (validarToken("{")) {
            program();
            if (validarToken("}")) {

            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
    }

    public void panicMode() {

    }

    private void varConsumido() {
        if (validarToken("{")) {
            criarVariavel();
            if (validarToken("}")) {
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
    }

    private void structConsumido() {
        
    }

    /* Método para criar variáveis 
       Ex: int numero, nota, total; 
     */
    private void criarVariavel() {
        variaveis();
        if (validarToken(";")) {
            program();
        } else {
            panicMode();
        }
    }

    /* CRIAR VÁRIAVEIS PARA CONSTANTE (OBRIGATÓRIO INFORMAR O VALOR)*/
    private void variaveis() {
        fatoracaoVariaveis();
        if (validarToken("Identificador")) {
            acrescentar();
        } else {
            panicMode();
        }
    }

    private void fatoracaoVariaveis() {
        if (validarToken("[")) {
            if (validarToken("Número")) {
                if (validarToken("]")) {
                    fatoracaoFatoracaoVariaveis();
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        }
        acrescentar();
    }

    private void acrescentar() {
        if (validarToken(",")) {
            variaveis();
        }
    }

    private void fatoracaoFatoracaoVariaveis() {
        if (validarToken("[")) {
            if (validarToken("Número")) {
                if (validarToken("]")) {
                    fatoracaoFatoracaoVariaveis();
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        }
        acrescentar();
    }

    private void constConsumido() {
        if (validarToken("const")) {
            if (validarToken("{")) {
                variavelConstanteObjeto();
                if (validarToken("}")) {
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
    }

    private void whileConsumido() {
        if (validarToken("(")) {
            // EXPRESSÃO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (validarToken(")")) {
                if (validarToken("{")) {
                    program();
                    if (validarToken("}")) {
                    } else {
                        panicMode();
                    }
                } else {
                    panicMode();
                }
            } else {
                panicMode();
            }
        } else {
            panicMode();
        }
    }

    private void typedefConsumido() {

    }

    private void returnConsumido() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
