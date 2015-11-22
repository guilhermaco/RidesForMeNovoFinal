package com.ridesforme.ridesforme.basicas;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Marcos on 05/09/2015.
 */
public class Usuario implements Serializable{

    public String nome;
    public String senha;
    public String confirmarSenha;
    public Date dataNascimento;
    public String sexo;
    public String empresa;
    public String email;
    public String telefone;
    public long matricula;



    public Usuario(){}


    public Usuario(String nome, String senha, String confirmar, Date dataNascimento, String sexo, String empresa, String email, String telefone){
        this.nome = nome;
        this.senha = senha;
        this.confirmarSenha = confirmar;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.empresa = empresa;
        this.email = email;
        this.telefone = telefone;

    }

    public Usuario(long id, String nome, String senha, String confirmar, Date data, String sexo, String empresa, String email, String telefone){
        this.matricula = id;
        this.nome = nome;
        this.senha = senha;
        this.confirmarSenha = confirmar;
        this.dataNascimento = data;
        this.sexo = sexo;
        this.empresa = empresa;
        this.email = email;
        this.telefone = telefone;

    }
}
