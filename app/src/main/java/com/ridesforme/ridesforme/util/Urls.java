package com.ridesforme.ridesforme.util;

/**
 * Created by Robson on 26/10/2015.
 */
public class Urls {
    //ftp://ridesforme.no-ip.info/

    public static final String webservice = "http://ridesforme.no-ip.info:8080";

    //Metodos webservice
    public static final String AllCarona = webservice+"/rpg/carona/getAllCarona";
    public static final String login = webservice+"/rpg/usuario/login";
    public static final String cadastroUsuario = webservice+"/rpg/usuario/cadastrarUsuario";
    public static final String cadastrarViagem = webservice+"/rpg/carona/cadastrarViagem";

    //Consultas Caronas com filtros
    public static final String getCaronaFiltroBairroAll = webservice+"/rpg/caronafiltro/getCaronaFiltroBairroAll";
    public static final String getCaronaFiltroBairroPaga = webservice+"/rpg/caronafiltro/getCaronaFiltroBairroPaga";
    public static final String getCaronaFiltroBairroGratis = webservice+"/rpg/caronafiltro/getCaronaFiltroBairroGratis";
    public static final String getCaronaFiltroBairroDataGratis = webservice+"/rpg/caronafiltro/getCaronaFiltroBairroDataGratis";
    public static final String getCaronaFiltroBairroDataPago = webservice+"/rpg/caronafiltro/getCaronaFiltroBairroDataPago";
    public static final String getCaronaFiltroBairroDataTudo = webservice+"/rpg/caronafiltro/getCaronaFiltroBairroDataTudo";






    public static final String getCaronaFiltroData = webservice+"/rpg/carona/getCaronaFiltroData";
    public static final String getCaronaFiltroDataHora = webservice+"/rpg/carona/getCaronaFiltroDataHora";
}
