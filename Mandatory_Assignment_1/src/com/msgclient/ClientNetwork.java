package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.io.IOException;
import java.util.regex.Pattern;


/**
 * This class handles input from the GUI, and formats it to application level commands, that are send to the chat server
 * The object listens for new commands send from the server
 * and sends commands using a Chain of Responsibility pattern using one or more ClientMessageController objects.
 * */
public class ClientNetwork implements ClientNetworkInterface {
   private boolean shutdown = false;
   private ClientGUISwingInterface cgui = null;
   private ClientMessageController cmic = null;
    private ClientMessageController cmoc = null;
   private Client client;


   //Regex
   //private static final Pattern join_pattern = Pattern.compile("\\AJOIN ([a-zA-Z_0-9-]{1,12}), ((\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})):(\\d{1,65535})\\Z");
   //private static final Pattern msg_pattern = Pattern.compile("\\ADATA ([a-zA-Z_0-9-]{1,12}):((.){1,255})\\Z");
   //private static final Pattern error_pattern = Pattern.compile("\\AJ_ER (\\d+):((.){1,255})\\Z");
   //private static final Pattern list_pattern = Pattern.compile("([a-zA-Z_0-9-]{1,12})+"); //LIST must be removed before running regex on string

    public ClientNetwork(ClientGUISwingInterface cgui, Client client, ClientMessageController cmic, ClientMessageController cmoc){
        this.cgui = cgui;
        this.client = client;
        this.cmic = cmic;
        this.cmoc = cmoc;
        welcomemsg();
    }

    public ClientNetwork(ClientGUISwingInterface cgui) {
        this(cgui, new Client(), null, null);
   }

   public void shutdown(){
       shutdown = true;
       if(client != null){
           client.destroy();
       }
   }

   private void welcomemsg(){
       this.cgui.receivedMessage("System", "", true);
       this.cgui.receivedMessage("System", "Welcome to the chat system.", true);
       this.cgui.receivedMessage("System", "Commands", true);
       this.cgui.receivedMessage("System", "JOIN <<user_name>>, <<server_ip>>:<<server_port>>", true);
       this.cgui.receivedMessage("System", "- After having connected to a server, type any none command key words to send to the chat server", true);
       this.cgui.receivedMessage("System", "QUIT", true);
       this.cgui.receivedMessage("System", "HELP", true);
       this.cgui.receivedMessage("System", "", true);
       this.cgui.receivedMessage("System", "Examples", true);
       this.cgui.receivedMessage("System", "Example join server:", true);
       this.cgui.receivedMessage("System", "JOIN ImAwesome, 127.0.0.1:5000", true);
       this.cgui.receivedMessage("System", "", true);
       this.cgui.receivedMessage("System", "Example close connection to server and quit program", true);
       this.cgui.receivedMessage("System", "QUIT", true);
       this.cgui.receivedMessage("System", "", true);
       this.cgui.receivedMessage("System", "Show this menu", true);
       this.cgui.receivedMessage("System", "HELP", true);
       this.cgui.receivedMessage("System", "", true);
   }

   public void send(String msg) throws MessageProtocolException{
        if(cmoc.command(new Message(msg)) == false){
            if(client.getUser() == null || cmoc.command(new Message("DATA " + client.getUser().getDisplayName() + ":" + msg)) == false){
                cgui.error("Unknown command");
            }
        }
   }

   @Override
   public void run() {
        do{
            if(client.getSocket() != null && client.getDataInputStream() != null){
                String msg = client.getMessage();
                if(msg != null && cmic != null){
                    if(cmic.command(new Message(msg)) == false){
                        cgui.receivedMessage("System", "Unknown command received from server", true);
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(!shutdown);
   }
}