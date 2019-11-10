package com.msgclient;

import java.io.IOException;

/**
 * Heartbeat
 * Makes sure that a message is send to the server every 60 seconds.
 * */
public class ClientNetworkHeartbeat implements Runnable {

    private volatile Client client = null;
    private boolean shutdown = false;
    private Thread me = null;
    public ClientNetworkHeartbeat(Client client) throws IOException {
        this.client = client;
    }

    @Override
    public void run() {
        me = Thread.currentThread();
        try {
            if(this.client != null && this.client.getSocket() != null) {
                do{
                    synchronized (this) {
                        wait(1000 * 60);
                    }
                    try {
                        if(this.client != null){
                            synchronized (this.client.getSocket()){
                                //socket send heartbeat
                                client.getDataOutputStream().writeUTF("IMAV");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }while(!shutdown);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(){
        this.shutdown = true;
        me.interrupt();
    }

}
