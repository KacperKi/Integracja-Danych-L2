package com.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import java.util.ArrayList;


@WebService
@SOAPBinding(style = Style.RPC)
public interface SoapInterface {
    @WebMethod Integer getNumberOfProducer(String producer);
    @WebMethod
    ArrayList<ArrayList<String>> getDataMatrix(String matrix);
    @WebMethod Integer getNumberOfProportions(String proportions);

}
