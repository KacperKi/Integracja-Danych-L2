package com.rest.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Rest {
    public static void main(String[] args) {
            SpringApplication.run(Rest.class, args);


//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            //utworzenie obiektu połączenia do bazy danych MySQL:
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/integracja?serverTimezone=UTC", "root", "");
//            //utworzenie obiektu do wykonywania zapytań do bd:
//            Statement st = conn.createStatement();
//            String query="SELECT * FROM dane";
//            //wykonanie zapytania SQL:
//            ResultSet rs = st.executeQuery(query);
//        }catch(Exception e){
//            System.out.println("DB Failure!");
//        }
//        System.out.println("Rest active!");
    }

}
