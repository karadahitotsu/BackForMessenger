package com.example.serverwork.api;

import com.example.serverwork.servercode.DataBaseConnection;
import com.example.serverwork.servercode.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeleteGroup extends DataBaseConnection {
    public static ObjectNode deleteGroup(UserController.GroupDeleting deleting){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();
            String sql = "DELETE FROM groups WHERE id = ? AND adminname = ?";
            PreparedStatement preparedStatement = Returnconnection().prepareStatement(sql);
            preparedStatement.setInt(1,deleting.getGroupId());
            preparedStatement.setString(2,deleting.getUserWhoDelete());
            int deleteCount = preparedStatement.executeUpdate();
            if(deleteCount>0){
                String sql1 = "ALTER TABLE groups ADD temp_id SERIAL;";
                String sql2 = "UPDATE groups SET temp_id = id;";
                String sql3 = "ALTER TABLE groups DROP COLUMN id;";
                String sql4 = "ALTER TABLE groups RENAME COLUMN temp_id TO id;";
                preparedStatement = Returnconnection().prepareStatement(sql1);
                preparedStatement.executeUpdate();
                preparedStatement = Returnconnection().prepareStatement(sql2);
                preparedStatement.executeUpdate();
                preparedStatement = Returnconnection().prepareStatement(sql3);
                preparedStatement.executeUpdate();
                preparedStatement = Returnconnection().prepareStatement(sql4);
                int changes = preparedStatement.executeUpdate();
                json.put("deleting status","completed");
                preparedStatement.close();
                return json;
            }
            json.put("deleting status","failed");
            preparedStatement.close();
            return json;
        }
        catch (Exception e){
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();
            json.put("Error",e.toString());
            return json;
        }
    }
}
