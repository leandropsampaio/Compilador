package analisadorSintatico;

/**
 * Essa classe encapsula as informações dos lexemas identificados, as suas
 * respectivas classes e a linha que se encontram.
 *
 * @author Leandro e Elvis
 */
public class Token {

    private String nome;
    private String tipo;
    private int linha;

    /**
     * Método construtor.
     *
     * @param nome - Identificação do Lexema
     * @param tipo - Token ao qual pertence
     * @param linha - Linha que está
     */
    public Token(String nome, String tipo, int linha) {
        this.nome = nome;
        this.tipo = tipo;
        this.linha = linha;
    }

    /**
     * Método para obter o nome.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método para obter o tipo.
     *
     * @return tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Método para obter a linha.
     *
     * @return linha
     */
    public int getLinha() {
        return linha;
    }

    @Override
    public String toString() {
        return "< " + nome + " >, < " + tipo + " >, Linha: " + linha;
    }
}
