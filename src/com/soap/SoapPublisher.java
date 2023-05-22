package com.soap;

import javax.xml.ws.Endpoint;
import java.io.InputStream;

public class SoapPublisher {
    Process ps;
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:7779/ws/first", new SoapWS());
//        SoapPublisher soapPublisher = new SoapPublisher();
    }

    public void SoapPublisher(){
        Endpoint.publish("http://localhost:7779/ws/first", new SoapWS());
//        try{
//            String path = "D:\\Studia\\Magisterskie\\3 semestr\\L_IntegracjaSystemow\\L6\\L6_2\\projekt1\\RestServerApp\\RestApi.jar";
//            ps=Runtime.getRuntime().exec(new String[]{"java","-jar",path});
//
//            System.out.println("Serwer SOAP Started - port 7779" +
//                    "Serwer REST Started - port 8081");
//        }catch(Exception e){
//            //de
//        }
    }




}
