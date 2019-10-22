package com.msgserver;

import java.util.List;

//We disconnects clients after 180 seconds
public class Disconnector implements Runnable {
    private List<Client> clients = null;
    private boolean shutdown = false;
    private long nextchecktime = 0;
    private static long defaultchecktime = 180000;
    public Disconnector(List<Client> clients){
        this.clients = clients;
        this.nextchecktime = 180000;
    }


    @Override
    public void run() {
        while(!shutdown){
            synchronized (this){
                try {

                    synchronized (this.clients){
                        for(int i = 0; i < clients.size(); i++){
                            //Math.max(0, defaultchecktime - (java.lang.System.currentTimeMillis() - clients.get(i).getTime()))
                        }
                    }
                    wait(nextchecktime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void shutdown(){
        this.shutdown = true;
    }
}
