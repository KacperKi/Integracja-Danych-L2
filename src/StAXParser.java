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

    public ArrayList<Laptop> readData() throws XMLStreamException {
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

                switch(startElement.getName().getLocalPart()) {
                    case "manufacturer":
                        newLaptop.setProducentLaptopa(
                                reader.nextEvent().toString()
                            );
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
                        newLaptop.setPrzekatnaEkranu(
                                reader.nextEvent().toString()
                            );
                        break;
                    case "resolution":
                        newLaptop.setRozdzielczoscEkranu(
                                reader.nextEvent().toString()
                        );
                        break;
                    case "type":
                        newLaptop.setRodzajPowierzchni(
                                reader.nextEvent().toString()
                        );
                        break;
                    case "name": //grafika i procesor
                        //check - processor first
                        if(
                                newLaptop.getNazwaProcesora() == null
                                || newLaptop.getNazwaProcesora().equals("")
                            ){
                            newLaptop.setNazwaProcesora(
                                    reader.nextEvent().toString()
                            );
                        }
                        else{
                            newLaptop.setNazwaGrafiki(
                                    reader.nextEvent().toString()
                            );
                        }
                        break;
                    case "physical_cores":
                        newLaptop.setLiczbaRdzeni(
                                reader.nextEvent().toString()
                        );
                        break;
                    case "clock_speed":
                        newLaptop.setPredkoscProcesora(
                                reader.nextEvent().toString()
                        );
                        break;
                    case "ram":
                        newLaptop.setPamiecRam(
                                reader.nextEvent().toString()
                        );
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
                        newLaptop.setPojemnoscDysku(
                                reader.nextEvent().toString()
                        );
                        break;
                    case "memory":
                        newLaptop.setPamiecGrafiki(
                                reader.nextEvent().toString()
                        );
                        break;
                    case "os":
                        newLaptop.setNazwaOs(
                                reader.nextEvent().toString()
                        );
                        break;
                    case "disc_reader":
                        newLaptop.setNapedFizyczny(
                                reader.nextEvent().toString()
                        );
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


        return null;
    }
}
