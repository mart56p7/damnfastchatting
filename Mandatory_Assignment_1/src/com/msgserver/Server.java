package com.msgserver;

import com.msgresources.FIFO;
import com.msgresources.Message;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    private volatile ServerSocket server = null;
    private List<Client> clients = null;
    private boolean shutdown = false;

    public Server(List<Client> clients, int port)
    {
        this.clients = clients;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.out.println("Server started " + server.getInetAddress() + " on port " + server.getLocalPort());
            System.out.println("Waiting for a client ...");
            while(!shutdown){
                try
                {
                    Socket socket = server.accept();
                    synchronized (clients) {
                        clients.add(new Client(socket));
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
        List<Client> soc = new ArrayList<>();
        Talker talker = new Talker(soc, msgqueue);
        Listener listener = new Listener(soc, msgqueue, talker);
        (new Thread(talker)).start();
        (new Thread(listener)).start();

        Server server = new Server(soc, 5000);
    }
}
