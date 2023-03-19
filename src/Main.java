import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class Main {

    public static void main(String[] args){
        new Main();
    }
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
    ArrayList<ArrayList<String>> dataToTable;
    JFrame mainFrame;
    JButton ImportButton, SaveButtonData, ImportXMLButton, SaveXMLButtonData;
    JTable tableWithData;
    JScrollPane scrollPane;
    DefaultTableModel model;

    public Main() {
      CreateFrame();
      CreateListener();
    }
    void CreateFrame(){
        mainFrame = new JFrame("Integracja Systemów - Kacper Kisielewski");

        ImportButton = new JButton("Import data from TXT file");
        SaveButtonData = new JButton("Export data to TXT file");

        ImportXMLButton = new JButton("Import data from XML file");
        SaveXMLButtonData = new JButton("Export data to XML file");

        ImportButton.setBounds(10,5, 200, 30); SaveButtonData.setBounds(220, 5, 200,30);
        mainFrame.add(ImportButton); mainFrame.add(SaveButtonData);

        ImportXMLButton.setBounds(430, 5, 200, 30); SaveXMLButtonData.setBounds(640, 5, 200, 30);
        mainFrame.add(ImportXMLButton); mainFrame.add(SaveXMLButtonData);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(900, 80);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
    }
    void CreateListener(){
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
                ImportXMLButtonFunction();
            }
        });
        SaveXMLButtonData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExportXMLButtonFunction();
            }
        });
    }
    void ImportButtonFunction(){
        PrintInformationAndSelectFileWithData();    //Select correct TXT file
        if(pathToFile!=null) {
            ReadDataFromFile();                         //Import all data from txt file
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
    void ImportXMLButtonFunction(){
        PrintInformationAndSelectXMLFileWithData();
        if(pathToXMLFile!=null){
            //readData
            if(scrollPane == null){
                //showTable();
                //TableWasChangedListenerCreate();
            }
            else {
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

    void ReadDataFromFile() {
        dataToTable = new ArrayList<>();
        if(dataToTable.size() != 0) dataToTable.clear();

        String line;
        System.out.println("PATH: " + pathToFile);
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            while ((line = br.readLine()) != null) {
                   ArrayList<String> results = new ArrayList<>();
                   results.addAll(Arrays.asList(line.split(";")));
                   while(results.size() < nameOfColumnsFromFile.size()){
                       results.add("brak");
                   }
                   dataToTable.add(results);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    void TableWasChangedListenerCreate(){
        this.tableWithData.getModel().addTableModelListener(e -> {
            String newValue = tableWithData.getModel().getValueAt(e.getFirstRow(), e.getColumn()).toString();
            if(!dataToTable.get(e.getLastRow()).get(e.getColumn()).equals(newValue)) {
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
                if(el.equals("")) result[i][j] = "brak";
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

        tableWithData.getTableHeader().setReorderingAllowed(false);
        tableWithData.getTableHeader().setResizingAllowed(false);

        scrollPane.setEnabled(true);
        scrollPane.setBounds(10, 45, 1700, 250);
        scrollPane.setVisible(true);

        this.mainFrame.add(scrollPane);
        mainFrame.setSize(1720,340);
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

}