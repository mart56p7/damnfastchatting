package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageOperationInMsg extends ClientMessageOperation {
    private static final Pattern msg_pattern = Pattern.compile("\\ADATA ([a-zA-Z_0-9-]{1,12}):((.){1,255})\\Z");

    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationInMsg(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a Message message
     * @return is the msg is a Message message returns true, else returns false
     * */
    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher msg_matcher = msg_pattern.matcher(msg.getMessage());
        if(msg_matcher.find()){
            //Using the regex we extract data from the message and we check if its the user it self that has written the message as the third parameter to receivedMessage
            cgui.receivedMessage(msg_matcher.group(1), msg_matcher.group(2), msg_matcher.group(1).equals(client.getUser().getDisplayName()));
            return true;
        }
        return false;
    }
}
