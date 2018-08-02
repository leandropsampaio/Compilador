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
    private List<Struct> structs;
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
        structs = new ArrayList<>();
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
            System.out.println("888nome"+variavel.getNome());
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
        if (!metodos.contains(c)) {
            metodos.add(c);
            return true;
        }
        return false;
    }
    
    public boolean addStruct(Struct c) {
        if (!structs.contains(c)) {
            structs.add(c);
            return true;
        }
        return false;
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
    
    
    public List<Struct> getStruct() {
        return structs;
    }
    public Struct BuscaStructPorNome(String nome) {
        System.out.println("888BuscarStructGlobal");
        Iterator iterador = structs.listIterator();
        while (iterador.hasNext()) {
            Struct struct = (Struct) iterador.next();
            System.out.println("888nome"+struct.getNome());
            if (struct.getNome().equals(nome)) {
                return struct;
            } else {

            }
        }
        return null;
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
    
    
    
     public boolean verificarHerancaStruct(String nome) {
        System.out.println("888BuscaVariavelConstantePorNome");        
        if(null == this.BuscaStructPorNome(nome)){
           return false;
        }        
        return true;

    }     
     
     public boolean verificarVariaveisHerancas(String nomeStruct1,String nomeStruct2) {
               System.out.println("emMetodoElvis"+nomeStruct1+nomeStruct2);
               Struct s1 = BuscaStructPorNome(nomeStruct1);
               Struct s2 = BuscaStructPorNome(nomeStruct2);
        
                Iterator iterador1 = s1.getVariaveis().listIterator();
                Iterator iterador2 = s2.getVariaveis().listIterator();
                while (iterador1.hasNext() && iterador2.hasNext()) {
                    Variavel variavel1 = (Variavel) iterador1.next();
                    Variavel variavel2 = (Variavel) iterador2.next();
                    System.out.println("variaveismetodo"+variavel1.getNome()+variavel2.getNome());
                    if (variavel1.getTipo().equals(variavel2.getTipo()) && variavel1.getNome().equals(variavel2.getNome())) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
    }
}
