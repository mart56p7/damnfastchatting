package com.msgclient;

import com.msgresources.MessageProtocolException;
import com.msgresources.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Data that is associated with a client.
 * */
public class Client {
    private Socket socket = null;
    private User user = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private Thread heartThread = null;
    private ClientNetworkHeartbeat heart = null;
    private String username = null;
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
        lastcommunication = System.currentTimeMillis();
    }

    public long getTime(){
        return this.lastcommunication;
    }

    public void destroy(){
        try {
            dos = null;
            dis = null;
            if(socket != null){
                socket.close();
            }
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

    public void startHeart() throws MessageProtocolException{
        if(this.socket != null) {
            try {
                this.heart = new ClientNetworkHeartbeat(this);
                this.heartThread = new Thread(this.heart);
                heartThread.start();
            } catch (IOException e) {
                reset();
                throw new MessageProtocolException("Failed to connect - Missing a heart");
            }
        }
    }

    //One could easily argue that this method should be in ClientNetwork instead of here. It is here because of ease of access.
    // But it also means that we have active network methods in the object, instead of it just being a data container.
    public void sendMessage(String msg) throws MessageProtocolException {
        if(isConnected()){
            try {
                System.out.println("Client sendMessage: " + msg);
                getDataOutputStream().writeUTF(msg);
            } catch (IOException e) {
                new MessageProtocolException(e.getMessage());
            }
        }
    }

    //One could easily argue that this method should be in ClientNetwork instead of here. It is here because of ease of access.
    // But it also means that we have active network methods in the object, instead of it just being a data container.
    public String getMessage() {
        if (getSocket() != null && getDataInputStream() != null) {
            try {
                if (getDataInputStream().available() > 0) {
                    String msg = getDataInputStream().readUTF();
                    System.out.println("getMessage: " + msg);
                    return msg;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void reset(){
        //Connection specific settings
        connected = false;
        username = null;
        socket = null;
        heart = null;
        heartThread = null;
        dis = null;
        dos = null;
    }
}
