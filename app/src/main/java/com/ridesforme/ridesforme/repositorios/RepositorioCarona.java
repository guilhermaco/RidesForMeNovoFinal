package com.ridesforme.ridesforme.repositorios;
import com.ridesforme.ridesforme.basicas.Carona;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by Marcos Antônio on 29/09/2015.
 */
public class RepositorioCarona implements Serializable{

    private List<Carona> mCaronas = new ArrayList<>();

    private static RepositorioCarona inst = null;


    private RepositorioCarona(){

    }

    public static RepositorioCarona getSingleton(){
        if(inst == null){
            inst = new RepositorioCarona();
        }

        return inst;
    }





    public void cadastrarCarona(Carona carona){
        mCaronas.add(carona);
    }

    public List<Carona> loadCaronas(){
        return mCaronas;
    }

    public Carona addCaronaMentira () {
        Carona c = new Carona();
        c.CaronaId = 1;
        c.setRuaOrigem("av recife");
        c.setCidadeOrigem("Recife");
        return c;
    }

    public List<Carona> loadCaronas(String endereco, String enderecoDestino){

        List<Carona> caronas = new ArrayList<>();
        //caronas.add(addCaronaMentira());

        for(Carona carona : mCaronas){
            if(carona.RuaOrigem.equals(endereco) && carona.RuaDestino.equals(enderecoDestino)){
                caronas.add(carona);
                break;
            }
        }

        return caronas;
    }


}
