package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageOperationInJ_ER extends ClientMessageOperation {
    private static final Pattern error_pattern = Pattern.compile("\\AJ_ER (\\d+):((.){1,255})\\Z");

    public ClientMessageOperationInJ_ER(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

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
