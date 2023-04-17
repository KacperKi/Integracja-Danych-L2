import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.event.*;
import java.beans.Visibility;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;

public class Main {

    public static void main(String[] args){
        new Main();
    }
    MySQLConnector mySQLConnector;
    String pathToFile, pathToSaveFile, pathToXMLFile;
    ArrayList<String> nameOfColumnsFromFile = new ArrayList<String>() {
        {
            add("nazwa producenta");
            add("przekątna ekranu");
            add("rozdzielczość ekranu");
            add("rodzaj powierzchni ekranu");
            add("ekran dotykowy");
            add("nazwa procesora");
            add("liczba rdzeni fizycznych");
            add("prędkość taktowania MHz");
            add("pamięć RAM");
            add("pojemność dysku");
            add("rodzaj dysku");
            add("nazwa układu graficznego");
            add("pamięć układu graficznego");
            add("nazwa systemu operacyjnego");
            add("rodzaj napędu fizycznego");
        }
    };
    ArrayList<ArrayList<String>> dataToTable,
                                newRecordsFromDatabase, newRecordsFromTXT, newRecordsFromXML,
                                duplicatedRecordsFromDatabase, duplicatedRecordsFromTXT, duplicatedRecordsFromXML;
    JFrame mainFrame;
    JButton ImportButton, SaveButtonData, ImportXMLButton, SaveXMLButtonData, ConnectToDatabaseButton,
            ImportMySQLButton, ExportMySQLButton;
    JTable tableWithData;
    JScrollPane scrollPane;
    DefaultTableModel model;
    JTextField queryField;
    Color[] rowColors;

    public Main() {
      CreateFrame();
      CreateListener();

      dataToTable = new ArrayList<>();
    }

