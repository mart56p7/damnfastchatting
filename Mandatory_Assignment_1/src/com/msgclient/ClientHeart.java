package com.msgclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHeart implements Runnable {

    private volatile Socket socket = null;
    private boolean shutdown = false;
    private DataOutputStream dos = null;

    public ClientHeart(Socket socket) throws IOException {
        this.socket = socket;
        dos = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            if(this.socket != null) {
                do{
                    synchronized (this) {
                        wait(1000 * 60);
                    }
                    try {
                        synchronized (socket){
                            //socket send heartbeat
                            dos.writeUTF("IMAV");
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
