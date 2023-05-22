package com.soap;

import java.sql.*;
import java.util.ArrayList;

public class MySQLConnectorSoap {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    public MySQLConnectorSoap() {
        init();
    }
    private void init(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/integracja?" + "user=root&password=");

            if (connect != null && !connect.isClosed()) {
                System.out.println("Connection correct!");
            }else{
                System.out.println("Connection closed or failure!");
            }

        }catch(Exception e){System.out.println("Init Function!\n" + e);}

    }

    public int getNumberOfSth(String query){
        try {
            statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //select * from dane where UPPER(manufacturer_name) = "DELL" ;
    public ArrayList<ArrayList<String>> readDataFromDB(String opt) throws Exception {
        try {

            statement = connect.createStatement();
            String query = "select * from dane where screen_type = '"+opt+"';";
            resultSet = statement.executeQuery(query);
            return writeResultSet(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public ArrayList<String> readResolutionFromDB(){
        ArrayList<String> t = new ArrayList<>();
        try {
            statement = connect.createStatement();

            String query = "select screen_resolution from dane;";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                t.add(resultSet.getString(1));
            }
            return t;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private ArrayList<ArrayList<String>> writeResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<ArrayList<String>> listOfRows = new ArrayList<>();
        ArrayList<String> row;

        while (resultSet.next()) {
            row = new ArrayList<>();
            row.add(resultSet.getString("manufacturer_name"));
            row.add(resultSet.getString("screen_size"));
            row.add(resultSet.getString("screen_resolution"));
            row.add(resultSet.getString("screen_type"));
            row.add(resultSet.getString("screen_touch"));
            row.add(resultSet.getString("processor_name"));
            row.add(resultSet.getString("processor_physical_cores"));
            row.add(resultSet.getString("processor_clock_speed"));
            row.add(resultSet.getString("ram"));
            row.add(resultSet.getString("disc"));
            row.add(resultSet.getString("disc_type"));
            row.add(resultSet.getString("gpu_name"));
            row.add(resultSet.getString("gpu_memory"));
            row.add(resultSet.getString("name_os"));
            row.add(resultSet.getString("disc_reader"));
            listOfRows.add(row);
        }
        System.out.println("Function: writeResultSet\nClass: MySQLConnector\nNumber of rows:" + listOfRows.size());
        return listOfRows;
    }
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void closeConnection(){
        try {
            connect.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean checkConnection(){
        try {
            return !connect.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}








