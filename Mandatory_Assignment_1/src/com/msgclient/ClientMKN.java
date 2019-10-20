package com.msgclient;

import com.msgresources.MessageProtocolException;
import com.msgresources.MessageProtocolInterface;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientMKN implements Runnable, MessageProtocolInterface {
   private Socket socket = null;
   private boolean shutdown = false;
   private ClientGUIInterface cgui = null;

   public ClientMKN(ClientGUIInterface cgui) {
        this.cgui = cgui;
        this.cgui.receivedMessage("System", "Welcome to the chat system.", true);
        this.cgui.receivedMessage("System", "Commands", true);
        this.cgui.receivedMessage("System", "JOIN <<user_name>>, <<server_ip>>:<<server_port>>", true);
        this.cgui.receivedMessage("System", "", true);
        this.cgui.receivedMessage("System", "Examples", true);
        this.cgui.receivedMessage("System", "JOIN ImAwesome, 192.10.0.1:5000", true);
   }

   public void shutdown(){
       shutdown = true;
   }

   public void send(String msg){
        //Lets tokanize over string and see what we got
       try{
           //Here we call the functions
           throw new MessageProtocolException("test");
       }catch(MessageProtocolException e){
           cgui.error(e.getMessage());
       }
   }

   @Override
   public void run() {
       while(!shutdown){
           if(socket != null) {

           }
           try {
               System.out.println("\n");
               Thread.sleep(2000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
   }

   @Override
   public void JOIN(String username, String server_ip, int port) throws MessageProtocolException {
       try {
           socket = new Socket(server_ip, port);
       } catch (IOException e) {
           throw new MessageProtocolException("Failed to connect");
       }
   }

   @Override
   public void J_OK() throws MessageProtocolException {

   }

   @Override
   public void J_ER(int errcode, String errmsg) {

   }

   @Override
   public void DATA(String username, String text) throws MessageProtocolException {

   }

   @Override
   public void IMAV() throws MessageProtocolException {

   }

   @Override
   public void QUIT() throws MessageProtocolException {

   }

   @Override
   public List<String> LIST() throws MessageProtocolException {
      return null;
   }
}