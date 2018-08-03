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
public class Metodo {

    private String tipo;
    private String nome;
    private List<Variavel> variaveis;
    private List<Variavel> parametros;
    private List<Struct> structs;

    public Metodo() {
        this.variaveis = new ArrayList<>();
        this.structs = new ArrayList<>();
    }

    /**
     * Construtor para caso o metodo possua parametros
     *
     * @param tipo
     */
    public Metodo(String tipo) {
        this.tipo = tipo;
        this.variaveis = new ArrayList<>();
        this.structs = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public Variavel getVariavel(String identificador, boolean isVetor) {
        for (Variavel v : variaveis) {
            if (v.getNome().equals(identificador) && v.getIsVetor() == isVetor) {
                return v;
            }
        }
        return null;
    }

    public Struct getStruct(String identificador) {
        for (Struct s : structs) {
            if (s.getNome().equals(identificador)) {
                return s;
            }
        }
        return null;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setParametros(List<Variavel> parametros) {
        this.parametros = parametros;
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

    public boolean addStruct(Struct v) {
        if (!structs.contains(v)) {
            structs.add(v);
            return true;
        }
        return false;
    }

    public Struct BuscaStructPorNome(String nome) {
        System.out.println("888BuscaVariavelConstantePorNome");
        Iterator iterador = structs.listIterator();
        while (iterador.hasNext()) {
            Struct struct = (Struct) iterador.next();
            System.out.println("888nome" + struct.getNome());
            if (struct.getNome().equals(nome)) {
                return struct;
            } else {

            }
        }
        return null;
    }

    public boolean verificarHerancaStruct(String nome) {
        System.out.println("888BuscaVariavelConstantePorNome");
        if (null == this.BuscaStructPorNome(nome)) {
            return false;
        }
        return true;

    }

    public Variavel BuscaVariavelPorNome(String nome) {
        System.out.println("888BuscarStructGlobal");
        Iterator iterador = variaveis.listIterator();
        while (iterador.hasNext()) {
            Variavel variavel = (Variavel) iterador.next();
            //System.out.println("888nome"+variaveis.getNome());
            if (variavel.getNome().equals(nome)) {
                return variavel;
            } else {

            }
        }
        return null;
    }

    public boolean verificarVariaveisHerancas(String nomeStruct1, String nomeStruct2) {
        System.out.println("emMetodoElvis" + nomeStruct1 + nomeStruct2);
        Struct s1 = BuscaStructPorNome(nomeStruct1);
        Struct s2 = BuscaStructPorNome(nomeStruct2);

        Iterator iterador1 = s1.getVariaveis().listIterator();
        Iterator iterador2 = s2.getVariaveis().listIterator();
        while (iterador1.hasNext() && iterador2.hasNext()) {
            Variavel variavel1 = (Variavel) iterador1.next();
            Variavel variavel2 = (Variavel) iterador2.next();
            System.out.println("variaveismetodo" + variavel1.getNome() + variavel2.getNome());
            if (variavel1.getTipo().equals(variavel2.getTipo()) && variavel1.getNome().equals(variavel2.getNome())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public List<Variavel> getVariaveis() {
        return variaveis;
    }

    public List<Struct> getStruct() {
        return structs;
    }

    public List<Variavel> getParametros() {
        return parametros;
    }

    /**
     * Verifica se os metodos são iguais: tem o mesmo nome
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Metodo) {
            Metodo m = ((Metodo) o);
            System.out.println("#####EQUALS");
            System.out.println(nome);
            System.out.println(m.getNome());
            if (m.getNome().equals(nome)) { //verifica se os nomes são iguais
                if (parametros != null) {
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
                }

                return true;

            }
        }
        System.out.println("aaab");
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nome);
        return hash;
    }

}
