package com.msgclient;

import com.msgresources.MessageProtocolException;
import com.msgresources.User;
import com.msgresources.UserInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class handles input from the GUI, and formats it to application level commands, that are send to the chat server
 * */
public class ClientNetwork implements ClientNetworkInterface {
   private boolean shutdown = false;
   private ClientGUISwingInterface cgui = null;

   //Connection specific settings
   private boolean connected = false;
   private String username = null;
   private Socket socket = null;
   private ClientNetworkHeartbeat heart = null;
   private Thread heartThread = null;
   private DataInputStream dis = null;
   private DataOutputStream dos = null;

   //Regex
   private static final Pattern join_pattern = Pattern.compile("\\AJOIN ([a-zA-Z_0-9-]{1,12}), ((\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})):(\\d{1,65535})\\Z");
   private static final Pattern msg_pattern = Pattern.compile("\\ADATA ([a-zA-Z_0-9-]{1,12}):((.){1,255})\\Z");
   private static final Pattern error_pattern = Pattern.compile("\\AJ_ER (\\d+):((.){1,255})\\Z");
   private static final Pattern list_pattern = Pattern.compile("([a-zA-Z_0-9-]{1,12})+"); //LIST must be removed before running regex on string

    public ClientNetwork(ClientGUISwingInterface cgui) {
        this.cgui = cgui;
        help();
   }

   public void shutdown(){
       shutdown = true;
       if(heart != null) {
           heart.shutdown();
       }
   }

   private void help(){
       this.cgui.receivedMessage("System", "", true);
       this.cgui.receivedMessage("System", "Welcome to the chat system.", true);
       this.cgui.receivedMessage("System", "Commands", true);
       this.cgui.receivedMessage("System", "JOIN <<user_name>>, <<server_ip>>:<<server_port>>", true);
       this.cgui.receivedMessage("System", "QUIT", true);
       this.cgui.receivedMessage("System", "", true);
       this.cgui.receivedMessage("System", "Examples", true);
       this.cgui.receivedMessage("System", "Example join server:", true);
       this.cgui.receivedMessage("System", "JOIN ImAwesome, 127.0.0.1:5000", true);
       this.cgui.receivedMessage("System", "", true);
       this.cgui.receivedMessage("System", "Example close connection to server and quit program", true);
       this.cgui.receivedMessage("System", "QUIT", true);
       this.cgui.receivedMessage("System", "", true);
   }

   public void send(String msg) throws MessageProtocolException{
       //Remove any spaces
       System.out.println("send: " + msg);
       msg.trim();
       if(msg.toLowerCase().equals("quit")){
           if(connected) {
               QUIT();
           }
           shutdown();
       }
       else if(msg.toLowerCase().equals("help")){
           help();
       }
       else
       {
           //http://tutorials.jenkov.com/java-regex/matcher.html
           Matcher join_matcher = join_pattern.matcher(msg);
           if(join_matcher.find()){
               if(connected){
                   QUIT();
                   reset();
                   cgui.receivedMessage("System", "Closing connection to server..", true);
               }

               cgui.receivedMessage("System", "Connecting to server " + join_matcher.group(2) + " on port " + Integer.parseInt(join_matcher.group(7)), true);
               JOIN(join_matcher.group(1), join_matcher.group(2), Integer.parseInt(join_matcher.group(7)));
           }
           else if(connected){
               if(!msg.equals("")) {
                   System.out.println("sending data..");
                   DATA(username, msg);
               }
           }
           else{
               if(!connected){
                   cgui.error("You are not connected to a server!");
               }else{
                   cgui.error("Unknown command '" + connected + "'");
               }
           }
       }
   }

   @Override
   public void run() {
       do{
           if(socket != null && dis != null) {
               synchronized (dis) {
                   try {
                       if (dis.available() > 0) {
                           String input = dis.readUTF();
                           System.out.println("Received: " + input);
                           Matcher msg_matcher = msg_pattern.matcher(input);
                           Matcher error_matcher = error_pattern.matcher(input);
                           Matcher list_matcher = null;
                           if (input.startsWith("LIST ")) {
                               input = input.substring(5, input.length());
                               list_matcher = list_pattern.matcher(input);
                           }

                           if (msg_matcher.find()) {
                               System.out.println("new Message arrived");
                               cgui.receivedMessage(msg_matcher.group(1), msg_matcher.group(2), msg_matcher.group(1).equals(username));
                           } else if (input.equals("J_OK")) {
                               try {
                                   J_OK();
                               } catch (MessageProtocolException e) {
                                   cgui.error("Failed to finish handshake with server error message " + e.getMessage());
                               }


                           } else if (error_matcher.find()) {
                               cgui.error(error_matcher.group(1) + ":" + error_matcher.group(2));
                           } else if (list_matcher != null && list_matcher.find()) {
                               List<UserInterface> users = new ArrayList<>();
                               users.add(new User(list_matcher.group(1)));
                               while (list_matcher.find()) {
                                   users.add(new User(list_matcher.group(1)));
                               }
                               LIST(users);
                           } else {
                               cgui.error("500: Unknown command from server: '" + input + "'");
                           }
                       }
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
           try {
               Thread.sleep(125);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }while(!shutdown);
   }

   public void JOIN(String username, String server, int port) throws MessageProtocolException {
       try {
           this.socket = new Socket(server, port);
           dis = new DataInputStream(socket.getInputStream());
           dos = new DataOutputStream(socket.getOutputStream());
           serverSend("JOIN " + username + ", " + server + ":" + port);
           this.username = username;
       } catch (IOException e) {
           throw new MessageProtocolException("Failed to connect to " + server + ":" + port + " with message: " + e.getMessage());
       } catch (MessageProtocolException m){
           throw new MessageProtocolException("Failed to connect to " + server + ":" + port + " with message: " + m.getMessage());
       }
   }

   public void J_OK() throws MessageProtocolException {
        if(username != null && socket != null && connected == false){
            connected = true;
            System.out.println("J_OK " + connected);
            cgui.receivedMessage("System", "You are now connected to " + socket.getInetAddress() + " on port " + socket.getPort(), true);
            try {
                this.heart = new ClientNetworkHeartbeat(socket, this);
                this.heartThread = new Thread(this.heart);
                heartThread.start();
            } catch (IOException e) {
                this.socket = null;
                this.username = "";
                this.connected = false;
                this.heartThread = null;
                throw new MessageProtocolException("Failed to connect - Missing a heart");
            }
        }
   }

   public void J_ER(int errcode, String errmsg) {
        cgui.error(String.valueOf(errcode) + ": " + errmsg);
   }

   public void DATA(String username, String text) throws MessageProtocolException {
        serverSend("DATA " + username + ":" + text);
   }

   public void QUIT() throws MessageProtocolException {
        serverSend("QUIT");
   }

    public void LIST(List<UserInterface> users) {
        cgui.updateUserList(users);
    }

    private void serverSend(String msg) throws MessageProtocolException{
        if(dos != null){
            try {
                System.out.println("Sending " + msg);
                dos.writeUTF(msg);
            } catch (IOException e) {
                new MessageProtocolException(e.getMessage());
                disconnected();
            }
        }
    }

    public void disconnected(){
        if(heart != null){
            if(connected) {
                cgui.receivedMessage("System", "Disconnected from server. Closing connection", true);
            }
            heart.shutdown();
            try {
                heartThread.join();
                reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void reset(){
        shutdown = false;

        //Connection specific settings
        connected = false;
        username = null;
        socket = null;
        heart = null;
        heartThread = null;
        dis = null;
        dos = null;
    }
}