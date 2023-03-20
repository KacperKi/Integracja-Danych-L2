import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class StAXParser {
    XMLInputFactory xmlInputFactory;
    XMLEventReader reader;
    public StAXParser(String pathToFile) throws FileNotFoundException {
        this.xmlInputFactory = XMLInputFactory.newInstance();
        try {
            this.reader = xmlInputFactory.createXMLEventReader(new FileInputStream(pathToFile));
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Laptop> readDatafromFile() throws XMLStreamException {
        ArrayList<Laptop> listOfLaptop = new ArrayList<>();
        Laptop newLaptop = new Laptop();

        while (reader.hasNext()) {
            XMLEvent nextEvent = null;
            try {
                nextEvent = reader.nextEvent();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();

                XMLEvent next = reader.nextEvent();

                switch(startElement.getName().getLocalPart()) {
                    case "manufacturer":

                        if(!next.isEndElement()) {
                            newLaptop.setProducentLaptopa(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setProducentLaptopa("brak");
                        }
                        break;
                    case "screen":
                        Iterator<Attribute> t = startElement.getAttributes();
                        int i = 0;
                        while(t.hasNext()) {
                            i++;
                            t.next();
                        }
                        if(i==0)
                        {
                            newLaptop.setCzyDotykowyEkran( "brak");
                        }else{
                            newLaptop.setCzyDotykowyEkran(
                                    startElement.getAttributeByName(
                                            new QName("touch")).getValue()
                            );
                        }
                        break;
                    case "size":
                        if(!next.isEndElement()) {
                            newLaptop.setPrzekatnaEkranu(
                                    next.toString()
                                );
                        }else{
                            newLaptop.setPrzekatnaEkranu("brak");
                        }
                        break;
                    case "resolution":
                        if(!next.isEndElement()) {
                            newLaptop.setRozdzielczoscEkranu(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setRozdzielczoscEkranu("brak");
                        }
                        break;
                    case "type":
                        if(!next.isEndElement()) {
                            newLaptop.setRodzajPowierzchni(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setRodzajPowierzchni("brak");
                        }
                        break;
                    case "name": //grafika i procesor
                        //check - processor first
                        if(!next.isEndElement()) {
                            if(
                                    newLaptop.getNazwaProcesora() == null
                                    || newLaptop.getNazwaProcesora().equals("")
                                ){
                                newLaptop.setNazwaProcesora(
                                        next.toString()
                                );
                            }
                            else{
                                newLaptop.setNazwaGrafiki(
                                        next.toString()
                                );
                            }
                        }else{
                            newLaptop.setNazwaGrafiki("brak");
                        }
                        break;
                    case "physical_cores":
                        if(!next.isEndElement()) {
                            newLaptop.setLiczbaRdzeni(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setLiczbaRdzeni("brak");
                        }
                        break;
                    case "clock_speed":
                        if(!next.isEndElement()) {
                            newLaptop.setPredkoscProcesora(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setPredkoscProcesora("brak");
                        }
                        break;
                    case "ram":
                        if(!next.isEndElement()) {
                            newLaptop.setPamiecRam(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setPamiecRam("brak");
                        }
                        break;
                    case "disc":
                        Iterator<Attribute> k = startElement.getAttributes();
                        int j = 0;
                        while(k.hasNext()) {
                            j++;
                            k.next();
                        }
                        if(j==0){
                            newLaptop.setRodzajDysku("brak");
                        }else{
                            newLaptop.setRodzajDysku(
                                startElement.getAttributeByName(
                                            new QName("type")
                                        ).getValue()
                            );
                        }
                        break;
                    case "storage":
                        if(!next.isEndElement()) {
                                newLaptop.setPojemnoscDysku(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setPojemnoscDysku("brak");
                        }
                        break;
                    case "memory":
                        if(!next.isEndElement()) {
                            newLaptop.setPamiecGrafiki(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setPamiecGrafiki("brak");
                        }
                        break;
                    case "os":
                        if(!next.isEndElement()) {
                            newLaptop.setNazwaOs(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setNazwaOs("brak");
                        }
                        break;
                    case "disc_reader":
                        if(!next.isEndElement()) {
                            newLaptop.setNapedFizyczny(
                                    next.toString()
                            );
                        }else{
                            newLaptop.setNapedFizyczny("brak");
                        }
                        break;
                    default:
                            break;
                }
            }
            //end of single element of list
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("laptop")) {
                    listOfLaptop.add(newLaptop);
                    newLaptop = new Laptop();
                }

            }
        }
        return listOfLaptop;
    }



}
