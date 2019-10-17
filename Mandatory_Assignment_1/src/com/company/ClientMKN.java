package com.company;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

public class ClientMKN implements Runnable, ClientInterface{
   Socket socket = null;

   public ClientMKN(String address, int port) throws IOException {
      socket = new Socket(address, port);
   }

   @Override
   public void run() {
      if(socket != null){

      }
   }

   @Override
   public void JOIN(String username, String server_ip, int pot) throws ClientException {

   }

   @Override
   public void J_OK() throws ClientException {

   }

   @Override
   public void J_ER(int errcode, String errmsg) throws ClientException {

   }

   @Override
   public void DATA(String username, String text) throws ClientException {

   }

   @Override
   public void IMAV() throws ClientException {

   }

   @Override
   public void QUIT() throws ClientException {

   }

   @Override
   public List<String> LIST() throws ClientException {
      return null;
   }
}