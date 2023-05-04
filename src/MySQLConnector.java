import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnector {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    public MySQLConnector() {
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
    public void runQuery(String query){
        try {
            statement = connect.createStatement();
            System.out.println(statement.execute(query));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<ArrayList<String>> readTableFromDB() throws Exception {
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select * from dane");

            return writeResultSet(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
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



    public void insertRowToDataBase(ArrayList<ArrayList<String>> data){
        try {
            String sqlQuery = " INSERT INTO dane\n" +
                    "(manufacturer_name,screen_size,screen_resolution,screen_type,screen_touch,processor_name,processor_physical_cores,processor_clock_speed,ram,disc,disc_type," +
                    "gpu_name,gpu_memory,name_os,disc_reader) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement preparedStmt;

            for(ArrayList<String> row : data) {
                int i = 1;
                preparedStmt = connect.prepareStatement(sqlQuery);
                for (String value : row) {
                    preparedStmt.setString(i, value);
                    i++;
                }
                preparedStmt.execute();
            }


        }catch(Exception e){
            System.out.println(e);
        }

    }



}








