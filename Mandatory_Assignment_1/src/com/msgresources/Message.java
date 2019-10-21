package com.msgresources;

public class Message {
    private String message = null;
    private User user = null;

    public Message(String message){
        this(message, null);
    }

    public Message(String message, User user){
        this.message = message;
        this.user = user;
    }

    public void setMessage(String message){
        setMessage(message, null);
    }

    public void setMessage(String message, User user){
        this.message = message;
        this.user = user;
    }

    public String getMessage(){
        return this.message;
    }

    public User getUser(){
        return this.user;
    }

    public static String Format(Message msg){
        if(msg.getUser() == null){
            return msg.getMessage();
        }
        else{
            return "DATA " + msg.getUser().getDisplayName() + ":" + msg.getMessage();
        }
    }
}