    void CreateFrame(){
        mainFrame = new JFrame("Integracja Systemów - Kacper Kisielewski");

        ImportButton = new JButton("Import data from TXT file");
        SaveButtonData = new JButton("Export data to TXT file");

        ImportXMLButton = new JButton("Import data from XML file");
        SaveXMLButtonData = new JButton("Export data to XML file");

        ConnectToDatabaseButton = new JButton("Connect to Database!");
        ImportMySQLButton = new JButton("Import from MySQL");
        ExportMySQLButton = new JButton("Export to MySQL");

        ImportButton.setBounds(10,5, 200, 30); SaveButtonData.setBounds(210, 5, 200,30);
        mainFrame.add(ImportButton); mainFrame.add(SaveButtonData);

        ImportXMLButton.setBounds(430, 5, 200, 30); SaveXMLButtonData.setBounds(630, 5, 200, 30);
        ImportXMLButton.setBackground(new Color(0, 156, 253, 255)); SaveXMLButtonData.setBackground(new Color(0,156,253,255));
        mainFrame.add(ImportXMLButton); mainFrame.add(SaveXMLButtonData);

        ConnectToDatabaseButton.setBounds(10, 45, 200, 30);
//        ConnectToDatabaseButton.setBackground(new Color(222, 220, 56,100));

        ImportMySQLButton.setBounds(210,45,150,30); ExportMySQLButton.setBounds(360,45,150,30);
//        ImportMySQLButton.setBackground(new Color(222, 220, 56,100)); ExportMySQLButton.setBackground(new Color(222, 220, 56,100));
        mainFrame.add(ImportMySQLButton);mainFrame.add(ExportMySQLButton);

        queryField = new JTextField("This function is disabled");
        queryField.setBounds(220,45, 300, 30);
        queryField.setEditable(false); queryField.setVisible(false);

        mainFrame.add(queryField);
        mainFrame.add(ConnectToDatabaseButton);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(900, 120);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
    }
    void CreateListener() {
        ImportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImportButtonFunction();
            }
        });
        SaveButtonData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ExportButtonFunction();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ImportXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    try {
                        ImportXMLButtonFunction();
                    } catch (XMLStreamException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        SaveXMLButtonData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportXMLButtonFunction();
            }
        });
        ConnectToDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectToDatabaseButtonFunction();
            }
        });
        queryField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    RunQueryFromQueryJTextAfterPressEnter();
                }
                super.keyPressed(e);
            }
        });
        ImportMySQLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MySQLFunction(true);
            }
        });
        ExportMySQLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MySQLFunction(false);
            }
        });
        mainFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if(mySQLConnector.checkConnection()) {
                        mySQLConnector.closeConnection();
                        System.out.println("Connection closed with DB!");
                    }
                }catch(Exception w){
                    System.out.println("Connection is null!");
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }


    void ImportButtonFunction(){
        PrintInformationAndSelectFileWithData();    //Select correct TXT file
        if(pathToFile!=null) {
            ReadDataFromFile();
        }
        else {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Empty path to txt file!",
                    "Empty path!",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    void ExportButtonFunction() throws IOException {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Select only txt files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            this.pathToSaveFile = String.valueOf(chooser.getSelectedFile());
        }

        if(pathToSaveFile!=null) {
            File f = new File(pathToSaveFile);
            if (f.exists()) {
                int n = JOptionPane.showConfirmDialog(
                        mainFrame, "Overwrite the specified file?",
                        "File exists!",
                        JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    saveToFileData();
                }
            }else {
                saveToFileData();
            }

        }
        else {
            JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Ther's no output file selected!!",
                    "Error!",
                    JOptionPane.OK_OPTION
            );
        }
    }
    void ImportXMLButtonFunction() throws FileNotFoundException, XMLStreamException {
        PrintInformationAndSelectXMLFileWithData();
        if(pathToXMLFile!=null){
            StAXParser parser = new StAXParser(pathToXMLFile);
            ArrayList<Laptop> xmlData = parser.readDatafromFile();
            dataToTable = new ArrayList<>();
            for(Laptop t: xmlData){
                dataToTable.add(t.convertClassToArrayList());
            }
            if (scrollPane == null) {
                ShowTable();
                TableWasChangedListenerCreate();
            } else {
                this.model = new DefaultTableModel(ConvertDataToObject(dataToTable), nameOfColumnsFromFile.toArray());
                this.tableWithData.setModel(model);
            }
        }
        else {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Empty path to XML file!",
                    "Empty path!",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
    void ExportXMLButtonFunction(){
        if(tableWithData != null) {
            XMLFileCreator creator = new XMLFileCreator(tableWithData);

            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Select only xml files", "xml");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(mainFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.pathToSaveFile = String.valueOf(chooser.getSelectedFile());
            }
            if (pathToSaveFile != null) {
                File f = new File(pathToSaveFile);
                if (f.exists()) {
                    int n = JOptionPane.showConfirmDialog(
                            mainFrame, "Overwrite the specified file?",
                            "File exists!",
                            JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        //save to xml function
                        creator.saveToFile(pathToSaveFile);
                    }
                } else {
                    //save to xml function
                    creator.saveToFile(pathToSaveFile);
                }

            } else {
                JOptionPane.showConfirmDialog(
                        mainFrame,
                        "Ther's no output file selected!!",
                        "Error!",
                        JOptionPane.OK_OPTION
                );
            }
        }
    }



    void ConnectToDatabaseButtonFunction(){
        mySQLConnector = new MySQLConnector();

    }
    void PrintInformationAndSelectFileWithData(){
        JFileChooser chooser = new JFileChooser(pathToFile);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Select only txt files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile());
            pathToFile = String.valueOf(chooser.getSelectedFile());
        }
    }
    void PrintInformationAndSelectXMLFileWithData(){
        JFileChooser chooser = new JFileChooser(pathToFile);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Select only XML files", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile());
            pathToXMLFile = String.valueOf(chooser.getSelectedFile());
        }
    }


    void RunQueryFromQueryJTextAfterPressEnter(){
        String query = queryField.getText();
        mySQLConnector.runQuery(query);

    }


    void ReadDataFromFile() {
        ArrayList<ArrayList<String>> dataFromFile = new ArrayList<>();
        this.dataToTable = getCurrentDataFromTable();
        
        String line;
        System.out.println("PATH: " + pathToFile);

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            while ((line = br.readLine()) != null) {
                   ArrayList<String> results = new ArrayList<>();
                   results.addAll(Arrays.asList(line.split(";")));
                   while(results.size() < nameOfColumnsFromFile.size()){
                       results.add("brak");
                   }
                dataFromFile.add(results);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        newRecordsFromTXT = new ArrayList<>(dataFromFile);
        duplicatedRecordsFromTXT = new ArrayList<>(dataFromFile);

        System.out.println("Len: " + newRecordsFromTXT.size());
        System.out.println("Len: " + getCurrentDataFromTable().size());

        newRecordsFromTXT.removeAll(getCurrentDataFromTable());

        duplicatedRecordsFromTXT.removeAll(newRecordsFromTXT);

        JOptionPane.showMessageDialog(
                mainFrame,
                "Found " + newRecordsFromTXT.size() + " new rows" +
                        "\nFound " + duplicatedRecordsFromTXT.size() + " duplicated rows",
                "Imported from TXT",
                JOptionPane.WARNING_MESSAGE);

        rowColors = new Color[dataToTable.size()+dataFromFile.size()];
        for(int i=0; i<dataToTable.size(); i++){
            rowColors[i] = Color.LIGHT_GRAY;
        }

        for(ArrayList<String> row : dataFromFile){
            this.dataToTable.add(row);
            if(duplicatedRecordsFromTXT.contains(row)){
                this.rowColors[dataToTable.size()-1] = Color.RED;
            }else{
                this.rowColors[dataToTable.size()-1] = Color.LIGHT_GRAY;
            }
        }

        if (scrollPane == null || tableWithData == null) {
            ShowTable();
            TableWasChangedListenerCreate();
        } else {
            this.model = new DefaultTableModel(ConvertDataToObject(dataToTable), nameOfColumnsFromFile.toArray());
            this.tableWithData.setModel(model);
            TableWasChangedListenerCreate();
        }
        TableWasChangedListenerCreate();

    }

    void TableWasChangedListenerCreate(){
        this.tableWithData.getModel().addTableModelListener(e -> {
            String newValue = tableWithData.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString();
            if(!dataToTable.get(e.getLastRow()).get(e.getColumn()).equals(newValue)) {
                System.out.println("LIST");

                if(!ValidateDataInTable(e.getColumn(), newValue)){
                    //Information about validation!
                    JOptionPane.showMessageDialog(
                            mainFrame,
                            "It was changed -"
                                    + " column: " + (e.getColumn() + 1)
                                    + " row: " + (e.getLastRow() + 1)
                                    + "\nNew value: " + tableWithData.getModel().getValueAt(e.getFirstRow(), e.getColumn())
                                    + "\nThis is an invalid value for a variable!",
                            "WARNING - Incorrect Data!",
                            JOptionPane.WARNING_MESSAGE);
                    this.model.setValueAt(dataToTable.get(e.getFirstRow()).get(e.getColumn()),e.getFirstRow(), e.getColumn());
                    this.tableWithData.setModel(model);
                } else {
                    JOptionPane.showMessageDialog(
                            mainFrame,
                            "It was changed -"
                                    + " column: " + (e.getColumn() + 1)
                                    + " row: " + (e.getLastRow() + 1)
                                    + "\nNew value: " + tableWithData.getModel().getValueAt(e.getFirstRow(), e.getColumn()),
                            "Data was changed!",
                            JOptionPane.INFORMATION_MESSAGE);
                    rowColors[e.getFirstRow()] = Color.WHITE;
                    scrollPane.repaint();
                    System.out.println("White color set");

                }
            }
        });
    }
    Object[][] ConvertDataToObject(ArrayList<ArrayList<String>> data){
        int row = data.size();
        int column = data.get(0).size();
        Object[][] result = new Object[row][column];

        int i = 0, j = 0;
        for(ArrayList<String> t: data){
            j = 0;
            for(String el: t){
                if(el == null || el.equals("")) result[i][j] = "brak";
                else result[i][j] = el;
                j++;
            }
            i++;
        }
        return result;
    }
    void ShowTable(){
        this.model = new DefaultTableModel(ConvertDataToObject(dataToTable), nameOfColumnsFromFile.toArray());
        this.tableWithData = new JTable(model);
        this.scrollPane = new JScrollPane(tableWithData, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        rowColors = new Color[dataToTable.size()];
        for(int i=0; i<dataToTable.size(); i++){
                rowColors[i] = Color.LIGHT_GRAY;
        }

        tableWithData.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
            {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
//                c.setBackground(row == 1 ? Color.RED : Color.LIGHT_GRAY);
                c.setBackground(rowColors[row]);
                return c;
            }
        });


        tableWithData.getTableHeader().setReorderingAllowed(false);
        tableWithData.getTableHeader().setResizingAllowed(false);

        scrollPane.setEnabled(true);
        scrollPane.setBounds(10, 85, 1700, 300);
        scrollPane.setVisible(true);
        this.mainFrame.add(scrollPane);

        mainFrame.setSize(1720,440);
        mainFrame.invalidate();
        mainFrame.validate();
        mainFrame.repaint();
    }
    void saveToFileData() throws FileNotFoundException {
        PrintWriter output = new PrintWriter(pathToSaveFile);
        String rowToInsert = "";

        for(int i=0; i<tableWithData.getRowCount(); i++){
            rowToInsert = "";
            for(int j=0; j<tableWithData.getColumnCount();j++){
                rowToInsert += (tableWithData.getModel().getValueAt(i,j) + ";");
            }
            output.println(rowToInsert);
        }
        output.close();
        JOptionPane.showMessageDialog(
                mainFrame,
                "Operation successful!\n" + "Path to your file: " + pathToSaveFile,
                "Information!",
                JOptionPane.WARNING_MESSAGE
        );
    }
    boolean ValidateDataInTable(int numberOfColumn, String value){
        String[] regex = {
                "[a-zA-Z]{2,10}",           //nazwa producenta
                "\\d+(?:\\.\\d+)?\"",       //wielkosc matrycy
                "\\d{1,4}+x\\d{1,4}",       //rodzielczosc resolution
                "[a-zA-Z{4,12}]",           //rodzaj matrycy
                "^(?:Tak|Nie|TAK|NIE|tak|nie)$",//dotykowy ekran
                "[a-zA-Z0-9 ]{4,10}",       //procesor
                "\\d{1,2}",                 // liczba rdzeni
                "[0-9]{3,4}$",              //taktowanie
                "\\d{1,2}?GB",              //RAM
                "\\d{1,5}?GB",              //pojemnosc dysku
                "^(?:HDD|SSD)$",            //dysk
                "[a-zA-Z0-9 ]{4,30}",       //układ graficzny
                "\\d{1,2}?GB",              //pamięc grafiki
                "[a-zA-Z0-9 ]{4,20}",       //system operacyjny
                "[a-zA-Z-]{3,10}"           //napęd
        };

        if(!value.equals("brak")) {
            if(value.matches(regex[numberOfColumn])) return true;
            else return false;
        }
        else return true;
    }
    ArrayList<ArrayList<String>> getCurrentDataFromTable(){
        ArrayList<ArrayList<String>> currentData = new ArrayList<>();
        ArrayList<String> row;

        if (scrollPane == null || tableWithData == null) {
            dataToTable = new ArrayList<>();
            return new ArrayList<>();
        } else {
            for (int i = 0; i < tableWithData.getRowCount(); i++) {
                row = new ArrayList<>();
                for (int j = 0; j < tableWithData.getColumnCount(); j++) {
                    row.add(tableWithData.getModel().getValueAt(i, j).toString());
                }
                currentData.add(row);
            }
            return currentData;
        }
    }

    void MySQLFunction(boolean option){
        try {
            if (option) {
                //IF TRUE - Import from database

                ArrayList<ArrayList<String>> dataFromDataBase = mySQLConnector.readTableFromDB();

                this.duplicatedRecordsFromTXT = new ArrayList<>();
                this.duplicatedRecordsFromXML = new ArrayList<>();
                this.newRecordsFromTXT = new ArrayList<>();
                this.newRecordsFromXML = new ArrayList<>();

                newRecordsFromDatabase = new ArrayList<>(); duplicatedRecordsFromDatabase = new ArrayList<>();

                newRecordsFromDatabase = new ArrayList<>(dataFromDataBase);
                duplicatedRecordsFromDatabase = new ArrayList<>(dataFromDataBase);

                newRecordsFromDatabase.removeAll(getCurrentDataFromTable());
                duplicatedRecordsFromDatabase.removeAll(newRecordsFromDatabase);

                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Found " + newRecordsFromDatabase.size() + " new rows" +
                        "\nFound " + duplicatedRecordsFromDatabase.size() + " duplicated rows",
                        "Imported from Database",
                        JOptionPane.WARNING_MESSAGE);


                rowColors = new Color[dataToTable.size()+dataFromDataBase.size()];
                for(int i=0; i<dataToTable.size(); i++){
                    rowColors[i] = Color.LIGHT_GRAY;
                }

                for(ArrayList<String> row : dataFromDataBase){
                    this.dataToTable.add(row);
                    if(duplicatedRecordsFromDatabase.contains(row)){
                        this.rowColors[dataToTable.size()-1] = Color.RED;
                    }else{
                        this.rowColors[dataToTable.size()-1] = Color.LIGHT_GRAY;
                    }
                }

                if (scrollPane == null || tableWithData == null) {
                    ShowTable();
                    TableWasChangedListenerCreate();
                } else {
                    this.model = new DefaultTableModel(ConvertDataToObject(dataToTable), nameOfColumnsFromFile.toArray());
                    this.tableWithData.setModel(model);
                }
                TableWasChangedListenerCreate();

            } else {
                //FALSE - Export from database
                mySQLConnector.runQuery("delete from dane;");
                ArrayList<ArrayList<String>> dataToExport = new ArrayList<>(getCurrentDataFromTable());

                dataToExport.removeAll(duplicatedRecordsFromDatabase);
                mySQLConnector.insertRowToDataBase(dataToExport);

            }
        }catch(Exception e){
            System.out.println(e);
        }
    }



}