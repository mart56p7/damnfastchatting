package com.msgresources;

import java.io.IOException;

public interface MessageProtocolInterface {

   /**
    * Called when the client is verified
    * J_OK
    * From server to client.
    * Client is accepted.
    * */
   void J_OK() throws MessageProtocolException;

   /**
    * LIST <<name1 name2 name3
    * ...
    * >>
    * From server to client.
    * A list of all active user names is sent to all clients, each time the
    * list at the server changes
    * */
   java.util.List<String> LIST() throws MessageProtocolException;

   /**
    * Called if an error occures
    * J_ER <<err_code>>: <<err_msg>>
    * From server to client.
    * Client not accepted. Duplicate username, unknown command, bad command or
    * any other errors.
    * */
   void J_ER(int errcode, String errmsg);

   /**
    * DATA <<user_name>>: <<free text
    * ...
    * >>
    * From client to server.
    * From server to all clients.
    * First part of message indicates from which user it is, the colon(:)
    * indicates where the user message begins. Max 250 user characters.
    * */
   void DATA(String username, String text) throws MessageProtocolException;


   /**
   * Called when a client join
   * JOIN <<user_name>>, <<server_ip>>:<<server_port>>
   * JOIN <<user_name>>, <<server_ip>>:<<server_port>>
   * From client to server.
   * The user name is given by the user. Username is max 12 chars long, only
   * letters, digits, �-� and �_� allowed.
   * */

   void JOIN(String username, String server_ip, int port) throws MessageProtocolException, IOException;

   /**
    * IMAV
    * From client to server.
    * Client sends this heartbeat alive every 1 minute.
    * */
   void IMAV() throws MessageProtocolException;
   /**
    * QUIT
    * From client to server.
    * Client is closing down and leaving the group.
    * */
   void QUIT() throws MessageProtocolException;


}
