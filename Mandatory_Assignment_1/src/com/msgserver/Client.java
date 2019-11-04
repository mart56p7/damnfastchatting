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

    private boolean connected = false;

    public Client(){
        this(null);
    }

    public Client(Socket socket){
        this(socket, null);
    }

    public Client(Socket socket, User user){
        setUser(user);
        setSocket(socket);
    }

    public Socket getSocket(){
        return this.socket;
    }

    public void setSocket(Socket socket){
        this.socket = socket;
        if(this.socket != null){
            try {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                updateTime();
                setConnected(true);
            } catch (IOException e) {
                this.socket = null;
                System.out.println("Failed to connect client: " + e.getMessage());
                e.printStackTrace();
            }
        }
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

    public void updateTime(){
        lastcommunication = java.lang.System.currentTimeMillis();
    }

    public long getTime(){
        return this.lastcommunication;
    }

    public void close(){
        try {
            dos = null;
            dis = null;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            socket = null;
            user = null;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
