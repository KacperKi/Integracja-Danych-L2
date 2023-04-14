import java.sql.*;

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


    public void readDataBase() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/integracja?" + "user=admin&password=");
//            statement = connect.createStatement();
//            resultSet = statement.executeQuery("select * from feedback.comments");
//            writeResultSet(resultSet);
//
//            preparedStatement = connect.prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
//            preparedStatement.setString(6, "TestComment");
//            resultSet = preparedStatement.executeQuery();
//            writeResultSet(resultSet);

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
        }
    }
    private void writeResultSet(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String comment = resultSet.getString("comments");
            System.out.println("Comment: " + comment);
        }
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}

