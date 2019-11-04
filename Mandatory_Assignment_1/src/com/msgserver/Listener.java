package com.msgserver;

import com.msgresources.*;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Listener implements Runnable {

    private volatile List<Client> clients;
    private volatile FIFO<Message> msgqueue;
    private final int waittime = 100;
    private boolean running = true;
    private Talker talker;

    private static final Pattern join_pattern = Pattern.compile("\\AJOIN ([a-zA-Z_0-9-]{1,12}), ((\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})):(\\d{1,65535})\\Z");
    private static final Pattern msg_pattern = Pattern.compile("\\ADATA ([a-zA-Z_0-9-]{1,12}):((.){1,255})\\Z");

    public Listener(List<Client> clients, FIFO<Message> msgqueue, Talker talker){

        this.clients = clients;
        this.msgqueue = msgqueue;
        this.talker = talker;
    }

    @Override
    public void run() {
        while(running){
            //System.out.println("Running" + clients.size());
            long start = System.currentTimeMillis();
            synchronized (clients){
                for(Client client: clients){
                    try {
                        //System.out.println("Messages in Listener " + client.getDataInputStream().available());
                        if(client != null && client.getDataInputStream().available() > 0){
                            System.out.println("Data available");
                            System.out.println("Reading data");
                            String msg = client.getDataInputStream().readUTF();

                            System.out.println("Done reading data");
                            //Look at message data
                            try {
                                System.out.println("Calling messagee controller with " + msg);
                                msgcontroller(client, msg);
                            } catch (MessageProtocolException e) {
                                System.out.println("Message failed with the error " + e.getMessage());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            long end = System.currentTimeMillis();
            try {
                //System.out.println("Sleeping" + Math.min(waittime - (end - start), 0));
                Thread.sleep(Math.max(waittime - (end - start), 0));
                //System.out.println("Im awake");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void Shutdown(){
        running = false;
    }

    private void msgcontroller(Client client, String msg) throws MessageProtocolException{
        Matcher matcher_join = join_pattern.matcher(msg);
        Matcher matcher_msg = msg_pattern.matcher(msg);
        System.out.println("msgcontroller received " + msg);
        if(matcher_join.find()){
            System.out.println("New join");
            newJoin(matcher_join.group(1), client);
        }
        else if(msg.equals("IMAV")){
            System.out.println("Revieved heart by " + client.getUser().getDisplayName());
            client.updateTime();
        }
        else if(msg.equals("QUIT")){
            //Remove client and update client list for all
            System.out.println("\bbefore Removed client " + clients.size());
            synchronized (clients){
                clients.remove(client);
                client.close();
            }
            for(int i = 0; i < clients.size(); i++){
                if(clients.get(i).getUser() != null && clients.get(i).getUser().getDisplayName() != null){
                    System.out.println(clients + " " + clients.get(i).getUser().getDisplayName());
                }
                else if(clients.get(i).getUser() != null){
                    System.out.println(clients + " " + clients.get(i).getUser());
                }else{
                    System.out.println(clients);
                }
            }
            System.out.println("\bafter Removed client " + clients.size());
            //Sends user list to all users.
            talker.LIST();
        }
        else if(matcher_msg.find()){
            System.out.println("new msg");
            //Username
            String username = matcher_msg.group(1);
            //Msg
            String message = matcher_msg.group(2);
            newMessage(username, message, client);
        }
        else{
            System.out.println("Unknown msg");
            //Tell client we dont understand em
            talker.sendMessage(client, errormsg(404, "Unknown command"));
        }
    }

    private Message errormsg(int id, String msg){
        return new Message("J_ER "+id+":"+msg);
    }

    private Message okmsg(){
        return new Message("J_OK");
    }

    private void newMessage(String username, String message, Client client){
        if(client.getUser().getDisplayName().equals(username)) {
            msgqueue.push(new Message(message, client.getUser()));
        }
        else{
            talker.sendMessage(client, errormsg(404, "Unknown command"));
        }
    }

    private void newJoin(String username, Client client) throws MessageProtocolException {
        boolean notuniquename = false;
        synchronized (clients){
            for(Client c : clients){
                if(c.getUser() != null) {
                    notuniquename = c.getUser().getDisplayName().equals(username) || notuniquename;
                }
            }
        }
        if(!notuniquename) {
            synchronized (client) {
                client.setUser(new User(username));
                talker.J_OK(client);
            }
        }
        else{
            talker.J_ER(client, 400, "Not uniqueu username");
        }
    }

}
