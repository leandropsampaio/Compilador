/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantico;

import analisadorSintatico.Token;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Leandro
 */
public class Struct {

    
    private String nome;
    private int linha;
    private List<Variavel> variaveis;
    

    public Struct() {
        this.variaveis = new ArrayList<>();
    }

    /**
     * Construtor para caso o metodo possua parametros
     *
     * @param tipo
     */
    public Struct(String nome) {
        this.nome = nome;
        this.variaveis = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }
    

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public int getLinha() {
        return linha;
    }
    

    public void setLinha(int linha) {
        this.linha = linha;
    }

    /**
     * Adiciona uma variavel caso não exista nesse metodo
     *
     * @param v
     * @return
     */
    public boolean addVariavel(Variavel v) {
        if (!variaveis.contains(v)) {
            variaveis.add(v);
            return true;
        }
        return false;
    }
    
    

    public List<Variavel> getVariaveis() {
        return variaveis;
    }
    
    public void addListaVariaveis(List<Variavel> variaveis) {
            this.variaveis.addAll(variaveis);
    }

    public boolean BuscarVariavelPorNome(String nome){
        Iterator iterador = variaveis.listIterator();
        while (iterador.hasNext()) {
            Variavel variavel = (Variavel) iterador.next();
            System.out.println("888nome"+variavel.getNome());
            if (variavel.getNome().equals(nome)) {
                return true;
            } else {

            }
        }
        return false;
    }

    /**
     * Verifica se os metodos são iguais: tem o mesmo nome
     *
     * @param o
     * @return
     */
    /*
    @Override
    public boolean equals(Object o) {
        if (o instanceof Metodo) {
            Metodo m = ((Metodo) o);
            System.out.println("#####");
            System.out.println(nome);
            System.out.println(m.getNome());
            if (m.getNome().equals(nome)) { //verifica se os nomes são iguais
                Iterator iterador = this.parametros.listIterator();
                Iterator iterador2 = m.getParametros().listIterator();
                while (iterador.hasNext()) {
                    Variavel parametro = (Variavel) iterador.next();
                    Variavel parametro2 = (Variavel) iterador2.next();
                    if (parametro.getTipo().equals(parametro2.getTipo())) {
                    } else {
                        return false;
                    }
                }

                return true;

            }
        }
        return false;
    }
    */
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nome);
        return hash;
    }

}
