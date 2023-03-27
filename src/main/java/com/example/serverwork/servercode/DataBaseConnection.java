package com.example.serverwork.servercode;

import java.sql.Connection;
import java.sql.DriverManager;

public  class DataBaseConnection {
    private static Connection connection;
    public DataBaseConnection(){

    }




    public static void makeconnection(){

       try {
           connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/messenger","postgres","26463");
       }catch (Exception e){
           System.out.println("ERROR");
       }


    }
    public static Connection Returnconnection() {
        try {
            if (connection==null){
                makeconnection();
                System.out.println("create connection");
            }
            return connection;
        }catch (Exception e){
            return connection;
        }

    }
}
