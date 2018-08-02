/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Leandro
 */
public class Global {

    private List<Variavel> variaveis;
    private List<Classe> classes;
    private List<Metodo> metodos;
    private static Global instance;

    /**
     * Construtor da classe
     */
    private Global() {
        variaveis = new ArrayList<>();
        classes = new ArrayList<>();
        metodos = new ArrayList<>();
    }

    public static Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    /**
     * Adiciona uma variavel caso não exista
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

    /**
     * Busca uma variavel pelo nome
     *
     * @param c
     * @return
     */
    public boolean BuscaVariavelConstantePorNome(String nome) {
        System.out.println("888BuscaVariavelConstantePorNome");
        Iterator iterador = variaveis.listIterator();
        while (iterador.hasNext()) {
            Variavel variavel = (Variavel) iterador.next();
            System.out.println("888nome" + variavel.getNome());
            if (variavel.getNome().equals(nome) && variavel.isConstante()) {
                return true;
            } else {

            }
        }
        return false;

    }

    public boolean addClasse(Classe c) {
        if (!classes.contains(c)) {
            classes.add(c);
            return true;
        }
        return false;
    }

    public boolean addMetodo(Metodo c) {
        System.out.println("aaa");
        if (!metodos.contains(c)) {
            metodos.add(c);
            return true;
        }
        return false;
    }

    public boolean contemMetodo(Metodo c) {
        return metodos.contains(c);
    }

    /**
     * Retorna uma classe caso exista, caso não retorna null
     *
     * @param identificador
     * @return
     */
    public Classe getClasse(String identificador) {
        for (Classe c : classes) {
            if (c.getNome().equals(identificador)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Retorna uma metodo caso exista, caso não retorna null
     *
     * @param identificador
     * @return
     */
    public Metodo getMetodo(String identificador) {
        for (Metodo c : metodos) {
            if (c.getNome().equals(identificador)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Retorna uma variavel caso exista, caso não retorna null
     *
     * @param identificador
     * @return
     */
    public Variavel getVariavel(String identificador) {
        for (Variavel v : variaveis) {
            if (v.getNome().equals(identificador)) {
                return v;
            }
        }
        return null;
    }

    public List<Classe> getClasses() {
        return classes;
    }

    public List<Metodo> getMetodos() {
        return metodos;
    }
}
