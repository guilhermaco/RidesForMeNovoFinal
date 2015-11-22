package com.ridesforme.ridesforme.basicas;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by Marcos on 05/09/2015.
 */
public class Carona implements Serializable{

    public Integer CaronaId;
    public String EstadoOrigem;
    public String CidadeOrigem;
    public String BairroOrigem;
    public Integer UsuarioID;
    public String RuaOrigem;
    public String EstadoDestino;
    public String CidadeDestino;
    public String BairroDestino;
    public String RuaDestino;

    public String Valor;
    public String DescricaoCarona;

    public Integer getUsuarioID() {
        return UsuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        UsuarioID = usuarioID;
    }

    public String TipoVeiculo;
    public String DescricaoVeiculo;

    public String Vagas;

    public String TipoTrajeto;
    public String TipoOferta;

    public Date DataHoraSaidaIda;
    public Date DataHoraSaidaVolta;

    public String DiaDaSemana;
    public String Status;
    public Integer Classificacao;

    public void setCaronaId(Integer caronaId) {
        CaronaId = caronaId;
    }

    public Integer getClassificacao() {
        return Classificacao;
    }

    public void setClassificacao(Integer classificacao) {
        Classificacao = classificacao;
    }



    public Carona(){}

    public int getCaronaId() {
        return CaronaId;
    }

    public void setCaronaId(int caronaId) {
        CaronaId = caronaId;
    }

    public Date getDataHoraSaidaIda() {
        return DataHoraSaidaIda;
    }

    public void setDataHoraSaidaIda(Date dataHoraSaidaIda) {
        DataHoraSaidaIda = dataHoraSaidaIda;
    }

    public String getEstadoOrigem() {
        return EstadoOrigem;
    }

    public void setEstadoOrigem(String estadoOrigem) {
        EstadoOrigem = estadoOrigem;
    }

    public String getCidadeOrigem() {
        return CidadeOrigem;
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        CidadeOrigem = cidadeOrigem;
    }

    public String getBairroOrigem() {
        return BairroOrigem;
    }

    public void setBairroOrigem(String bairroOrigem) {
        BairroOrigem = bairroOrigem;
    }

    public String getRuaOrigem() {
        return RuaOrigem;
    }

    public void setRuaOrigem(String ruaOrigem) {
        RuaOrigem = ruaOrigem;
    }

    public String getEstadoDestino() {
        return EstadoDestino;
    }

    public void setEstadoDestino(String estadoDestino) {
        EstadoDestino = estadoDestino;
    }

    public String getCidadeDestino() {
        return CidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        CidadeDestino = cidadeDestino;
    }

    public String getBairroDestino() {
        return BairroDestino;
    }

    public void setBairroDestino(String bairroDestino) {
        BairroDestino = bairroDestino;
    }

    public String getRuaDestino() {
        return RuaDestino;
    }

    public void setRuaDestino(String ruaDestino) {
        RuaDestino = ruaDestino;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getDescricaoCarona() {
        return DescricaoCarona;
    }

    public void setDescricaoCarona(String descricaoCarona) {
        DescricaoCarona = descricaoCarona;
    }

    public String getTipoVeiculo() {
        return TipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        TipoVeiculo = tipoVeiculo;
    }

    public String getDescricaoVeiculo() {
        return DescricaoVeiculo;
    }

    public void setDescricaoVeiculo(String descricaoVeiculo) {
        DescricaoVeiculo = descricaoVeiculo;
    }

    public String getVagas() {
        return Vagas;
    }

    public void setVagas(String vagas) {
        Vagas = vagas;
    }

    public String getTipoTrajeto() {
        return TipoTrajeto;
    }

    public void setTipoTrajeto(String tipoTrajeto) {
        TipoTrajeto = tipoTrajeto;
    }

    public String getTipoOferta() {
        return TipoOferta;
    }

    public void setTipoOferta(String tipoOferta) {
        TipoOferta = tipoOferta;
    }

    public Date getDataHoraSaidaVolta() {
        return DataHoraSaidaVolta;
    }

    public Carona(Integer caronaId, String estadoOrigem, String cidadeOrigem, String bairroOrigem, Integer usuarioID, String ruaOrigem, String estadoDestino, String cidadeDestino, String bairroDestino, String ruaDestino, String valor, String descricaoCarona, String tipoVeiculo, String descricaoVeiculo, String vagas, String tipoTrajeto, String tipoOferta, Date dataHoraSaidaIda, Date dataHoraSaidaVolta, String diaDaSemana, String status, Integer classificacao) {
        CaronaId = caronaId;
        EstadoOrigem = estadoOrigem;
        CidadeOrigem = cidadeOrigem;
        BairroOrigem = bairroOrigem;
        UsuarioID = usuarioID;
        RuaOrigem = ruaOrigem;
        EstadoDestino = estadoDestino;
        CidadeDestino = cidadeDestino;
        BairroDestino = bairroDestino;
        RuaDestino = ruaDestino;
        Valor = valor;
        DescricaoCarona = descricaoCarona;
        TipoVeiculo = tipoVeiculo;
        DescricaoVeiculo = descricaoVeiculo;
        Vagas = vagas;
        TipoTrajeto = tipoTrajeto;
        TipoOferta = tipoOferta;
        DataHoraSaidaIda = dataHoraSaidaIda;
        DataHoraSaidaVolta = dataHoraSaidaVolta;
        DiaDaSemana = diaDaSemana;
        Status = status;
        Classificacao = classificacao;
    }

    public void setDataHoraSaidaVolta(Date dataHoraSaidaVolta) {
        DataHoraSaidaVolta = dataHoraSaidaVolta;
    }

    public String getDiaDaSemana() {
        return DiaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        DiaDaSemana = diaDaSemana;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}