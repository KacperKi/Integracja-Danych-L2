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
            System.out.println("Len of data: " + dataFromTable.size());
            for (ArrayList<String> row : dataFromTable) {
                System.out.println("Len of data in row: " + row.size());
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
            System.out.println("Path to Export: " + pathToExportFile);

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
        laptop.appendChild(createElementsWithAtr(doc, laptop, "screen", row.get(1), "touch", row.get(4)));

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
