package analisadorLexico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import analisadorSintatico.Token;

/**
 * Classe responsavél por realizar a análise léxica dos arquivos e gerar a saída
 * de cada análise em um arquivo correspondente.
 *
 * @author Elvis Huges e Leandro Sampaio
 */
public class AnalisadorLexico {

    private StringBuilder arquivoEntrada = new StringBuilder();
    private ArrayList<Token> arrayTokens = new ArrayList<>();
    private Token token;

    /**
     * Método contrutor responsável por transformar o arquivo de entrada em uma
     * stream facilitando a manipulação para a análise léxica
     *
     * @param caminhoEntrada
     */
    public AnalisadorLexico(String caminhoEntrada) {
        try (Stream<String> stream = Files.lines(Paths.get(caminhoEntrada))) {
            stream.forEach(l -> arquivoEntrada.append(l).append(System.lineSeparator()));
        } catch (IOException ex) {
            System.out.println("Arquivo não encontrado: " + caminhoEntrada);
        }
    }

    /**
     * Método responsavél por realizar a análise léxica
     *
     * @param nomeArquivo nome do arquivo de entrada para gerar o nome do
     * arquivo de saida e fazer as operaçõs de escrita
     * @throws Exception Excessão para arquivo não encontrado
     */
    public void analiseLexica(String nomeArquivo) throws Exception {

        FileWriter arquivoSaida = new FileWriter("entrada\\saida\\saida-" + nomeArquivo + ".txt");

        PrintWriter gravar = new PrintWriter(arquivoSaida);

        String errosLexicos = "\n"; //String para armazenar erros léxicos 

        int linhas = 1; // Armazena o numero da linha que estamos fazendo a análise , inicialmente 1
        String expressoes[] = new String[20]; // Array de expressões regex
        String tokens[] = new String[15]; // Array de tokens 

        // Expressões Regex
        String expressaoNumerico = ("(-(\\x09|\\x0A|\\x0D|\\x20)*)?\\d+(\\.\\d+)?");
        String expressaoPalavraReservada = "\\b(const|var|struct|typedef|procedure|function|return|start|if|then|else|while|scan|print|int|float|bool|string|true|false|extends)\\b";
        String expressaoAritmerico = "\\-\\-|\\-|\\+\\+|\\+|\\*|\\/";
        String expressaoIdentificador = "[a-zA-Z]([a-zA-Z]|(\\d)|(_))*";
        String expressaoDelimitador = "\\;|\\,|\\(|\\)|\\[|\\]|\\{|\\}|\\.";
        String expressaoComentario = "(\\/\\*)(.|\\s)*?(\\*\\/)";
        String expressaoErroComentarioBloco = "\\/\\*(.|\r\\\n)*";
        String expressaoCadeiaCaracteres = ("\"([a-zA-Z]|\\d|\\x5C\\x22|\\x20|\\x21|[\\x23-\\x7E])*\"");
        String expressaoRelacional = "\\!\\=|\\=\\=|\\>\\=|\\<\\=|\\<|\\>|\\=";
        String expressaoLogico = "\\!|\\&\\&|\\|\\|";
        String expressaoErroSimbolo = "([\\x00-\\x08]|[\\x0E-\\x1F]|[\\x7F-\\xFF]|\\x0B|\\x0C|\\x3A|\\x25|\\x23|\\x24|\\x26|\\x27|\\x3F|\\x40|\\x5C|\\x5E|\\x5F|\\x60|\\x7C|\\x7E)";   // removi o + dessa regex
        String expressaoEspaco = "(\\x09|\\x20)+";
        String expressaoErroCadeiaCaractere = ("\".*\\r\\n");
        String expressaoQuebraLinha = "\\x0D\\x0A|\\x0A\\x0D";
        String expressaoComentarioBarra = "\\/\\/.*\\r\\n";

        // Nomes dos tokens
        String tokenNumerico = "NRO";
        String tokenAritmerico = "ART";
        String tokenIdentificador = "IDE";
        String tokenDelimitador = "DEL";
        String tokenComentario = "comentario";
        String tokenCadeiaCaractere = "CAD";
        String tokenErroSimbolico = "simbolo_invalido";
        String tokenPalavraReservada = "PRE";
        //String tokenEspaco = "Espaço";
        String tokenErroCadeiraCaractere = "cadeia_mal_formada";
        //String tokenExpressaoQuebraLinha = "quebra de linha";
        String tokenExpressaoComentarioBarra = "comentarioBarra";
        String tokenExpressaoRelacional = "REL";
        String tokenExpressaoLogico = "LOG";
        String tokenExpressaoErroComentarioBloco = "comentario_mal_formado";

        // Preenchendo o array de expressões com as Strings de expressões criadas anteriormente
        expressoes[0] = expressaoPalavraReservada;
        expressoes[1] = expressaoComentarioBarra;
        expressoes[2] = expressaoComentario;
        expressoes[3] = expressaoNumerico;
        expressoes[4] = expressaoErroComentarioBloco;
        expressoes[5] = expressaoCadeiaCaracteres;
        expressoes[6] = expressaoRelacional;
        expressoes[7] = expressaoDelimitador;
        expressoes[8] = expressaoEspaco;
        expressoes[9] = expressaoErroCadeiaCaractere;
        expressoes[10] = expressaoQuebraLinha;
        expressoes[11] = expressaoIdentificador;
        expressoes[12] = expressaoLogico;
        expressoes[13] = expressaoErroSimbolo;
        expressoes[14] = expressaoAritmerico;

        // Preenchendo o array de tokens com as strings de tokens criadas anteriormente
        tokens[0] = tokenPalavraReservada;
        tokens[1] = tokenExpressaoComentarioBarra;
        tokens[2] = tokenComentario;
        tokens[3] = tokenNumerico;
        tokens[4] = tokenExpressaoErroComentarioBloco;
        tokens[5] = tokenCadeiaCaractere;
        tokens[6] = tokenExpressaoRelacional;
        tokens[7] = tokenDelimitador;
        //tokens[8] = tokenEspaco;
        tokens[9] = tokenErroCadeiraCaractere;
        //tokens[10] = tokenExpressaoQuebraLinha;
        tokens[11] = tokenIdentificador;
        tokens[12] = tokenExpressaoLogico;
        tokens[13] = tokenErroSimbolico;
        tokens[14] = tokenAritmerico;

        // Teste para imprimir a quantidade de caracteres de entrada
        //System.out.println(arquivoEntrada.length());
        /**
         * While utilizado para pecorrer o arquivo de entrada
         *
         */
        while (arquivoEntrada.length() > 2) {

            for (int i = 0; i < 15; i++) { // percorrendo todos os regex criados contido no array 

                // Pattern utilizado para compilar a expressão do regex
                Pattern pattern = Pattern.compile(expressoes[i]);
                // Matcher utilizado verificar se a entrada combina com a expressão passada anteriormente
                Matcher matcher = pattern.matcher(arquivoEntrada.toString());

                if (matcher.lookingAt()) { // Caso algum padrão regex de expressoes[i] é encontrado no matcher de arquivoEntrada.toString()

                    if (tokens[i] != null) { // Caso não seja null
                        if (i != 1 && i != 2) {   // Caso não seja comentário de bloco e linha

                            if (i == 4 || i == 9 || i == 13) { // Caso seja algum regex de erro deve-se preencher a string de erro lexico

                                if (i == 13) {
                                    errosLexicos = errosLexicos + String.format("%02d", linhas) + " " + tokens[i] + " " + matcher.group() + "\n";
                                } else {
                                    errosLexicos = errosLexicos + String.format("%02d", linhas) + " " + tokens[i] + " " + matcher.group();
                                }
                            } else if (matcher.group().contains("\n") && i != 3) { // caso não seja erro deve-se gravar no arquivo
                                gravar.print(String.format("%02d", linhas) + " " + tokens[i] + " " + matcher.group());
                                token = new Token(matcher.group(), tokens[i], linhas);
                                arrayTokens.add(token);
                            } else {
                                gravar.print(String.format("%02d", linhas) + " " + tokens[i] + " " + matcher.group() + "\n");
                                token = new Token(matcher.group(), tokens[i], linhas);
                                arrayTokens.add(token);
                                //  System.out.println(linhas + " " + tokens[i] + " " + matcher.group() + "\n");
                            }
                        }
                    }

                    /**
                     * Caso alguma entrada contenha quebras de linhas (\n),
                     * deve-se contar quantas quebras tem no mesmo para saber a
                     * posição da linha
                     */
                    if (i == 1 || i == 2 || i == 3 || i == 9 || i == 10) {
                        String s = matcher.group();

                        if (s.contains("\n")) {
                            int quantidade = s.length() - s.replaceAll("\n", "").length();
                            linhas = linhas + quantidade;
                        }
                    }
                    // Remove o padrão encontrado para retornar o while, diminuindo a string que representa o arquivo  
                    arquivoEntrada.delete(0, matcher.end());

                    i = -1; // Resetando o i para garantir a passagem em todos tokens               

                }
            }

        }

        /*
           Caso a string de erros (errosLexicos) não tenha sido preenchida gera-se mensagem de sucesso
           no arquivo de saída, caso contrario imprime a string dos erros léxicos.
         */
        if (errosLexicos.equals("\n")) {
            gravar.printf("\nSucesso, código pronto para a análise semântica!");
        } else {
            gravar.print(errosLexicos);
        }
        // Fechamento da escrita do arquivo
        gravar.close();
    }

    public ArrayList getTokens() {
        return arrayTokens;
    }
}
