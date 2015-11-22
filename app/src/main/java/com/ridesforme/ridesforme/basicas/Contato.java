package com.ridesforme.ridesforme.basicas;

/**
 * Created by Marcos on 08/09/2015.
 */
public class Contato {

    public String nomeContato;
    public String emailContato;
    public String mensagemContato;
    public String tipoContato;
    public float classificacaoApp;


    public Contato(){}

    public Contato(String nome, String email, String mensagem, String tipo){
        this.nomeContato = nome;
        this.emailContato = email;
        this.mensagemContato = mensagem;
        this.tipoContato = tipo;
    }

    public Contato(String nome, String email, String mensagem, String tipo, float classificacaoApp){
        this.nomeContato = nome;
        this.emailContato = email;
        this.mensagemContato = mensagem;
        this.tipoContato = tipo;
        this.classificacaoApp = classificacaoApp;
    }





}
