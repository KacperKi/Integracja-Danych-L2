package com.soap;

import javax.jws.WebService;

//@WebService(targetNamespace = "com.soap.SoapWS")

@WebService(endpointInterface = "com.soap.SoapInterface")
public class SoapWS implements SoapInterface{

    @Override
    public String getHelloWorldAsString(String name) {
        return "XE";
    }

    @Override
    public long getDaysBetweenDates(String date1, String date2) {
        return 0;
    }

    @Override
    public String getHelloWorld() {
        return "Hello World";
    }
}
