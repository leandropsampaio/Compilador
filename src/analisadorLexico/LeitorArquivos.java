package analisadorLexico;

import java.io.File;
import java.util.ArrayList;
import analisadorSintatico.AnalisadorSintatico1;
import analisadorSintatico.Token;
import java.io.IOException;
import java.util.Iterator;

/**
 * Classe responsável por dar início a análise lendo todos arquivos dentro da
 * pasta "entrada", localizada na raiz do projeto, e invocando AnalisadorLexico
 * para cada arquivo lido.
 *
 * @author Elvis Huges e Leandro Sampaio
 */
public class LeitorArquivos {

    public static void main(String args[]) throws Exception {

        AnalisadorLexico analisadorLexico;
        File pastaArquivos = new File("entrada\\");
        File[] listaArquivos = pastaArquivos.listFiles(); // lista de arquivos lidos na pasta "entrada"

        /**
         * Verifica-se o tamanho da lista de arquivos contidos na listaArquivos
         * e para cada arquivos realiza-se a análise léxica propriamente dita.
         */
        for (int i = 0; i < listaArquivos.length; i++) {
            if (listaArquivos[i].isFile()) {
                analisadorLexico = new AnalisadorLexico("entrada\\" + listaArquivos[i].getName());
                analisadorLexico.analiseLexica(listaArquivos[i].getName());
                analiseSintatica(analisadorLexico.getTokens(), listaArquivos[i].getName());
            }
        }
        System.out.println("Análise Concluída com Sucesso!");
    }

    private static void analiseSintatica(ArrayList tokens, String nomeArquivo) throws IOException {
        System.out.println(nomeArquivo);
        System.out.println("---------------------------");
        Iterator iterador = tokens.listIterator();
        while(iterador.hasNext()){
            Token token = (Token) iterador.next();
            System.out.println(token.getTipo());
        }
        System.out.println("---------------------------");
        AnalisadorSintatico1 analisadorSintatico = new AnalisadorSintatico1();
        analisadorSintatico.iniciar(tokens, nomeArquivo);
    }
}
