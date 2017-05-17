/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.Serializable;

/**
 *
 * @author Jircus
 */
public class Message implements Serializable {
    
    private String name;
    private String message;
    private int userCount;
    
    public Message(String name, String message){
        this.name = name;
        this.message = message;
    }

    public Message(String name, String message, int count){
        this.name = name;
        this.message = message;
        this.userCount = count;
    }
    
    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
    
}
