package com.msgserver;

import com.msgresources.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket = null;
    private User user = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private long lastcommunication = 0;

    public Client(Socket socket){
        this(socket, null);
    }

    public Client(Socket socket, User user){
        this.socket = socket;
        if(this.socket != null){
            try {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                this.socket = null;
                System.out.println("Failed to connect client: " + e.getMessage());
                e.printStackTrace();
            }
        }
        this.user = user;
        System.out.println("Client connected");
    }

    public Socket getSocket(){
        return this.socket;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public DataInputStream getDataInputStream(){
        return this.dis;
    }

    public DataOutputStream getDataOutputStream(){
        return this.dos;
    }
}
