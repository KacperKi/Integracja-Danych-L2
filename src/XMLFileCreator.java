import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class XMLFileCreator {

    ArrayList<ArrayList<String>> dataFromTable;
    JTable tableWithData;

    public XMLFileCreator(JTable table) {
        this.tableWithData = table;
        dataFromTable = new ArrayList<>();
        collectData();
    }

    void collectData(){
        for(int i=0; i<tableWithData.getRowCount(); i++){
            ArrayList<String> row = new ArrayList<>();
            for(int j=0; j<tableWithData.getColumnCount();j++){
                row.add(tableWithData.getModel().getValueAt(i,j).toString());
            }
            this.dataFromTable.add(row);
        }
    }

    boolean saveToFile(String pathToExportFile){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            // add elements to Document
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


            //add root element to document
            Element rootElement = doc.createElement("laptops");
            rootElement.setAttribute("moddate", dtf.format(now));
            doc.appendChild(rootElement);


            int counter = 1;
            for (ArrayList<String> row : dataFromTable) {
                rootElement.appendChild(createLaptopElement(doc, row, counter));
                counter++;
            }

            // for output to file, console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            // write to console or file
            StreamResult console = new StreamResult(System.out);
            StreamResult file = new StreamResult(new File(pathToExportFile));

            // write data
            transformer.transform(source, console);
            transformer.transform(source, file);


        }catch (Exception e){
            System.out.println(e + " ----- saveTofileFunction");
            return false;
        }

        return false;
    }





    private static Node createLaptopElement(Document doc, ArrayList<String> row, Integer counter) {
        Element laptop = doc.createElement("laptop");
        laptop.setAttribute("id", counter.toString());

        laptop.appendChild(createElements(doc, laptop, "manufacturer", row.get(0)));

        Element screen = doc.createElement("screen");
            screen.setAttribute("touch", row.get(4));

            Element size = doc.createElement("size");
            size.appendChild(doc.createTextNode(row.get(1)));
            screen.appendChild(size);

            Element resolution = doc.createElement("resolution");
            resolution.appendChild(doc.createTextNode(row.get(2)));
            screen.appendChild(resolution);

            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode(row.get(3)));
            screen.appendChild(type);

        laptop.appendChild(screen);

        Element processor = doc.createElement("processor");

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(row.get(5)));

            Element physical_cores = doc.createElement("physical_cores");
            physical_cores.appendChild(doc.createTextNode(row.get(6)));

            Element clock_speed = doc.createElement("clock_speed");
            clock_speed.appendChild(doc.createTextNode(row.get(7)));

            processor.appendChild(name);
            processor.appendChild(physical_cores);
            processor.appendChild(clock_speed);
        laptop.appendChild(processor);

        laptop.appendChild(createElements(doc, laptop, "ram", row.get(8)));

        Element disc = doc.createElement("disc");
            disc.setAttribute("type", row.get(10));

            Element storage = doc.createElement("storage");
            storage.appendChild(doc.createTextNode(row.get(9)));

            disc.appendChild(storage);
        laptop.appendChild(disc);

        Element graphic_card = doc.createElement("graphic_card");
            graphic_card.appendChild(createElements(doc, graphic_card, "name", row.get(11)));
            graphic_card.appendChild(createElements(doc, graphic_card, "memory", row.get(12)));
        laptop.appendChild(graphic_card);

        laptop.appendChild(createElements(doc, laptop, "os", row.get(13)));
        laptop.appendChild(createElements(doc, laptop, "disc_reader", row.get(14)));

        return laptop;
    }






    private static Node createElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    private static Node createElementsWithAtr(Document doc, Element element, String name, String value,String nameAtr, String atr) {
        Element node = doc.createElement(name);
        node.setAttribute(nameAtr, atr);
        node.appendChild(doc.createTextNode(value));
        return node;
    }





}
