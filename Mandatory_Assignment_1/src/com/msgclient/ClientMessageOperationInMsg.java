package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageOperationInMsg extends ClientMessageOperation {
    private static final Pattern msg_pattern = Pattern.compile("\\ADATA ([a-zA-Z_0-9-]{1,12}):((.){1,255})\\Z");

    public ClientMessageOperationInMsg(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher msg_matcher = msg_pattern.matcher(msg.getMessage());
        if(msg_matcher.find()){
            cgui.receivedMessage(msg_matcher.group(1), msg_matcher.group(2), msg_matcher.group(1).equals(client.getUser().getDisplayName()));
            return true;
        }
        return false;
    }
}
