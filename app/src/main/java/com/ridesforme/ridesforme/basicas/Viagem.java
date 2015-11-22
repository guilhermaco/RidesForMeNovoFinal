package com.ridesforme.ridesforme.basicas;

/**
 * Created by Felipe on 03/11/2015.
 */
public class Viagem {
    private Integer ViagemID;
    private Integer UsuarioID;
    private Integer CaronaID;
    private Integer Classificacao;

    public Integer getViagemID() {
        return ViagemID;
    }

    public void setViagemID(Integer viagemID) {
        ViagemID = viagemID;
    }

    public Integer getUsuarioID() {
        return UsuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        UsuarioID = usuarioID;
    }

    public Integer getCaronaID() {
        return CaronaID;
    }

    public void setCaronaID(Integer caronaID) {
        CaronaID = caronaID;
    }

    public Integer getClassificacao() {
        return Classificacao;
    }

    public void setClassificacao(Integer classificacao) {
        Classificacao = classificacao;
    }
}
