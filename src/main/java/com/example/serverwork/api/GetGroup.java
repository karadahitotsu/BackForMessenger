package com.example.serverwork.api;

import com.example.serverwork.servercode.DataBaseConnection;
import com.example.serverwork.servercode.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetGroup extends DataBaseConnection {
    public static ObjectNode getGroups(UserController.GroupGet groupGet){
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();
            String sql = "SELECT * FROM groups WHERE ? = ANY(users);";
            PreparedStatement preparedStatement = Returnconnection().prepareStatement(sql);
            preparedStatement.setString(1,groupGet.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayNode groups = objectMapper.createArrayNode();

            while (resultSet.next()){
                ObjectNode whatInside = objectMapper.createObjectNode();
                Array usersArray = resultSet.getArray("users");
                String[] users = (String[])usersArray.getArray();
                whatInside.put("id",resultSet.getInt("id"));
                whatInside.put("group name",resultSet.getString("name"));
                whatInside.put("admin name",resultSet.getString("adminname"));
                whatInside.putPOJO("users",objectMapper.valueToTree(users));
                whatInside.put("private",resultSet.getBoolean("private"));
                groups.add(whatInside);
                json.set("Groups",groups);
            }
            preparedStatement.close();
            return json;
        }catch (Exception e){
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();
            json.put("Error",e.toString());
            return json;
        }
    }
}
