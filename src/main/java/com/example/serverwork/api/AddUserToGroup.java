package com.example.serverwork.api;
import com.example.serverwork.servercode.DataBaseConnection;
import com.example.serverwork.servercode.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddUserToGroup extends DataBaseConnection {
    public static ObjectNode groupAdding(UserController.GroupPatting adding){
        try {


            String sql = "UPDATE groups\n" +
                    "SET users = array_append(users, ?)\n" +
                    "WHERE name = ?;";
            String sql1 = "SELECT * FROM groups WHERE name = ? AND users @> ARRAY[?];";
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();

            PreparedStatement addingStatement = Returnconnection().prepareStatement(sql);
            PreparedStatement checkStatement = Returnconnection().prepareStatement(sql1);
            checkStatement.setString(1,adding.getGroupName());
            checkStatement.setString(2,adding.getUserWhoInvite());
            ResultSet resultSet = checkStatement.executeQuery();
            if(resultSet.next()){
                checkStatement.setString(2,adding.getInvitedUser());
                resultSet = checkStatement.executeQuery();
                if(resultSet.next()){
                    checkStatement.close();
                    addingStatement.close();
                    json.put("Adding status", "This user already in group");
                    return json;
                }
                addingStatement.setString(1,adding.getInvitedUser());
                addingStatement.setString(2,adding.getGroupName());
                int wasAdding = addingStatement.executeUpdate();
                if(wasAdding>0) {
                    checkStatement.close();
                    addingStatement.close();
                    json.put("Adding status", "Adding complete");
                    return json;
                }
                else {
                    checkStatement.close();
                    addingStatement.close();
                    json.put("Adding status","Something wrong");
                    return json;
                }
            }
            else {
                checkStatement.close();
                addingStatement.close();
                json.put("Adding status","User didn't found in group");
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
