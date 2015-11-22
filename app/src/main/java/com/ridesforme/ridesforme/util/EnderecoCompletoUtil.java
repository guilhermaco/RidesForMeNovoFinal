package com.ridesforme.ridesforme.util;

/**
 * Created by Felipe on 14/10/2015.
 */
public class EnderecoCompletoUtil {
    private String endereco;
    private String numero;
    private String cidade;
    private String bairro;

    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
