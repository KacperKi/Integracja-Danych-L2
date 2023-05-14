package com.soap;

import javax.xml.ws.Endpoint;

public class SoapPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:7779/ws/first", new SoapWS());
        System.out.println("# - SOAP Server Started");
    }
}
