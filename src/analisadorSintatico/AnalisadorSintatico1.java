package analisadorSintatico;

import java.io.BufferedWriter;
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
    private int errosSintaticos = 0;
    private int contador = 0;
    private String StringErrosSintaticos = null;

    private boolean proximoToken = false;

    /**
     * Método que inicia a análise sintática.
     *
     * @param tokens lista de tokens extraídos da análise léxica
     * @param nomeArquivo 
     */
    public void iniciar(ArrayList tokens, String nomeArquivo) throws IOException {
        FileWriter saidaSintatico = new FileWriter("entrada\\saidaSintatico\\saida-" + nomeArquivo + ".txt");
        try {
            saidaSintatico.write("Análise Sintática iniciada para o arquivo " + nomeArquivo + "\n");
            //saidaSintatico.append("/n");
            System.out.println("Análise Sintática iniciada para o arquivo " + nomeArquivo);
            this.tokens = tokens;
            proximoToken();
            this.StringErrosSintaticos = "\n";
            Iterator iterador = this.tokens.listIterator();
            while (iterador.hasNext()) {
                Token token = (Token) iterador.next();
                System.out.println(token.getNome());
            }
            programa();

            if (errosSintaticos == 0 && !proximoToken) { // adicionar verificador de main !!!

                System.out.println("Análise Sintática finalizada com sucesso para o arquivo " + nomeArquivo + "\n");
                saidaSintatico.write("Análise Sintática finalizada com sucesso para o arquivo " + nomeArquivo + "\n");
            } else {
                System.out.println("\n\n");
                saidaSintatico.write(this.StringErrosSintaticos);
                saidaSintatico.write("\n\n");
                System.out.println("ERROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!");
                System.out.println("Análise Sintática finalizada com erro para o arquivo " + nomeArquivo);
                saidaSintatico.write("Análise Sintática finalizada com erro para o arquivo " + nomeArquivo);
            }
            saidaSintatico.close();

        } catch (IOException ex) {
            Logger.getLogger(AnalisadorSintatico1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
        Caso tenha '|' na grámatica seria if - if ...
        Caso seja direto na grámatica seria if - else if ...
     */
    private boolean proximoToken() {

        System.out.println("ENTROU EM Próximo token !!!!!");

        System.out.println("POSICAO: " + posicao + "TAMANHO TOTAL: " + tokens.size());

        if (posicao + 1 < tokens.size()) {
            posicao++;
            tokenAnterior = tokenAtual;
            tokenAtual = tokens.get(posicao);
            // System.out.println(" token atual em proximo token !!!!!" + tokenAtual);
            // Pulando comentários de linha e bloco
            //validarToken("Comentário de Linha");s
            //validarToken("Comentário de Bloco");
            proximoToken = true;
            System.out.println("O PRÓXIMO TOKEN É: " + tokenAtual);
            return true;
        }
        Token tokenFinal = new Token("", "", tokenAnterior.getLinha() + 1);
        tokens.add(tokenFinal);
        tokenAtual = tokens.get(posicao + 1);
        proximoToken = false;
        return false;
    }

    private void tokenAnterior(int contador) {

        System.out.println("**********************************************************CONTADOR: " + contador);
        for (int i = 1; i <= contador; i++) {
            System.out.println(i);
            if (posicao - 1 < tokens.size()) {
                System.out.println("*************************************************************PASSOU!");
                posicao--;
                tokenAtual = tokens.get(posicao);
                tokenAnterior = tokens.get(posicao - 1);
                // Pulando comentários de linha e bloco
                //validarToken("Comentário de Linha");
                //validarToken("Comentário de Bloco");
            }
        }
    }

    private boolean validarToken(String tipo) {
        System.out.println("METODO VALIDAR TOKEN:" + tipo + tokenAtual.getNome());
        //System.out.println("TESTE!");
        if (tokenAtual.getTipo().equals(tipo) || tokenAtual.getNome().equals(tipo)) {
            System.out.println("                                                Validou!, token:" + tokenAtual);
            proximoToken();
            return true;
        }
        //System.out.println("VALIDANDO TOKEN: " + tipo);
        return false;
    }

    private Token showProx() {
        System.out.println("");
        if (posicao + 1 < tokens.size()) {
            System.out.println("MOSTRARRRRRRRRRRRRRRRR:           " + tokens.get(posicao + 1));
            return tokens.get(posicao + 1);
        }

        System.out.println("NULLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO!");
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
        System.out.println("DECLARACAO");
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
        } else if (proximoToken) {
            System.out.println("**************************************************************");
            System.out.println("DECLARACAO INCORRETA!" + " NA LINHA: " + tokenAtual.getLinha());
            System.out.println("**************************************************************");
            String mensagemErro = "- ERRO DE DECLARAÇÃO";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
            panicMode("declaracao");
            return true;
        }
        System.out.println("SAIDA DECLARACAO");
        return false;
    }

    private boolean declaracaoDeFuncao() {
        System.out.println("DECLARACAO DE FUNCAO");

        if (validarToken("function")) {
            funcId();
            if (!validarToken("(")) {
                String mensagemErro = "- Faltou ( na declaração de fuction ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ( DE FUNCAO" + tokenAtual.getLinha());
                panicMode("funcaoProcedimentoFim");
            }
            funcaoProcedimentoFim();
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE FUNCAO");
        return false;

    }

    private boolean declaracaoDeProcedimento() {
        System.out.println("DECLARACAO DE PROCEDIMENTO");
        if (validarToken("procedure")) {
            if (!validarToken("IDE")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o identificador na declaração do procedure";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O IDENTIFICADOR" + tokenAtual.getLinha());
                //panicMode("funcaoProcedimentoFim");
            }
            if (!validarToken("(")) {
                String mensagemErro = "- Faltou o ( na declaração do procedure";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O (" + tokenAtual.getLinha());
                panicMode("funcaoProcedimentoFim");
            }
            funcaoProcedimentoFim();
            return true;

        }
        System.out.println("SAIDA DECLARACAO DE PROCEDIMENTO");
        return false;
    }

    private boolean declaracaoDeInicio() {
        System.out.println("DECLARACAO DE INICIO");
        if (validarToken("start")) {
            if (!validarToken("(")) {
                String mensagemErro = "- Faltou ( na declaracao de start";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O (" + tokenAtual.getLinha());
                panicMode("bloco");
            }
            if (!validarToken(")")) {
                String mensagemErro = "- Faltou ) na declaracao de start";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O )" + tokenAtual.getLinha());
                panicMode("bloco");
            }
            bloco();
            return true;

        }
        System.out.println("SAIDA DECLARACAO DE INICIO");
        return false;
    }

    private boolean declaracaoDeVar() {
        System.out.println("DECLARACAO DE VAR");
        if (validarToken("var")) {
            if (!validarToken("{")) {
                String mensagemErro = "- Faltou a { na declaração de var";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O { DO VAR" + tokenAnterior.getLinha());
                panicMode("declaracaoDeVariavelCorpo");
            }
            declaracaoDeVariavelCorpo();
            if (!validarToken("}")) {
                String mensagemErro = "- Faltou a } na declaracao de var";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O } DO VAR" + tokenAnterior.getLinha());
                panicMode("declaracao");
            }
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE VAR");
        return false;

    }

    private boolean declaracaoDeConst() {
        System.out.println("DECLARACAO DE CONST");
        if (validarToken("const")) {
            if (!validarToken("{")) {
                String mensagemErro = "- Faltou a { na declaracao de const";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O { DO CONST" + tokenAnterior.getLinha());
                panicMode("declaracaoDeConstanteCorpo");
            }
            declaracaoDeConstanteCorpo();
            if (!validarToken("}")) {
                String mensagemErro = "- Faltou a } na declaracao de const";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O } DO CONST" + tokenAnterior.getLinha());
                panicMode("declaracao");
            }
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE CONST");
        return false;
    }

    /*IMPLEMENTAR MODO PÂNICO!*/
    private boolean ifThen() {
        System.out.println("IF THEN");
        if (validarToken("if")) {
            if (!validarToken("(")) {
                String mensagemErro = "- Faltou o ( do If";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O (" + tokenAnterior.getLinha());
                panicMode("expressao");
            }
            if (!expressao()) {
                errosSintaticos++;
                String mensagemErro = "- Faltou informar a expressão do IF";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("Faltou informar a expressão do IF" + tokenAnterior.getLinha());;
            }
            if (!validarToken(")")) {
                String mensagemErro = "- Faltou o ) do If";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O )" + tokenAnterior.getLinha());
                panicMode("then");
            }
            if (!validarToken("then")) {
                String mensagemErro = "- Faltou o (then) da declaração do IF";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O then" + tokenAnterior.getLinha());
                panicMode("bloco");
            }
            bloco();
            return true;
        }
        System.out.println("SAIDA IF THEN");
        return false;
    }

    private void panicMode(String localSincronizar) {
        errosSintaticos++;
        switch (localSincronizar) {
            case "expressao":
                searchNextExpressao();
                break;
            case "then":
                searchNextThen();
                break;
            case "bloco":
                searchNextBloco();
                break;
            case "saida":
                searchNextSaida();
                break;
            case "entrada":
                searchNextEntrada();
                break;
            case "structCorpo":
                searchNextstructCorpo();
                break;
            case "listaDeInstrucoes":
                searchNextListaDeInstrucoes();
                break;
            case "funcaoProcedimentoFim":
                searchNextFuncaoProcedimentoFim();
                break;
            case "declaracaoDeVariavelCorpo":
                searchNextDeclaracaoDeVariavelCorpo();
                break;
            case "declaracao":
                searchNextDeclaracao();
                break;
            case "declaracaoDeConstanteCorpo":
                searchNextDeclaracaoDeConstanteCorpo();
                break;
            case "tipoAux":
                searchNextTipoAux();
                break;
            case "declaracaoDeStructCorpo":
                searchNextDeclaracaoDeStructCorpo();
                break;
            case "expressaoIdentificadoresStruct":
                searchNextExpressaoIdentificadoresStruct();
                break;
            case "tipoVetorDeclarando":
                searchNextTipoVetorDeclarando();
                break;
            case "parametroAux":
                searchNextParametroAux();
                break;
            case "parametros":
                searchNextParametros();
                break;
            case "simboloUnario":
                searchNextSimboloUnario();
                break;
            case "searchNextExpressaoIdentificadorVarAux":
                searchNextExpressaoIdentificadorVarAux();
                break;
            case "searchNextExpressaoIdentificadoresVarAux":
                searchNextExpressaoIdentificadoresVarAux();
                break;
        }
    }

    private boolean funcId() {
        System.out.println("FUNC ID");
        if (!tipo()) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o tipo da função";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
            System.out.println("FALTOU O TIPO" + tokenAtual.getLinha());
            //panicMode("funcaoProcedimentoFim");
        }
        if (!validarToken("IDE")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o nome da função";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
            System.out.println("FALTOU O IDENTIFICADOR" + tokenAtual.getLinha());
            //panicMode("funcaoProcedimentoFim");
        }

        System.out.println("SAIDA FUNC ID");
        return false;
    }

    private boolean tipo() {
        System.out.println("TIPO");
        if (tipobase()) {
            tipoAux();
            return true;
        }

        System.out.println("SAIDA TIPO");
        return false;
    }

    private boolean tipobase() {
        System.out.println("TIPO BASE");
        if (validarToken("IDE")) {
            System.out.println("3");
            return true;
        } else if (escalar()) {
            return true;
        } else if (declaracaoDeStruct()) { // VERIFICAR
            return true;
        } else if (validarToken("struct")) {
            if (!validarToken("IDE")) {
                String mensagemErro = "- Faltou o identificador da struct";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O IDENTIFICADOR DO STRUCT" + tokenAtual.getLinha());
                panicMode("tipoAux");
            }
            return true;
        }
        System.out.println("SAIDA TIPO BASE");
        return false;
    }

    private boolean escalar() {
        System.out.println("ESCALAR");
        if (validarToken("int") || validarToken("float") || validarToken("bool") || validarToken("string")) {
            return true;
        }
        System.out.println("SAIDA ESCALAR");
        return false;
    }

    private boolean declaracaoDeStruct() {
        System.out.println("DECLARACAO DE STRUCT");
        if (validarToken("struct")) {
            if (declaracaoDeStructAux()) {
                return true;
            }
            tokenAnterior(1);
        }
        System.out.println("SAIDA DECLARACAO DE STRUCT");
        return false;
    }

    private boolean declaracaoDeStructAux() {
        System.out.println("DECLARACAO DE STRUCT AUX");
        if (validarToken("IDE")) {
            if (tokenAtual.getTipo().equals("IDE")) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                tokenAnterior(1);
                return false;
            }
            Extends();// LEMBRARRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR
            if (!validarToken("{")) {
                String mensagemErro = "- Faltou a { da struct";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O { DE STRUCT" + tokenAtual.getLinha());
                panicMode("structCorpo");
            }
            declaracaoDeStructCorpo();
            if (!validarToken("}")) {
                String mensagemErro = "- Faltou a } da struct ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O } DE STRUCT" + tokenAtual.getLinha());
                panicMode("listaDeInstrucoes");
            }
            return true;
        } else if (Extends()) {
            if (!validarToken("{")) {
                String mensagemErro = "- Faltou a { da struct";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("faltou { da struct" + tokenAtual.getLinha());
                panicMode("structCorpo");
            }
            declaracaoDeStructCorpo();
            if (!validarToken("}")) {
                String mensagemErro = "- Faltou a } da struct ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O } DE STRUCT" + tokenAtual.getLinha());
                panicMode("listaDeInstrucoes");
            }
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE STRUCT AUX");
        return false;
    }

    private boolean Extends() {
        System.out.println("EXTENDS");
        if (validarToken("extends")) {
            if (!validarToken("IDE")) {
                String mensagemErro = "- Faltou o identificador do extends";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O IDENTIFICADOR DO EXTENDS" + tokenAtual.getLinha());
                panicMode("declaracaoDeStructCorpo");
            }
        } // PODE SER VAZIO
        System.out.println("SAIDA EXTENDS");
        return true;
    }

    private boolean declaracaoDeStructCorpo() {
        System.out.println("DECLARACAO DE STRUCT CORPO");
        if (declaracaoDeStructLinha()) {
            declaracaoDeStructCorpoAux();
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE STRUCT CORPO");
        return false;

    }

    private boolean declaracaoDeStructLinha() {
        System.out.println("DECLARACAO DE STRUCT LINHA");
        if (tipo()) {
            expressaoIdentificadoresStruct();
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE STRUCT LINHA");
        return false;
    }

    private boolean expressaoIdentificadoresStruct() {
        System.out.println("EXPRESSAO IDENTIFICADORES STRUCT");
        if (expressaoIdentificadorStruct()) {
            expressaoIdentificadoresStructAux();
            return true;
        }
        System.out.println("SAIDA EXPRESSAO IDENTIFICADORES STRUCT");
        return false;
    }

    private boolean expressaoIdentificadorStruct() {
        System.out.println("EXPRESSAO IDENTIFICADOR STRUCT");
        if (validarToken("IDE")) {
            return true;
        }

        //System.out.println("erro sintatico: expressaoIdentificadorStruct");
        System.out.println("SAIDA EXPRESSAO IDENTIFICADOR STRUCT");
        return false;
    }

    private boolean expressaoIdentificadoresStructAux() {
        System.out.println("EXPRESSAO IDENTIFICADORES STRUCT AUX");
        if (!validarToken(";") && !tokenAtual.getNome().equals(",")) {
            String mensagemErro = "- Faltou o ;";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("FALTOU O ; DO STRUCT" + tokenAtual.getLinha());
            panicMode("declaracaoDeStructCorpo");
            return true;
        } else if (!validarToken(",")) {
            System.out.println("PROXIMOOOOOOOOOOOOO: " + showProx().getNome());
            if (tokenAtual.getTipo().equals("IDE")) {
                String mensagemErro = "- Faltou o identificador da struct";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";

                System.out.println("FALTOU A VIRGULA" + tokenAtual.getLinha());
                panicMode("expressaoIdentificadoresStruct");
            }
        }
        expressaoIdentificadoresStruct();

        System.out.println("SAIDA EXPRESSAO IDENTIFICADORES STRUCT AUX");
        return false;
    }

    /*
        TERMINAR!!!!!!!!
     */
    private boolean tipoAux() {
        System.out.println("TIPO AUX");
        if (tipoVetorDeclarando()) {
            return true;
        }

        System.out.println("SAIDA TIPO AUX");
        return false;
    }

    private boolean tipoVetorDeclarando() {
        System.out.println("TIPO VETOR DECLARANDO");
        if (tipoVetorDeclarado()) {
            tipoVetorDeclarandoAux();
            return true;
        }
        System.out.println("SAIDA TIPO VETOR DECLARANDO");
        return false;
    }

    private boolean tipoVetorDeclarado() {
        System.out.println("TIPO VETOR DECLARADO");
        if (validarToken("[")) {
            if (!validarToken("]")) {
                String mensagemErro = "- Faltou ] do vetor";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O ] do vetor" + tokenAtual.getLinha());
                panicMode("tipoVetorDeclarando");
            }
            return true;
        }
        System.out.println("SAIDA TIPO VETOR DECLARADO");
        return false;
    }

    private boolean tipoVetorDeclarandoAux() {
        System.out.println("TIPO VETOR DECLARANDO AUX");
        tipoVetorDeclarando();

        System.out.println("SAIDA TIPO VETOR DECLARANDO AUX");
        return true;
    }

    private boolean declaracaoDeStructCorpoAux() {
        System.out.println("DECLARACAO DE STRUCT CORPO AUX");
        declaracaoDeStructCorpo();

        System.out.println("SAIDA DECLARACAO DE STRUCT CORPO AUX");
        return true;
    }

    /**
     * ***********************************************************************
     */
    private boolean funcaoProcedimentoFim() {
        System.out.println("FUNCAO PROCEDIMENTO FIM");
        if (validarToken(")")) {
            //System.out.println("AAAAAAAAAAAAAA");
            if (bloco()) {

            }
            return true;
        } else if (parametros()) {
            //System.out.println("BBBBBBBBBBBBBBBBBBb");
            if (!validarToken(")")) {
                String mensagemErro = "- Faltou o )";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ) DA FUNCAO" + tokenAnterior.getLinha());
                panicMode("bloco");
            }
            if (bloco()) {

            }
            return true;
        } else {
            String mensagemErro = "- Faltou o )";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("FALTOU O ) DA FUNCAO" + tokenAnterior.getLinha());
            panicMode("bloco");

            if (bloco()) {
                return true;
            }
        }

        System.out.println("SAIDA FUNCAO PROCEDIMENTO FIM");
        return false;
    }

    private boolean parametros() {
        System.out.println("PARAMETROS");
        if (parametro()) {
            parametrosAux();
            return true;
        }
        System.out.println("SAIDA PARAMETROS");
        return false;
    }

    private boolean bloco() {
        System.out.println("BLOCO");
        if (!validarToken("{")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou { do bloco";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("FALTOU O { DO BLOCO" + tokenAnterior.getLinha());
            //panicMode("listaDeInstrucoes");
        }
        blocoAux();
        System.out.println("SAIDA BLOCO");
        return false;
    }

    private boolean parametro() {
        System.out.println("PARAMETRO");
        if (tipo()) {
            System.out.println("TOKENNNN ATUALLLLLLL>>>>> " + tokenAtual);
            if (!validarToken("IDE")) {
                String mensagemErro = "- Faltou o identificador do parâmetro";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O IDENTIFICADOR DO PARAMETRO" + tokenAtual.getLinha());
                panicMode("parametrosAux");
                return false;
            }
            return true;
        }
        System.out.println("SAIDA PARAMETRO");
        return false;
    }

    private boolean parametrosAux() {
        System.out.println("PARAMETROS AUX");
        if (!validarToken(",")) {
            if (showProx().getTipo().equals("IDE")) {
                String mensagemErro = "faltou virgula em parametros";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU A VÍRGULA" + tokenAtual.getLinha());
                panicMode("parametros");
            }
        }
        parametros();
        System.out.println("SAIDA PARAMETROS AUX");
        return true;
    }

    /*
        VERIFICAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
     */
    private boolean blocoAux() {
        System.out.println("BLOCO AUX");
        if (listaDeInstrucoes()) {
            System.out.println("ENTROUUUUUUUUUUUUUUUUUUUUUUUUUUU AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII!");
            if (!validarToken("}")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o fecha } do bloco";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                panicMode("*********************");
            }
        } else if (!validarToken("}")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o fecha } do bloco";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
        }
        System.out.println("SAIDA BLOCO AUX");
        return false;
    }

    private boolean listaDeInstrucoes() {
        System.out.println("LISTA DE INSTRUCOES");
        if (instrucao()) {
            listaDeInstrucoesAux();
            return true;
        }
        System.out.println("SAIDA LISTA DE INSTRUCOES");
        return false;
    }

    private boolean instrucao() {
        System.out.println("INSTRUÇÃO");
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
        System.out.println("SAIDA INSTRUCAO");
        return false;
    }

    private boolean listaDeInstrucoesAux() {
        listaDeInstrucoes();
        return true;
    }

    private boolean instrucaoNormal() {
        System.out.println("INSTRUCAO NORMAL");
        if (operacaoDeAtribuicao()) {
            if (!validarToken(";")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ; da instrucao";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ; DA INSTRUÇÃO" + tokenAnterior.getLinha());
                //panicMode("listaDeInstrucoes");
            }
            return true;
        } else if (declaracaoDeStruct()) {
            if (!validarToken(";")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ; da Struct";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ; DA INSTRUÇÃO" + tokenAnterior.getLinha());
                //panicMode("listaDeInstrucoes");
            }
            return true;
        } else if (Print()) {
            if (!validarToken(";")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ; do Print";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ; DA INSTRUÇÃO" + tokenAnterior.getLinha());
                //panicMode("listaDeInstrucoes");
            }
            return true;
        } else if (scan()) {
            if (!validarToken(";")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ; do Scan";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ; DA INSTRUÇÃO" + tokenAnterior.getLinha());
                //panicMode("listaDeInstrucoes");
            }
            return true;
        } else if (instrucaoDeRetorno()) {
            if (!validarToken(";")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou ; do retorno";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ; DA INSTRUÇÃO" + tokenAnterior.getLinha());
                //panicMode("listaDeInstrucoes");
            }
            return true;
        }
        System.out.println("SAIDA INSTRUCAO NORMAL");
        return false;
    }

    /////aqui em diante....
    /*TRÊS PRIMEIROS COMO IDENTIFICADOR*/
    private boolean operacaoDeAtribuicao() {
        System.out.println("OPERACAO DE ATRIBUICAO");
        if (validarToken("IDE")) {
            System.out.println("111111111111111111111111111111111111111111111111111111111111111111111111111");
            if (validarToken("=")) {
                if (expressao()) {
                    return true;
                }
                tokenAnterior(2);
            }
            tokenAnterior(1);
        }
        if (Final()) {
            System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222222222");
            System.out.println("FINAL!");
            if (validarToken("=")) {
                if (expressao()) {
                    return true;
                }
                tokenAnterior(1);
            }
        }
        if (expressao()) {
            System.out.println("3333333333333333333333333333333333333333333333333333333333333333333333333333");
            System.out.println("EXPRESSAO!");
            return true;
        }

        System.out.println("4444444444444444444444444444444444444444444444444444444444444444444444444444444444444");
        System.out.println("SAIDA OPERACAO DE ATRIBUICAO");
        return false;
    }

    private boolean instrucaoDeRetorno() {
        System.out.println("INSTRUÇÃO DE RETORNO");
        if (validarToken("return")) {
            if (instrucaoDeRetornoAux()) {
                return true;
            }
        }

        System.out.println("SAIDA INSTRUÇÃO DE RETORNO");
        return false;
    }

    private boolean Print() { // revisar...
        System.out.println("PRINT");
        if (validarToken("print")) {
            if (!validarToken("(")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ( do print ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ( DO PRINT");
                //panicMode("saida");
            }
            saida();
            outrasSaidas();
            if (!validarToken(")")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ) do print ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ) DO PRINT");
                //panicMode("listaDeInstrucoes");
            }
            return true;
        }
        System.out.println("SAIDA PRINT");
        return false;
    }

    private boolean scan() {
        System.out.println("SCAN");
        if (validarToken("scan")) {
            if (!validarToken("(")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ( do scan";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O ( DO SCAN");
                //panicMode("entrada");
            }
            entrada();
            outrasEntradas();
            if (!validarToken(")")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o ) do scan ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ) DO SCAN");
                //panicMode("listaDeInstrucoes");
            }
            return true;
        }
        System.out.println("SAIDA SCAN");
        return false;
    }

    private boolean estruturaCondicional() {
        if (ifThen()) {
            estruturaCondicionalAux();
            return true;
        }
        return false;
    }

    private boolean While() {
        System.out.println("WHILE");
        if (validarToken("while")) {
            if (!validarToken("(")) {
                String mensagemErro = "faltou ( do while ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O ( DO WHILE");
                panicMode("expressao");
            }
            if (!expressao()) {
                errosSintaticos++;
                String mensagemErro = "- Faltou a expressão do While";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("Faltou a expressão do While");
            }
            if (!validarToken(")")) {
                String mensagemErro = "faltou ) do while ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O ) DO WHILE");
                panicMode("bloco");
            }
            bloco();
            return true;
        }
        System.out.println("SAIDA WHILE");
        return false;
    }

    private boolean declaracaoDeTypedef() {
        System.out.println("DECLARACAO DE TYPEDEF");
        if (validarToken("typedef")) {
            declaracaoDeTypedefAux();
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE TYPEDEF");
        return false;
    }

    private boolean Final() {
        System.out.println("FINAL");
        if (validarToken("IDE")) {
            System.out.println("--------------------------------------------- 4- " + contador);
            System.out.println("10");
            if (acessando()) {
                return true;
            }
            tokenAnterior(1);
        }
        System.out.println("SAIDA FINAL");
        return false;
    }

    private boolean expressao() {
        System.out.println("EXPRESSAO");
        if (opE()) {
            expressaoAux();
            return true;
        }

        System.out.println("SAIDA EXPRESSAO");
        return false;
    }

    private boolean instrucaoDeRetornoAux() {
        System.out.println("INSTRUCAO DE RETORNO AUX");
        expressao();

        System.out.println("SAIDA INSTRUCAO DE RETORNO AUX");
        return true;
    }

    private boolean saida() {
        System.out.println("SAIDA");
        if (expressao()) {
            return true;
        }
        System.out.println("SAIDA SAIDA");
        return false;
    }

    private boolean outrasSaidas() {
        System.out.println("OUTRAS SAIDAS");
        if (validarToken(",")) {
            saida();
            if (outrasSaidas()) {
                return true;
            }
        }
        System.out.println("SAIDA OUTRAS SAIDAS");
        return true;
    }

    private boolean entrada() {
        System.out.println("ENTRADAS");
        Final();
        if (!validarToken("IDE")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o identificador";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
            System.out.println("- Faltou o identificador");
            return true;
        }
        System.out.println("SAIDA ENTRADAS");
        return false;
    }

    private boolean outrasEntradas() {
        System.out.println("OUTRAS ENTRADAS");
        if (validarToken(",")) {
            entrada();
            if (outrasEntradas()) {
                return true;
            }
        }
        System.out.println("SAIDA OUTRAS ENTRADAS");
        return true;
    }

    private boolean estruturaCondicionalAux() {
        System.out.println("ESTRUTURA CONDICIONAL AUX");
        if (validarToken("else")) {
            if (bloco()) {
                return true;
            }
        }
        System.out.println("SAIDA ESTRUTURA CONDICIONAL AUX");
        return true;
    }

    private boolean declaracaoDeVariavelCorpo() {
        System.out.println("DECLARACAO DE VARIAVEL CORPO");
        if (declaracaoDeVariavelLinha()) {
            if (declaracaoDeVariavelCorpoAux()) {
                return true;
            }
        }
        System.out.println("SAIDA DECLARACAO DE VARIAVEL CORPO");
        return false;
    }

    private boolean declaracaoDeVariavelLinha() {
        System.out.println("DECLARACAO DE VARIAVEL LINHA");
        if (tipo()) {
            if (expressaoIdentificadoresVar()) {
                return true;
            }
        }
        System.out.println("SAIDA DECLARACAO DE VARIAVEL LINHA");
        return false;
    }

    private boolean declaracaoDeVariavelCorpoAux() {
        System.out.println("DECLARACAO DE VARIAVEL CORPO AUX");
        if (declaracaoDeVariavelCorpo()) {
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE VARIAVEL CORPO AUX");
        return true;
    }

    private boolean expressaoIdentificadoresVar() {
        System.out.println("EXPRESSAO IDENTIFICADORES VAR");
        if (expressaoIdentificadorVar()) {

        }
        if (expressaoIdentificadoresVarAux()) {
            return true;
        }
        System.out.println("SAIDA EXPRESSAO IDENTIFICADORES VAR");
        return false;
    }

    private boolean expressaoIdentificadorVar() {
        System.out.println("EXPRESSAO IDENTIFICADOR VAR");
        if (validarToken("IDE")) {
            System.out.println("12");

            /**
             * ****************************** VERIFICAR
             * *******************************
             */
            //tokenAnterior(1);
            /**
             * ************************************************************************
             */
        } else {
            panicMode("expressaoIdentificadorVarAux");
            System.out.println("*************************** FALTOU O IDENTIFICADOR *********************************");
            String mensagemErro = "- Faltou o Identificador";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";

        }
        if (expressaoIdentificadorVarAux()) {
            return true;
        }
        System.out.println("SAIDA EXPRESSAO IDENTIFICADOR VAR");
        return false;
    }

    private boolean expressaoIdentificadoresVarAux() {
        System.out.println("EXPRESSAO IDENTIFICADORES VAR AUX");
        if (tokenAtual.getTipo().equals("IDE")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou a ,";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("Faltou a ,");
        }
        if (validarToken(",") || tokenAtual.getTipo().equals("IDE")) {
            if (expressaoIdentificadoresVar()) {
            }
            return true;
        }
        if (!validarToken(";")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o ;";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("Faltou o ;");
            return true;
        }

        System.out.println("SAIDA EXPRESSAO IDENTIFICADOR VAR AUX");
        return true;
    }

    private boolean expressaoIdentificadorVarAux() {
        System.out.println("EXPRESSAO IDENTIFICADOR VAR AUX");
        if (validarToken("=")) {
            if (expressao()) {
                return true;
            }//if (showProx().getTipo().equals("IDE")) {
        }
        System.out.println("SAIDA EXPRESSAO IDENTIFICADOR VAR AUX");
        return true;
    }

    private boolean declaracaoDeConstanteCorpo() {
        System.out.println("DECLARACAO DE CONSTANTE CORPO");
        if (declaracaoDeConstanteLinha()) {
            if (declaracaoDeConstanteCorpoAux()) {
                return true;
            }
        }
        System.out.println("SAIDA DECLARACAO DE CONSTANTE CORPO");
        return false;
    }

    private boolean declaracaoDeConstanteLinha() {
        System.out.println("DECLARACAO DE CONSTANTE LINHA");
        if (tipo()) {
            if (expressaoIdentificadoresConst()) {
                return true;
            }
        }
        System.out.println("DECLARACAO DE CONSTANTE LINHA");
        return false;
    }

    /**
     * comecei a partir daqui >>>>>>
     *
     */
    private boolean declaracaoDeConstanteCorpoAux() {
        System.out.println("DECLARACAO DE CONSTANTE CORPO AUX");
        if (declaracaoDeConstanteCorpo()) {
            return true;
        }
        System.out.println("SAIDA DECLARACAO DE CONSTANTE CORPO AUX");
        return true;
    }

    private boolean expressaoIdentificadoresConst() {
        System.out.println("EXPRESSAO IDENTIFICADORES CONST");
        if (expressaoIdentificadorConst()) {
            expressaoIdentificadoresConstAux();
            return true;
        }
        System.out.println("SAIDA EXPRESSAO IDENTIFICADORES CONST");
        return false;
    }

    /* VERIFICAR TOKEN ANTERIOR NO CONST*/
    private boolean expressaoIdentificadorConst() {
        System.out.println("EXPRESSAO IDENTIFICADOR CONST");
        if (validarToken("IDE")) {
            System.out.println("13");
            if (!validarToken("=")) {
                errosSintaticos++;
                String mensagemErro = "- Faltou o = da expressão";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O = DA EXPRESSÃO" + tokenAnterior.getLinha());;
                //panicMode("expressao");
            }
            if (!expressao()) {
                errosSintaticos++;
                String mensagemErro = "- Faltou informar o valor do atributo";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
                System.out.println("FALTOU O = DA EXPRESSÃO" + tokenAnterior.getLinha());;
            }
            return true;
            //tokenAnterior(1);
        }
        System.out.println("SAIDA EXPRESSAO IDENTIFICADOR CONST");
        return false;
    }

    /*
    private boolean expressaoIdentificadoresConstAux() {
        System.out.println("EXPRESSAO IDENTIFICADORES CONST AUX");
        if (validarToken(";")) {
            return true;
        } else if (validarToken(",")) {
            expressaoIdentificadoresConst();
            return true;
        }
        System.out.println("SAIDA EXPRESSAO IDENTIFICADORES CONST AUX");
        return false;
    }
     */
    private boolean expressaoIdentificadoresConstAux() {
        System.out.println("EXPRESSAO IDENTIFICADORES CONST AUX");
        if (tokenAtual.getTipo().equals("IDE")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou a ,";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("Faltou a ,");
        }
        if (validarToken(",") || tokenAtual.getTipo().equals("IDE")) {
            if (expressaoIdentificadoresConst()) {
                return true;
            }
        }
        if (!validarToken(";")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o ;";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("Faltou o ;");
            return true;
        }

        System.out.println("SAIDA EXPRESSAO IDENTIFICADOR CONST AUX");
        return true;
    }

    private boolean declaracaoDeTypedefAux() {
        System.out.println("DECLARACAO DE TYPEDEF AUX");
        if (!tipo()) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o tipo do Typedef";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("- Faltou o tipo do Typedef");
        }
        if (!validarToken("IDE")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou o identificador do typedef";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("Faltou o identificador do typedef");
            //panicMode("listaDeInstrucoes");
        }
        if (!validarToken(";")) {
            errosSintaticos++;
            String mensagemErro = "- Faltou ; do typedef ";
            this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAnterior.getLinha() + "\n";
            System.out.println("FALTOU O ; NA DECLARACAO DE TYPEDEF ");
            //panicMode("listaDeInstrucoes");
        }
        System.out.println("SAIDA DECLARACAO DE TYPEDEF AUX");
        return false;
    }

    private boolean acessando() {
        System.out.println("ACESSANDO");
        if (acesso()) {
            acessandoAux();
            return true;
        }
        System.out.println("SAIDA ACESSANDO");
        return false;
    }

    private boolean acesso() {
        System.out.println("ACESSO");
        if (validarToken(".")) {
            if (!validarToken("IDE")) {
                String mensagemErro = "faltou identificador no acesso ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU IDENTIFICADOR");
                panicMode("listaDeInstrucoes");
            }
            return true;
        } else if (validarToken("[")) {
            expressao();
            if (!validarToken("]")) {
                String mensagemErro = "faltou ] ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU ]");
                panicMode("listaDeInstrucoes");
            }
            return true;
        }
        System.out.println("SAIDA ACESSO");
        return false;
    }

    private boolean acessandoAux() {
        System.out.println("ACESSANDO AUX");

        acessando();

        System.out.println("SAIDA ACESSANDO AUX");
        return true;
    }

    private boolean opE() {
        System.out.println("OPE");
        if (opRelacional()) {
            opEAux();
            return true;
        }
        System.out.println("SAIDA OPE");
        return false;
    }

    private boolean expressaoAux() { /// pulei !!!!!
        System.out.println("EXPRESSAO AUX");
        if (validarToken("||")) {
            if (expressao()) {
                return true;
            } else {
                return false;
            }
        }
        System.out.println("SAIDA EXPRESSAO AUX");
        return true;
    }

    private boolean opRelacional() {
        System.out.println("OP RELACIONAL");
        if (valorRelacional()) {
            opRelacionalAux();
            return true;
        }
        System.out.println("SAIDA OP RELACIONAL");
        return false;
    }

    private boolean opEAux() {
        System.out.println("OP AUX");
        if (validarToken("&&")) {
            opE();
            return true;
        }
        System.out.println("SAIDA OP AUX");
        return true;
    }

    private boolean valorRelacional() {
        System.out.println("VALOR RELACIONAL");
        if (opMult()) {
            valorRelacionalAux();
            return true;

        }
        System.out.println("SAIDA VALOR RELACIONAL");
        return false;
    }

    private boolean opRelacionalAux() {
        System.out.println("OP RELACIONAL AUX");
        if (escalarRelacional()) {
            opRelacional();
            return true;
        }
        System.out.println("SAIDA OP RELACIONAL AUX");
        return true;
    }

    private boolean opMult() {
        System.out.println("OP MULT");
        if (opUnary()) {
            opMultAux();
            return true;
        }
        System.out.println("SAIDA OP MULT");
        return false;
    }

    private boolean valorRelacionalAux() {
        System.out.println("VALOR RELACIONAL AUX");
        if (validarToken("+")) {
            opMult();
            valorRelacionalAux();
            return true;

        } else if (validarToken("-")) {
            opMult();
            valorRelacionalAux();
            return true;
        }
        System.out.println("SAIDA VALOR RELACIONAL AUX");
        return true;
    }

    private boolean escalarRelacional() {
        System.out.println("ESCALAR RELACIONAL");
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
        System.out.println("SAIDA ESCALAR RELACIONAL");
        return false;
    }

    /*
     * VALOR E FINAL POSSUEM O IDENTIFICADOR COMO PRIMEIRO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private boolean opUnary() {
        System.out.println("OP UNARY");
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
        System.out.println("SAIDA OP UNARY");
        return false;
    }

    private boolean opMultAux() {
        System.out.println("OP MULT AUX");
        if (validarToken("*")) {
            opUnary();
            opMultAux();
            return true;

        } else if (validarToken("/")) {
            opUnary();
            opMultAux();
            return true;
        }
        System.out.println("SAIDA OP MULT AUX");
        return true;
    }

    private boolean simboloUnario() {
        System.out.println("SIMBOLO UNARIO");
        if (validarToken("++")) {
            return true;
        } else if (validarToken("--")) {
            return true;
        }
        System.out.println("SAIDA SIMBOLO UNARIO");
        return true;
    }

    private boolean valor() {
        System.out.println("VALOR");

        if (validarToken("(")) {
            expressao();
            if (!validarToken(")")) {
                String mensagemErro = "faltou ) ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                panicMode("simboloUnario");
            }
            return true;
        } else if (validarToken("NRO")) {
            return true;
        } else if (validarToken("CAD")) {
            return true;
        } else if (validarToken("true")) {
            return true;
        } else if (validarToken("false")) {
            return true;
        } else if (validarToken("IDE")) {
            System.out.println("16");
            if (valorAux1()) {
                return true;
            }
            tokenAnterior(1);
        }
        System.out.println("SAIDA VALOR");
        return false;
    }

    private boolean valorAux1() {
        System.out.println("VALOR AUX1");
        if (validarToken("(")) {
            valorAux2();
        }
        System.out.println("SAIDA VALOR AUX1");
        return true;
    }

    private boolean valorAux2() {
        System.out.println("VALOR AUX 2");
        if (parametrosFuncao()) {
            if (!validarToken(")")) {
                String mensagemErro = "faltou ) ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU O ) DO VALOR" + tokenAtual.getLinha());
                panicMode("simboloUnario");
            }
            return true;
        } else if (!validarToken(")")) {
            panicMode("simboloUnario");
        }
        System.out.println("SAIDA VALOR AUX 2");
        return false;
    }

    private boolean parametrosFuncao() {
        System.out.println("PARAMETROS FUNCAO");
        if (expressao()) {
            parametrosFuncaoAux();
            return true;
        }
        System.out.println("SAIDA PARAMETROS FUNCAO");
        return false;
    }

    private boolean parametrosFuncaoAux() {
        System.out.println("PARAMETROS FUNCAO AUX");
        if (!validarToken(",")) {
            if (!tokenAtual.getNome().equals(")")) { ///////////////////////***//**/*/**/*/*/*/*/*//*/**/*/*/*/*/*/*/*/
                String mensagemErro = "faltou , ";
                this.StringErrosSintaticos = this.StringErrosSintaticos + mensagemErro + " na linha:" + tokenAtual.getLinha() + "\n";
                System.out.println("FALTOU A VÍRGULA" + tokenAtual.getLinha());
                panicMode("parametros");
            }
            return true;
        }
        parametrosFuncao();
        System.out.println("SAIDA PARAMETROS FUNCAO AUX");
        return true;
    }

    private boolean firstForaDeBloco() { //
        return tokenAtual.getNome().equals("const") || tokenAtual.getNome().equals("var") || tokenAtual.getNome().equals("struct")
                || tokenAtual.getNome().equals("typedef") || tokenAtual.getNome().equals("procedure") || tokenAtual.getNome().equals("function")
                || tokenAtual.getNome().equals("return") || tokenAtual.getNome().equals("start")
                || tokenAtual.getNome().equals("then") || tokenAtual.getNome().equals("else") || tokenAtual.getNome().equals("while")
                || tokenAtual.getNome().equals("scan") || tokenAtual.getNome().equals("print") || tokenAtual.getNome().equals("int")
                || tokenAtual.getNome().equals("float") || tokenAtual.getNome().equals("bool") || tokenAtual.getNome().equals("string")
                || tokenAtual.getNome().equals("true") || tokenAtual.getNome().equals("false") || tokenAtual.getNome().equals("extends");
    }

    private boolean firstDentroDeBloco() {
        return tokenAtual.getNome().equals("int") || tokenAtual.getNome().equals("float") || tokenAtual.getTipo().equals("IDE")
                || tokenAtual.getNome().equals("string") || tokenAtual.getNome().equals("bool");

    }

    /**
     * Procura o proximo token de sincronização
     */
    private void searchNextExpressao() {
        if (tokenAtual.getNome().equals("--") || tokenAtual.getNome().equals("!") || tokenAtual.getNome().equals("(")
                || tokenAtual.getNome().equals("++") || tokenAtual.getTipo().equals("CAD")
                || tokenAtual.getTipo().equals("DIG") || tokenAtual.getNome().equals("false")
                || tokenAtual.getTipo().equals("IDE") || tokenAtual.getNome().equals("true")) {

        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextExpressao();
        }
    }

    private void searchNextThen() {
        if (tokenAtual.getNome().equals("then")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextThen();
        }
    }

    private void searchNextBloco() {
        ///System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
        if (tokenAtual.getNome().equals("{")) {

        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextBloco();
        }
    }

    private void searchNextListaDeInstrucoes() {
        if (tokenAtual.getNome().equals("--") || tokenAtual.getNome().equals("!")
                || tokenAtual.getNome().equals("(") || tokenAtual.getNome().equals("++")
                || tokenAtual.getTipo().equals("CAD") || tokenAtual.getTipo().equals("DIG")
                || tokenAtual.getNome().equals("false") || tokenAtual.getTipo().equals("IDE")
                || tokenAtual.getNome().equals("print") || tokenAtual.getNome().equals("return")
                || tokenAtual.getNome().equals("scan") || tokenAtual.getNome().equals("struct")
                || tokenAtual.getNome().equals("true") || tokenAtual.getNome().equals("typedef")
                || tokenAtual.getNome().equals("var") || tokenAtual.getNome().equals("while")
                || tokenAtual.getNome().equals(")")) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextListaDeInstrucoes();
        }
    }

    private void searchNextstructCorpo() {
        if (tokenAtual.getNome().equals("bool") || tokenAtual.getNome().equals("float")
                || tokenAtual.getTipo().equals("IDE") || tokenAtual.getNome().equals("int")
                || tokenAtual.getNome().equals("string") || tokenAtual.getNome().equals("struct")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextstructCorpo();
        }
    }

    private void searchNextSaida() {
        if (tokenAtual.getNome().equals("--") || tokenAtual.getNome().equals("!")
                || tokenAtual.getNome().equals("(") || tokenAtual.getNome().equals("++")
                || tokenAtual.getTipo().equals("CAD") || tokenAtual.getTipo().equals("DIG")
                || tokenAtual.getNome().equals("false") || tokenAtual.getTipo().equals("IDE")
                || tokenAtual.getNome().equals("true")) {
        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextSaida();
        }
    }

    private void searchNextEntrada() {
        if (tokenAtual.getTipo().equals("IDE")) {
        } else if (firstDentroDeBloco()) {
        } else if (proximoToken()) {
            searchNextEntrada();
        }
    }

    private void searchNextDeclaracaoDeStructCorpo() {
        if (tokenAtual.getNome().equals("bool") || tokenAtual.getNome().equals("float")
                || tokenAtual.getTipo().equals("IDE") || tokenAtual.getNome().equals("int")
                || tokenAtual.getNome().equals("string") || tokenAtual.getNome().equals("struct")) {
        } else if (firstDentroDeBloco()) {
        } else if (firstForaDeBloco()) {
        } else if (proximoToken()) {
            searchNextDeclaracaoDeStructCorpo();
        }
    }

    private void searchNextFuncaoProcedimentoFim() {
        if (tokenAtual.getNome().equals(")") || tokenAtual.getNome().equals("bool")
                || tokenAtual.getNome().equals("float") || tokenAtual.getTipo().equals("IDE")
                || tokenAtual.getNome().equals("int") || tokenAtual.getNome().equals("string")
                || tokenAtual.getNome().equals("struct")) {
        } else if (firstForaDeBloco()) {

        } else if (proximoToken()) {
            searchNextFuncaoProcedimentoFim();
        }
    }

    private void searchNextDeclaracaoDeVariavelCorpo() {
        if (tokenAtual.getNome().equals("bool") || tokenAtual.getNome().equals("float")
                || tokenAtual.getTipo().equals("IDE") || tokenAtual.getNome().equals("int")
                || tokenAtual.getNome().equals("string") || tokenAtual.getNome().equals("struct")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextDeclaracaoDeVariavelCorpo();
        }
    }

    private void searchNextDeclaracao() {
        if (tokenAtual.getNome().equals("const") || tokenAtual.getNome().equals("function")
                || tokenAtual.getNome().equals("procedure") || tokenAtual.getNome().equals("start")
                || tokenAtual.getNome().equals("typedef") || tokenAtual.getNome().equals("var")) {

        } else if (proximoToken()) {
            searchNextDeclaracao();
        }
    }

    private void searchNextDeclaracaoDeConstanteCorpo() {
        if (tokenAtual.getNome().equals("bool") || tokenAtual.getNome().equals("float")
                || tokenAtual.getTipo().equals("IDE") || tokenAtual.getNome().equals("int")
                || tokenAtual.getNome().equals("string") || tokenAtual.getNome().equals("struct")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextDeclaracaoDeConstanteCorpo();
        }
    }

    private void searchNextTipoAux() {
        if (tokenAtual.getNome().equals("[")) {
        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextTipoAux();
        }
    }

    private void searchNextExpressaoIdentificadoresStruct() {
        if (tokenAtual.getTipo().equals("IDE")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextExpressaoIdentificadoresStruct();
        }
    }

    private void searchNextTipoVetorDeclarando() {
        if (tokenAtual.getNome().equals("{")) {
        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextTipoVetorDeclarando();
        }
    }

    private void searchNextParametroAux() {
        if (tokenAtual.getNome().equals(",")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextParametroAux();
        }
    }

    private void searchNextParametros() {
        if (tokenAtual.getNome().equals("bool") || tokenAtual.getNome().equals("float")
                || tokenAtual.getTipo().equals("IDE") || tokenAtual.getNome().equals("int")
                || tokenAtual.getNome().equals("string") || tokenAtual.getNome().equals("struct")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextParametros();
        }
    }

    private void searchNextSimboloUnario() {
        if (tokenAtual.getNome().equals("--") || tokenAtual.getNome().equals("++")) {
        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextSimboloUnario();
        }
    }

    private void searchNextExpressaoIdentificadorVarAux() {
        if (tokenAtual.getNome().equals("=") || tokenAtual.getNome().equals(",") || tokenAtual.getNome().equals(";")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextExpressaoIdentificadorVarAux();
        }
    }

    private void searchNextExpressaoIdentificadoresVarAux() {
        if (tokenAtual.getNome().equals(",") || tokenAtual.getNome().equals(";")) {
        } else if (firstForaDeBloco()) {

        } else if (firstDentroDeBloco()) {

        } else if (proximoToken()) {
            searchNextExpressaoIdentificadoresVarAux();
        }
    }
}
