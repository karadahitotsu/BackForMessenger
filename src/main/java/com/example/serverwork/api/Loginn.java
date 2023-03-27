package com.example.serverwork.api;

import com.example.serverwork.servercode.DataBaseConnection;
import com.example.serverwork.servercode.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.*;

public class Loginn extends DataBaseConnection {
    public static ObjectNode Login(UserController.Login userr){
        try{

            String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
            PreparedStatement preparedStatement= Returnconnection().prepareStatement(sql);
            preparedStatement.setString(1,userr.getLogin());
            preparedStatement.setString(2,userr.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();

            if(rs.next()){
                json.put("login",rs.getString("login"));
                json.put("email",rs.getString("email"));
                json.put("telephone",rs.getString("telephone"));
                json.put("role",rs.getString("role"));
                preparedStatement.close();
                return json;
            }
            else {
                json.put("Error","User not found");
                preparedStatement.close();
                return json;
            }
        }catch (Exception e){
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    ObjectNode json = objectMapper.createObjectNode();
                                    json.put("Error",e.toString());
                                    return json;
        }
    }
}
