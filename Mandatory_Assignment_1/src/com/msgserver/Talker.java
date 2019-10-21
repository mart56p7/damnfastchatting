package com.msgserver;

import com.msgresources.FIFO;
import com.msgresources.FIFOObserver;
import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Talker implements Runnable, FIFOObserver {

    private volatile List<Client> clients = null;
    private volatile FIFO<BufferedOutputStream> bos = null;
    private volatile FIFO<Message> msgqueue = null;
    private final int waittime = 5000;
    private boolean shutdown = false;
    private Thread me = null;

    public Talker(List<Client> clients, FIFO<Message> msgqueue){
        this.clients = clients;
        this.msgqueue = msgqueue;
        msgqueue.attach(this);
    }

    @Override
    public void run() {
        me = Thread.currentThread();
        while(!shutdown){
            while(msgqueue.size() > 0){
                Message msg = msgqueue.pop();
                System.out.println("New message: " + msg.getMessage());
                synchronized (clients) {
                    for(Client client : clients){
                        try {
                            client.getDataOutputStream().writeUTF(Message.Format(msg));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                synchronized (me) {
                    me.wait(Math.max(0, waittime));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown(){
        shutdown = true;
    }

    @Override
    public void FIFONotEmpty() {
        synchronized (me) {
            me.notify();
        }
    }

    public void sendMessage(Client client, Message msg) {
        try {
            client.getDataOutputStream().writeUTF(Message.Format(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void J_OK(Client client) throws MessageProtocolException{
        System.out.println("Sending ok");
        try {
            client.getDataOutputStream().writeUTF("J_OK");
        } catch (IOException e) {
            System.out.println("Failed to send OK to " + client.getUser().getDisplayName());
        }
    }

    public void J_ER(Client client, int errmsg, String msg) throws MessageProtocolException{
        try {
            client.getDataOutputStream().writeUTF("J_ER " + errmsg + ":" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
