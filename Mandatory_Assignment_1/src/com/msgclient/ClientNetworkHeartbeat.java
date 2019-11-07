package com.msgclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Heartbeat
 * */
public class ClientNetworkHeartbeat implements Runnable {

    private volatile Client client = null;
    private boolean shutdown = false;

    public ClientNetworkHeartbeat(Client client) throws IOException {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            if(this.client.getSocket() != null) {
                do{
                    synchronized (this) {
                        wait(1000 * 60);
                    }
                    try {
                        synchronized (this.client.getSocket()){
                            //socket send heartbeat
                            client.getDataOutputStream().writeUTF("IMAV");
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
    }

}
