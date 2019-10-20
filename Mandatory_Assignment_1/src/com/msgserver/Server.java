package com.msgserver;

import com.msgresources.FIFO;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    private volatile ServerSocket server = null;
    private List<Socket> sockets = null;
    private boolean shutdown = false;

    public Server(int port, List<Socket> sockets)
    {
        this.sockets = sockets;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            while(!shutdown){
                try
                {

                    System.out.println("Server started " + server.getInetAddress() + " on port " + server.getLocalPort());
                    System.out.println("Waiting for a client ...");
                    synchronized (sockets) {
                        sockets.add(server.accept());
                    }
                    System.out.println("Client accepted");

                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        this.shutdown = true;
    }

    public static void main(String args[])
    {
        FIFO<Message> msgqueue = new FIFO<>();
        List<Socket> soc = new ArrayList<Socket>();
        Server server = new Server(5000, soc);
        Listener listener = new Listener(soc, msgqueue);
    }
}
