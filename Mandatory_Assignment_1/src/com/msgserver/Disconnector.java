package com.msgserver;

import com.msgresources.Message;

import java.io.IOException;
import java.util.List;

//We disconnects clients after 180 seconds
public class Disconnector implements Runnable {
    private final List<Client> clients;
    private Talker talker;
    private boolean shutdown = false;
    private long nextchecktime = 0;
    private static long defaultchecktime = 180000;


    public Disconnector(List<Client> clients, Talker talker){ // !!!!
        this.clients = clients;
        this.nextchecktime = 10000;
        this.talker = talker;
    }


    @Override
    public void run() {
        while(!shutdown){
            synchronized (this){
                try {
                    synchronized (this.clients){
                        for (Client client : clients) {
                            long timeElapsed = System.currentTimeMillis() - client.getTime();
                            nextchecktime = Math.min(nextchecktime, defaultchecktime - timeElapsed);
                            if (timeElapsed >= defaultchecktime) {
                                disconnect(client);
                                break;
                            }
                        }
                    }
                    if(nextchecktime < 0) nextchecktime = 1000;
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

    public void disconnect(Client client){
        synchronized (clients){
            talker.sendMessage(client, new Message("You have been disconnected from the server"));
            clients.remove(client);
            client.close();
            talker.LIST();
            notify();
        }
    }
}
