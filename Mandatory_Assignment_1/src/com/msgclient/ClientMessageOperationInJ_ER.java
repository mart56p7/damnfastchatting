package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Error message that is send from server to client, is handled in this ClientMessageOperation
 * */
public class ClientMessageOperationInJ_ER extends ClientMessageOperation {
    //Regex pattern for the error message
    private static final Pattern error_pattern = Pattern.compile("\\AJ_ER (\\d+):((.){1,255})\\Z");

    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationInJ_ER(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a J_ER message
     * @return is the msg is a J_ER message returns true, else returns false
     * */
    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher error_matcher = error_pattern.matcher(msg.getMessage());

        if (error_matcher.find()) {
            cgui.receivedMessage("System", "Server responded with Error code: " + error_matcher.group(1) + ", Message: " + error_matcher.group(2), true);
            return true;
        }
        return false;
    }
}
