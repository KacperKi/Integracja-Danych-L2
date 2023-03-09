import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.opencsv.CSVReader;
import de.vandermeer.asciitable.AsciiTable;

public class Main {
    static String pathToFile;
    static ArrayList<String> nameOfColumnsFromFile = new ArrayList<String>(){
        {
            add("nr.");add("nazwa producenta"); add("przekątna ekranu"); add("rozdzielczość ekranu"); add("rodzaj powierzchni ekranu");
            add("ekran dotykowy"); add("nazwa procesora"); add("liczba rdzeni fizycznych"); add("prędkość taktowania MHz");
            add("pamięć RAM"); add("pojemność dysku"); add("rodzaj dysku"); add("nazwa układu graficznego");
            add("pamięć układu graficznego"); add("nazwa systemu operacyjnego"); add("rodzaj napędu fizycznego");
        }
    };
    static ArrayList<String> rowsFromFile = new ArrayList<>();

    static JFrame mainFrame;

    public static void main(String[] args){
        CreateFrameAndShow();

        SelectAndCheckFileWithData();
        ReadDataFromFile();
        PrintHeaderOfTable();
        ColumnNameBuildAndPrint();
        ConvertAndPrintDataFromArray();
    }


    public static void CreateFrameAndShow(){
        mainFrame = new JFrame("Kacper Kisielewski - LAB 2");

        JButton ImportButton = new JButton("Import data from TXT file");
        JButton SaveButtonData = new JButton("Export data to TXT file");

        ImportButton.setBounds(50,100, 95, 30); SaveButtonData.setBounds(100, 100, 95,30);
        mainFrame.add(ImportButton); mainFrame.add(SaveButtonData);

        mainFrame.setSize(500, 400);
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);

        while(true) {
            //do nothing
        }
    }
    public static void SelectAndCheckFileWithData(){
        boolean FileExists = false;
        while(!FileExists) {
            System.out.println("Insert Path to File: ");
            Scanner consolePath = new Scanner(System.in);
            pathToFile = consolePath.nextLine();

            File f = new File(pathToFile);
            if (f.exists() && !f.isDirectory()) {
                System.out.println("File exists - OK!");
                break;
            }
            else System.out.println("File doesn't exists!");
        }
    }
    public static void ReadDataFromFile() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            while ((line = br.readLine()) != null) {
                rowsFromFile.add(line);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public static void PrintHeaderOfTable(){
        System.out.printf("--------------------------------%n");
        System.out.printf("         Table with data        %n");
        System.out.printf("--------------------------------%n");
    }
    public static void ColumnNameBuildAndPrint(){
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow(nameOfColumnsFromFile);
        at.addRule();
        System.out.println(at.render(200));
    }
    public static void ConvertAndPrintDataFromArray(){
        ArrayList<String> elementsOfTable = new ArrayList<>();
        AsciiTable at = new AsciiTable();
        ArrayList<String> nameOfProducent = new ArrayList<>();
        ArrayList<Integer> counterOfProducent = new ArrayList<>();

        int numberOfRow = 1;

        for(String row: rowsFromFile){
            String[] result = row.split(";");
            elementsOfTable.add(String.valueOf(numberOfRow));

            for (int x=0; x<result.length; x++){
                String tmp = result[x];
                if(!tmp.equals("")) elementsOfTable.add(result[x]);
                else elementsOfTable.add("brak");

            }

            int position =0; int counter = 1;
            if(nameOfProducent.contains(result[0])) {
                position = nameOfProducent.indexOf(result[0]);
                counter = counterOfProducent.get(position);
                counter++;
                counterOfProducent.set(position, counter);
            }
            else{
                nameOfProducent.add(result[0]);
                counterOfProducent.add(1);
            }
            if(elementsOfTable.size()==14) {
                elementsOfTable.add("brak");
                elementsOfTable.add("brak");
            }
            if(elementsOfTable.size()==15) elementsOfTable.add("brak");

            at.addRow(elementsOfTable);
            at.addRule();
            elementsOfTable.clear();
            numberOfRow++;

        }
        System.out.println(at.render(200));

        System.out.println("Tabela danych");
        AsciiTable a = new AsciiTable();
        a.addRule();
        for(String t: nameOfProducent) {

            a.addRow(new ArrayList<>(Arrays.asList(t, counterOfProducent.get(nameOfProducent.indexOf(t)))));
            a.addRule();
        }
        System.out.println(a.render(30));
    }
//    D:\Studia\Magisterskie\3 semestr\L_IntegracjaSystemow\katalog.txt

}



//    Path to file with data
//    static File pathToFile;
//    Function with gui to select txt file
//    public static void PrintInformationAndSelectFileWithData(){
//        System.out.println("Laboratorium nr. 1!");
//        JFileChooser chooser = new JFileChooser(pathToFile);
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("Select only txt files", "txt");
//        chooser.setFileFilter(filter);
//        int returnVal = chooser.showOpenDialog(null);
//        if(returnVal == JFileChooser.APPROVE_OPTION) {
//            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
//        }
//        pathToFile = chooser.getSelectedFile();
//    }