      /*
JOIN <<user_name>>, <<server_ip>>:<<server_port>>
From client to server.
The user name is given by the user. Username is max 12 chars long, only
letters, digits, ‘-‘ and ‘_’ allowed.

J_OK
From server to client.
Client is accepted.

J_ER <<err_code>>: <<err_msg>>
From server to client.
Client not accepted. Duplicate username, unknown command, bad command or
any other errors.

DATA <<user_name>>: <<free text
...
>>
From client to server.
From server to all clients.
First part of message indicates from which user it is, the colon(:)
indicates where the user message begins. Max 250 user characters.

IMAV
From client to server.
Client sends this heartbeat alive every 1 minute.

QUIT
From client to server.
Client is closing down and leaving the group.

LIST <<name1 name2 name3 
...
>>
From server to client.
A list of all active user names is sent to all clients, each time the
list at the server changes
*/
   
   
   public interface ClientInterface{
   void JOIN(String username, String server_ip, int pot) throws ClientException;
   void J_OK() throws ClientException;
   void J_ER(int errcode, String errmsg) throws ClientException;
   void DATA(String username, String text) throws ClientException;
   void IMAV() throws ClientException;
   void QUIT() throws ClientException;
   java.util.List<String> LIST() throws ClientException;
}
