import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.ws.WebServiceRef;
import com.soap.*;

import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;


@WebServiceRef(wsdlLocation = "http://localhost:7779/ws/first?wsdl")
public class ClientSoapWSFrame {

    private String urlString;
    private String namespaceUri;
    private String wsOperation;
    private HttpURLConnection connection;

    JFrame userFrame;
    JButton ClientNumberOfLaptopWithProducer, ClientNumberOfLaptopWithMatrix, ClientNumberOfLaptopWithProportions;
    JComboBox ClientProducerBox, ClientMatrixBox, ClientProportionsBox;
    JLabel ProducerInfo, MatrixInfo, ProportionsInfo, ProducerNumberResult, MatrixNumberResult, ProportionsNumberResult;

    public static void main(String[] args) {

        ClientSoapWSFrame clientSoapWSFrame = new ClientSoapWSFrame();


    }

    public ClientSoapWSFrame() {

        CreateClientFrame();
        CreateUserListener();

        SoapWS soapWS = new SoapWS();
//        SoapInterface soapService = soapWS.getHelloWorld();


        System.out.println("Client App Started");
        System.out.println(soapWS.getHelloWorld());

    }


    void CreateClientFrame(){
        userFrame = new JFrame("Integracja Systemów - Aplikacja Klienta - Kacper Kisielewski");

        ClientNumberOfLaptopWithMatrix = new JButton("Number of matrix");
        ClientNumberOfLaptopWithProducer = new JButton("Number of producer");
        ClientNumberOfLaptopWithProportions = new JButton("Number of proportions");

        String[] ProportionsListElements = {"16x9", "16x10", "4x3", "21x9", "1x1"};
        String[] MatrixListElements = {"matowa", "błyszcząca"};
        String[] ProducerListElements = {"Asus", "Dell", "Hp", "Xiaomi"};

        ClientProportionsBox = new JComboBox(ProportionsListElements);
        ClientMatrixBox = new JComboBox(MatrixListElements);
        ClientProducerBox = new JComboBox(ProducerListElements);

        ClientProportionsBox.setBounds(10,5, 200, 30); ClientNumberOfLaptopWithProportions.setBounds(210, 5, 200,30);
        ClientProducerBox.setBounds(430, 5, 200, 30); ClientNumberOfLaptopWithProducer.setBounds(630, 5, 200, 30);
        ClientMatrixBox.setBounds(850,5,200,30); ClientNumberOfLaptopWithMatrix.setBounds(1050,5,200,30);

        ProducerInfo = new JLabel("Number of producer", SwingConstants.CENTER); ProducerInfo.setBounds(10,45,150,30);
        ProducerNumberResult = new JLabel("na"); ProducerNumberResult.setBounds(170,45,25,30);

        MatrixInfo = new JLabel("Number of matrix", SwingConstants.CENTER); MatrixInfo.setBounds(430,45,150,30);
        MatrixNumberResult = new JLabel("na"); MatrixNumberResult.setBounds(590,45,25,30);

        ProportionsInfo = new JLabel("Number of proportions", SwingConstants.CENTER); ProportionsInfo.setBounds(850,45,150,30);
        ProportionsNumberResult = new JLabel("na"); ProportionsNumberResult.setBounds(1010,45,25,30);

        userFrame.add(ClientNumberOfLaptopWithProportions); userFrame.add(ClientNumberOfLaptopWithMatrix); userFrame.add(ClientNumberOfLaptopWithProducer);
        userFrame.add(ClientProportionsBox); userFrame.add(ClientMatrixBox); userFrame.add(ClientProducerBox);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border yellowLine = BorderFactory.createLineBorder(Color.yellow);

        ProducerInfo.setBorder(blackline); MatrixInfo.setBorder(blackline); ProportionsInfo.setBorder(blackline);
//        ProducerNumberResult.setBorder(yellowLine); ProportionsNumberResult.setBorder(yellowLine); MatrixNumberResult.setBorder(yellowLine);

        userFrame.add(ProducerInfo); userFrame.add(MatrixInfo); userFrame.add(ProportionsInfo);
        userFrame.add(ProducerNumberResult); userFrame.add(ProportionsNumberResult); userFrame.add(MatrixNumberResult);

        userFrame.setSize(1280, 120);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        userFrame.setLocation(dim.width/2 - userFrame.getWidth()/2, dim.height/2 + userFrame.getHeight());
//        userFrame.setLocationRelativeTo(null);
        userFrame.setLayout(null);
        userFrame.setVisible(true);
    }

    void CreateUserListener(){

    }




    private void openConnection() throws IOException {
        URL url = new URL(String.format("%s.asmx?op=%s", urlString, wsOperation));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.setRequestProperty("SOAPAction", String.format("%s/%s", namespaceUri, wsOperation));
        connection.setDoOutput(true);
    }
    private void closeConnection() {
        if (connection != null) {
            connection.disconnect();
        }
    }


}


