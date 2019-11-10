package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.util.regex.Matcher;

public class ClientMessageOperationOutQuit extends ClientMessageOperation{
    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationOutQuit(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a Quit message
     * @return is the msg is a Quit message returns true, else returns false
     * */
    @Override
    public boolean command(Message msg) throws MessageProtocolException {

        //If the message matches our regex
        if(msg.getMessage().equalsIgnoreCase("quit")){
            //If we are connected to a server
            if(client.isConnected()){
                //Send the message
                client.sendMessage("QUIT");
            }
            return true;
        }
        return false;
    }
}
