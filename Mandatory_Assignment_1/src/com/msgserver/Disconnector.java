package com.msgserver;

import com.msgresources.Message;

import java.util.List;

/**
 * Disconnects clients after 180 seconds of inactivity
 */

public class Disconnector implements Runnable {
    private final List<Client> clients;
    private Talker talker;
    private boolean shutdown = false;
    private long nextchecktime = 0;
    private static long defaultchecktime = 180000;
    private Thread me = null;

    /**
     * @param clients the list of clients
     * @param talker the object that is used to talk to clients.
     * */
    public Disconnector(List<Client> clients, Talker talker){ // !!!!
        this.clients = clients;
        this.nextchecktime = 10000;
        this.talker = talker;
    }


    /**
     * Loops through clients, and disconnects inactive clients.
     * */
    @Override
    public void run() {
        me = Thread.currentThread();
        while(!shutdown){
            synchronized (this){
                try {
                    synchronized (this.clients){
                        for (Client client : clients) {

                            long timeElapsed = System.currentTimeMillis() - client.getTime();
                            //System.out.println("Checking if anyone needs to be disconnected " + timeElapsed);
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

    /**
     * Shutdowns the Disconnector object
     * */
    public void shutdown(){
        this.shutdown = true;
        if(me != null){
            me.interrupt();
        }
    }

    /**
     * @param client disconnects the given client
     * */
    private void disconnect(Client client){
        synchronized (clients){
            try {
                talker.sendMessage(client, new Message("J_ER 450:You have been disconnected from the server"));
            }
            catch(Exception e){
                System.out.println("Failed to send disconnect to client");
            }
            finally {
                clients.remove(client);
                client.close();
                talker.LIST();
                notify();
            }
        }
    }
}
