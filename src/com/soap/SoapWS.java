package com.soap;

import javax.jws.WebService;
import java.util.ArrayList;

//@WebService(targetNamespace = "com.soap.SoapWS")

@WebService(endpointInterface = "com.soap.SoapInterface")
public class SoapWS implements SoapInterface{

    MySQLConnectorSoap mySQLConnectorSoap;

    public SoapWS() {
        mySQLConnectorSoap = new MySQLConnectorSoap();
    }
    @Override
    public Integer getNumberOfProducer(String producer) {
        System.out.println("Log: " + producer);
        String query = "select count(*) from dane where manufacturer_name = '" + producer + "';";
        return mySQLConnectorSoap.getNumberOfSth(query);
    }

    @Override
    public String[] getDataMatrix(String matrix) {
        try {

            ArrayList<ArrayList<String>> tmp = mySQLConnectorSoap.readDataFromDB(matrix);
            String[] dataToRet = new String[tmp.size()*15];
            int increment=0;

            for(ArrayList<String> se: tmp){
                for(String st: se){
                    dataToRet[increment]=st;
                    increment++;
                }
            }

            return dataToRet;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getNumberOfProportions(String proportions) {
        String query = "select count(*) from dane where screen_resolution = '" + proportions + "';";
        return mySQLConnectorSoap.getNumberOfSth(query);
    }
}
