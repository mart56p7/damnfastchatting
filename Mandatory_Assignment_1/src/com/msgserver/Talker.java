package com.msgserver;

import com.msgresources.FIFO;
import com.msgresources.FIFOObserver;

import java.net.Socket;
import java.util.List;

public class Talker implements Runnable, FIFOObserver {

    private volatile List<Socket> sockets = null;
    private volatile FIFO<Message> msgqueue = null;
    private final int waittime = 5000;
    private boolean shutdown = false;
    private Thread me = null;

    public Talker(List<Socket> sockets, FIFO<Message> msgqueue){
        this.sockets = sockets;
        this.msgqueue = msgqueue;
        msgqueue.attach(this);
    }

    @Override
    public void run() {
        me = Thread.currentThread();
        while(!shutdown){
            while(msgqueue.size() > 0){
                Message msg = msgqueue.pop();
                synchronized (sockets) {
                    for(Socket s : sockets){
                        //Send msg to each socket
                    }
                }
            }
            try {
                wait(waittime);
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
        me.notify();
    }
}
