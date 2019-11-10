package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageOperationOutMsg extends ClientMessageOperation {
    private static final Pattern msg_pattern = Pattern.compile("\\ADATA ([a-zA-Z_0-9-]{1,12}):((.){1,255})\\Z");

    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationOutMsg(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a Message message
     * @return is the msg is a Message message returns true, else returns false
     * */
    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher msg_matcher = msg_pattern.matcher(msg.getMessage());
        //If the message matches our regex
        if(msg_matcher.find()){
            //If we are connected to a server
            if(client.isConnected()){
                //Send the message
                client.sendMessage(msg.getMessage());
            }
            return true;
        }
        return false;
    }
}
