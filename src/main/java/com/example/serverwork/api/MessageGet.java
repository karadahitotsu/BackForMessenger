package com.example.serverwork.api;

import com.example.serverwork.servercode.DataBaseConnection;
import com.example.serverwork.servercode.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MessageGet extends DataBaseConnection {
    public static ObjectNode Get(UserController.Messageget messageget){
        try{



            String sql = "SELECT DISTINCT contact\n" +
                    "FROM (\n" +
                    "    SELECT sender AS contact FROM messages WHERE sender <> ? and getter = ?\n" +
                    "    UNION\n" +
                    "    SELECT getter AS contact FROM messages WHERE getter <> ? and sender = ?\n" +
                    ") AS temp_table;";//search a unique contact
            PreparedStatement preparedStatement = Returnconnection().prepareStatement(sql);
            preparedStatement.setString(1,messageget.getUser());
            preparedStatement.setString(2, messageget.getUser());
            preparedStatement.setString(3, messageget.getUser());
            preparedStatement.setString(4, messageget.getUser());
            ResultSet rs =preparedStatement.executeQuery();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();
            ArrayNode contactsArray = objectMapper.createArrayNode();
            while (rs.next()){
                contactsArray.add(rs.getString("contact"));
            }
            json.set("contacts",contactsArray);
            if(messageget.getSelection()!=0){
                json = objectMapper.createObjectNode();
                String whatUserSelect = contactsArray.get(messageget.getSelection() - 1).asText();
                sql = "SELECT * FROM messages WHERE sender = ? or getter = ? ORDER BY senddate ASC";
                preparedStatement = Returnconnection().prepareStatement(sql);
                preparedStatement.setString(1,whatUserSelect);
                preparedStatement.setString(2,whatUserSelect);
                rs = preparedStatement.executeQuery();
                ArrayNode messages = objectMapper.createArrayNode();
                while(rs.next()){
                    ObjectNode whatInside = objectMapper.createObjectNode();
                    whatInside.put("ID",rs.getInt("id"));
                    whatInside.put("sender",rs.getString("sender"));
                    whatInside.put("getter",rs.getString("getter"));
                    whatInside.put("message",rs.getString("message"));
                    whatInside.put("date",rs.getDate("senddate").toString());
                    whatInside.put("time",rs.getTime("senddate").toString());
                    whatInside.put("read",rs.getBoolean("read"));
                    messages.add(whatInside);
                }
                json.set("messages",messages);
                preparedStatement.close();

                return json;


            }

            return json;


        }catch (Exception e){
            ObjectNode error = new ObjectMapper().createObjectNode();
            error.put("Error", e.toString());
            return error;
        }
    }
}
