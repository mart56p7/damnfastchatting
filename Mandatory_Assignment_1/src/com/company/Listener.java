package com.company;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Listener implements Runnable {

    private final List<Socket> sockets;
    private final int waittime = 5000;
    private boolean running = true;

    public Listener(List<Socket> sockets){
        this.sockets = sockets;
    }

    @Override
    public void run() {
        while(running){
            long start = System.currentTimeMillis();
            synchronized (sockets){for(Socket s: sockets){
                try {
                    BufferedInputStream in = new BufferedInputStream(s.getInputStream());
                    if(in.available() > 0){
                        int bytes = in.read();
                        // do thing
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}
            long end = System.currentTimeMillis();
            try {
                Thread.sleep(waittime - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void Shutdown(){
        running = false;
    }
}
