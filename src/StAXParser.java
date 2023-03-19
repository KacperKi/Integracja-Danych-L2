import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

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

    public void readData(){
        while (reader.hasNext()) {

            XMLEvent nextEvent = null;

            try {
                nextEvent = reader.nextEvent();
            } catch (XMLStreamException e) {
                throw new RuntimeException(e);
            }

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("desired")) {
                    //...
                }
            }


            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("website")) {
//                    websites.add(website);
                }
            }
        }

    }



}
