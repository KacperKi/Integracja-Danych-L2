import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        new Main();
    }
    String pathToFile;
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
    JButton ImportButton, SaveButtonData;
    JTable tableWithData;
    JScrollPane scrollPane;
    Object[][] dataToSave;
    DefaultTableModel model;

    public Main() {
      CreateFrame();
      CreateListener();
    }
    void CreateFrame(){
        mainFrame = new JFrame("Integracja Systemów - Kacper Kisielewski");

        ImportButton = new JButton("Import data from TXT file");
        SaveButtonData = new JButton("Export data to TXT file");

        ImportButton.setBounds(10,5, 200, 30); SaveButtonData.setBounds(220, 5, 200,30);
        mainFrame.add(ImportButton); mainFrame.add(SaveButtonData);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(450, 80);
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
                //w
            }
        });
    }
    void ImportButtonFunction(){
        PrintInformationAndSelectFileWithData();    //Select correct TXT file
        ReadDataFromFile();                         //Import all data from txt file
        if(scrollPane == null) ShowTable();
        else {
            this.model= new DefaultTableModel(ConvertDataToObject(dataToTable), nameOfColumnsFromFile.toArray());
            this.tableWithData.setModel(model);
        }
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
        scrollPane.setEnabled(true);
        scrollPane.setBounds(10, 45, 1700, 250);
        scrollPane.setVisible(true);

        this.mainFrame.add(scrollPane);
        mainFrame.setSize(1720,340);
        mainFrame.invalidate();
        mainFrame.validate();
        mainFrame.repaint();

        tableWithData.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
//                dataToSave = tableWithData.getModel().
                System.out.println("It was changed col: " + e.getColumn() + " row: " + e.getLastRow()
                        + " val: " + tableWithData.getModel().getValueAt(e.getFirstRow(), e.getColumn()));
            }
        });
    }

}