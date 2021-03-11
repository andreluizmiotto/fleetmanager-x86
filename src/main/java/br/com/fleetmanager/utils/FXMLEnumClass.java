package br.com.fleetmanager.utils;

import java.util.AbstractMap;
import java.util.HashMap;

public class FXMLEnumClass {

    private static final HashMap<FXMLEnum, AbstractMap.SimpleEntry<String, String>> FXMLMapEnum = new HashMap<>();

    static {
        FXMLMapEnum.put(FXMLEnum.MAINWINDOW, new AbstractMap.SimpleEntry<>("mainWindow", "Gerenciador de Frota"));
        FXMLMapEnum.put(FXMLEnum.LISTVEHICLE, new AbstractMap.SimpleEntry<>("listVehicle", "Veículos"));
    }

    public static String getFXMLFile(FXMLEnum pFXML) {
        return FXMLMapEnum.get(pFXML).getKey();
    }

    public static String getFXMLTittle(FXMLEnum pFXML) {
        return FXMLMapEnum.get(pFXML).getValue();
    }

}
