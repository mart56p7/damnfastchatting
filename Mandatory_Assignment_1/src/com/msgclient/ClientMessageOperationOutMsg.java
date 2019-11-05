package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageOperationOutMsg extends ClientMessageOperation {
    private static final Pattern msg_pattern = Pattern.compile("\\ADATA ([a-zA-Z_0-9-]{1,12}):((.){1,255})\\Z");

    public ClientMessageOperationOutMsg(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher msg_matcher = msg_pattern.matcher(msg.getMessage());
        System.out.println("1: " + msg.getMessage());
        if(msg_matcher.find()){
            System.out.println("2");
            if(client.isConnected()){
                System.out.println("3");
                client.sendMessage(msg.getMessage());
            }
            return true;
        }
        return false;
    }
}
